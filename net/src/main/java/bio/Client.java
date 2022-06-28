package bio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        //客户端必备
        Socket socket = null;
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;

        //
        InetSocketAddress serverAddress = new InetSocketAddress("127.0.0.1", 10001);

        while (true){
            try {
                socket = new Socket();

                socket.connect(serverAddress);

                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());

                outputStream.writeUTF("Ori 1");
                outputStream.flush();

                System.out.println(inputStream.readUTF());
            } finally {
                if (socket != null) {
                    socket.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }

    }
}
