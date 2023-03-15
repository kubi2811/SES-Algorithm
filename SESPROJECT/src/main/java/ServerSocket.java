import java.net.*;
import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerSocket implements Runnable {
    private int port;
    private BlockingQueue<String> queue;

    public ServerSocket(int port) {
        this.port = port;
        this.queue = new LinkedBlockingQueue<String>();
    }

    public void run() {
        try {
            java.net.ServerSocket serverSocket = new java.net.ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, queue);
                Thread t = new Thread(clientHandler);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);
        Thread t = new Thread(serverSocket);
        t.start();
    }
}
