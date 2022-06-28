package nio.server;

public class NioServer {
    public static void main(String[] args) {
        new Thread(new NioServerHandler(10001)).start();
    }
}
