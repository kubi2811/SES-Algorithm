package Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Sender implements ISender {
    private Socket socket;
    private ObjectOutputStream outputStream;

    public Sender(Socket socket) throws IOException {
        this.socket = socket;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public void send(IMessage message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}