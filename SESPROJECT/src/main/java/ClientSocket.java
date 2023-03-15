import java.net.*;
import java.io.*;
import java.util.Random;

public class ClientSocket implements Runnable {
    private String host;
    private int port;
    private int clientId;
    private int numClients;
    private Random random;

    public ClientSocket(String host, int port, int clientId, int numClients) {
        this.host = host;
        this.port = port;
        this.clientId = clientId;
        this.numClients = numClients;
        this.random = new Random();
    }

    public void run() {
        try {
            Socket socket = new Socket(host, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            for (int i = 0; i < 6; i++) {
                Thread t = new Thread(new Sender(out, clientId, numClients));
                t.start();
            }
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Sender implements Runnable {
        private PrintWriter out;
        private int senderId;
        private int numClients;

        public Sender(PrintWriter out, int senderId, int numClients) {
            this.out = out;
            this.senderId = senderId;
            this.numClients = numClients;
        }

        public void run() {
            try {
                for (int i = 0; i < 150; i++) {
                    int receiverId = (senderId + random.nextInt(numClients - 1)) % numClients;
                    if (receiverId >= senderId) {
                        receiverId++;
                    }
                    String message = "message " + i + " from " + senderId + " to " + receiverId;
                    out.println(message);
                    System.out.println("Sending: " + message);
                    Thread.sleep(random.nextInt(1000));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        int clientId = Integer.parseInt(args[2]);
        int numClients = Integer.parseInt(args[3]);
        ClientSocket clientSocket = new ClientSocket(host, port, clientId, numClients);
        Thread t = new Thread(clientSocket);
        t.start();
    }
}
