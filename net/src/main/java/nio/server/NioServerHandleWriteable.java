package nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServerHandleWriteable implements Runnable {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private volatile boolean started = false;


    public NioServerHandleWriteable(int port) {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            //设置为false，将当前的通道设置为非阻塞模式
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            //注册OP_ACCEPT事件，表示serverChannel对客户端连接事件进行监听
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            started = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (started) {
            try {
                //等待1s，被唤醒一次，如果有事件产生，也会被唤醒
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
                SelectionKey selectionKey = null;
                while (selectionKeyIterator.hasNext()) {
                    selectionKey = selectionKeyIterator.next();
                    selectionKeyIterator.remove();
                    try {

                        handleInput(selectionKey);
                    } catch (IOException e) {
                        selectionKey.cancel();
                        if (selectionKey.channel() != null) {
                            selectionKey.channel().close();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isValid()) {
            //当前有链接进来了，需要处理
            if (selectionKey.isAcceptable()) {
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                //和客户端通信的socket
                SocketChannel socketChannel = serverSocketChannel.accept();
                System.out.println("有客户端连接");
                socketChannel.configureBlocking(false);
                //注册OP_READ事件，表示SocketChannel对OP_READ事件的监听
                socketChannel.register(selector, SelectionKey.OP_READ);
            }
            //读数据
            if (selectionKey.isReadable()) {
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                //readBytes表示我们从channel中读取到了多少数据
                //read操作是从SocketChannel中读取，写入到Buffer中，所以是写模式
                int readBytes = socketChannel.read(readBuffer);
                if (readBytes > 0) {
                    //因为channel写入了buffer，所以我们读的时候，要进行模式切换，切换到读模式
                    readBuffer.flip();
                    //读取数据做业务处理
                    byte[] bytes = new byte[readBytes];
                    readBuffer.get(bytes);
                    String message = new String(bytes, "UTF-8");
                    System.out.println("服务器收到消息:" + message);
                    String response = "收到你的消息了" + message;
                    doWrite(socketChannel, response);
                }
                //链路已经关闭，释放资源
                else if (readBytes < 0) {
                    selectionKey.cancel();
                    socketChannel.close();
                }
            }
            //isWritable表示发送缓存可写，也就是发送缓存有空间，就会一直为true
            if (selectionKey.isWritable()) {
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                ByteBuffer att = (ByteBuffer) selectionKey.attachment();
                if (att.hasRemaining()) {
                    int count = socketChannel.write(att);
                    System.out.println("write:" + count + "byte");
                } else {
                    //如果发送buffer里没有数据，就得取消对OP_WRITE的监听
                    selectionKey.interestOps(SelectionKey.OP_READ);
                }
            }
        }
    }

    private void doWrite(SocketChannel socketChannel, String response) throws IOException {
        byte[] bytes = response.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        //将字节数组复制到writeBuffer，属于写模式
        writeBuffer.put(bytes);
        //由于先写入了数据，要读取buffer数据到sc中的话，得先切换到读模式
        writeBuffer.flip();
        socketChannel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, writeBuffer);
    }

    public void stop() {
        started = false;
    }
}
