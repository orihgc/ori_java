package nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioClientHandler implements Runnable {

    private String host;
    private int port;

    private SocketChannel socketChannel;
    private Selector selector;
    private volatile boolean started = false;


    public NioClientHandler(String host, int port) {
        this.host = host;
        this.port = port;

        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            started = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        while (started) {
            try {
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
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            SocketChannel sc = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                if (sc.finishConnect()) {
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else {
                    System.exit(1);
                }
            }
            if (key.isReadable()) {
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                //从SocketChannel中读取事件到buffer中，buffer是写模式
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0) {
                    //将buffer切换到读模式
                    readBuffer.flip();
                    //根据缓冲区可读字节数创建字符数组
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String result = new String(bytes, "UTF-8");
                    System.out.println("客户端收到消息：" + result);
                }
            }
        }
    }

    private void doWrite(SocketChannel sc, String request) throws IOException {
        byte[] bytes = request.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        sc.write(writeBuffer);
    }

    private void doConnect() throws IOException {
        //connect非阻塞，当他返回时，连接不一定完成了，
        // 如果返回值为true，表示连接完成
        // 如果返回值为false，表示连接未完成，还在三次握手的过程中
        if (socketChannel.connect(new InetSocketAddress(host, port))) {
            //监听OP_READ事件
            socketChannel.register(selector, SelectionKey.OP_READ);
        } else {
            //监听OP_CONNECT事件，表示select告诉我，连接已经完成
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    public void sendMessage(String msg) throws Exception {
        doWrite(socketChannel, msg);
    }
}
