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
                //???SocketChannel??????????????????buffer??????buffer????????????
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0) {
                    //???buffer??????????????????
                    readBuffer.flip();
                    //????????????????????????????????????????????????
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String result = new String(bytes, "UTF-8");
                    System.out.println("????????????????????????" + result);
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
        //connect?????????????????????????????????????????????????????????
        // ??????????????????true?????????????????????
        // ??????????????????false?????????????????????????????????????????????????????????
        if (socketChannel.connect(new InetSocketAddress(host, port))) {
            //??????OP_READ??????
            socketChannel.register(selector, SelectionKey.OP_READ);
        } else {
            //??????OP_CONNECT???????????????select??????????????????????????????
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    public void sendMessage(String msg) throws Exception {
        doWrite(socketChannel, msg);
    }
}
