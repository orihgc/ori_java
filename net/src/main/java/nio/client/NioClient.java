package nio.client;

import java.util.Scanner;

public class NioClient {
    private static NioClientHandler nioClientHandler = null;

    public static void main(String[] args) throws Exception {
        start();
        Scanner scanner = new Scanner(System.in);
        while (sendMsg(scanner.next()));
    }

    public static boolean sendMsg(String msg) throws Exception {
        nioClientHandler.sendMessage(msg);
        return true;
    }

    public static void start(){
        nioClientHandler = new NioClientHandler("127.0.0.1", 10001);
        new Thread(nioClientHandler).start();
    }

}
