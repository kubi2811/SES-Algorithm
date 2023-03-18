package Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class MessageReceiver implements Runnable {
    private int receiverId;
    private Socket socket;
    private IBuffer buffer;

    public MessageReceiver(int receiverId, Socket socket, IBuffer buffer) {
        this.receiverId = receiverId;
        this.socket = socket;
        this.buffer = buffer;
    }

    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            while (true) {
                IMessage message = (IMessage) inputStream.readObject();
                buffer.put(message);
                System.out.println("Process " + receiverId + " received message from process " + message.getSender() + ": " + message.getContent());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
