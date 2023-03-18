package Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Receiver implements IReceiver {
    private Socket socket;
    private IBuffer buffer;

    public Receiver(Socket socket, IBuffer buffer) throws IOException {
        this.socket = socket;
        this.buffer = buffer;
    }

    public void receive() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            while (true) {
                IMessage message = (IMessage) inputStream.readObject();
                buffer.put(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
