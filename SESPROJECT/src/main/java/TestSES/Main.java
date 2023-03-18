package TestSES;

import java.util.Random;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

class Message {
    int messageID;
    int senderID;
    int receiverID;
    String status;
    long timeStamp;

    public Message(int messageID, int senderID, int receiverID, String status, long timeStamp) {
        this.messageID = messageID;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.status = status;
        this.timeStamp = timeStamp;
    }
}

class Process {
    int processID;
    Message[] messageBuffer;
    Thread[] threads;

    public Process(int processID) {
        this.processID = processID;
        this.messageBuffer = new Message[150];
        this.threads = new Thread[6];
    }

    public Message[] generateRandomMessages() {
        Message[] messages = new Message[150];
        Random random = new Random();
        for (int i = 0; i < messages.length; i++) {
            int messageID = random.nextInt(100);
            int senderID = this.processID;
            int receiverID = random.nextInt(10);
            String status = "buffering";
            long timeStamp = System.currentTimeMillis();
            Message message = new Message(messageID, senderID, receiverID, status, timeStamp);
            messages[i] = message;
        }
        return messages;
    }

    public void sendMessage(Message message, int receiverID) {
        try {
            Socket socket = new Socket("localhost", 1234 + receiverID);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(message);
            out.flush();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessage() {
        try {
            ServerSocket serverSocket = new ServerSocket(8000 + this.processID);
            while (true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) in.readObject();
                in.close();
                socket.close();
                handleMessage(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void handleMessage(Message message) {
        message.status = "delivered";
        message.timeStamp = System.currentTimeMillis();
        messageBuffer[message.messageID % 150] = message;
    }

    public void startThreads() {
        for (int i = 0; i < threads.length; i++) {
            if (i < 3) {
                threads[i] = new Thread(() -> {
                    Message[] messages = generateRandomMessages();
                    for (Message message : messages) {
                        sendMessage(message, processID % 2 + 1);
                    }
                });
            } else {
                threads[i] = new Thread(() -> {
                    receiveMessage();
                });
            }
            threads[i].start();
        }
    }

    public void joinThreads() {
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Process process1 = new Process(1);
        Process process2 = new Process(2);

        process1.startThreads();
        process2.startThreads();

        process1.joinThreads();
        process2.joinThreads();
    }
}