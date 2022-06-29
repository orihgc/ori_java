package nio.server;

public class NioServerWriteable {
    public static void main(String[] args) {
        new Thread(new NioServerHandleWriteable(10001)).start();
    }
}
