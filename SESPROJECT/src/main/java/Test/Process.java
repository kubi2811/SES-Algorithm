package Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Process implements IProcess {
    private int id;
    private String ipAddress;
    private int port;
    private int numProcesses;

    public Process(int id, String ipAddress, int port, int numProcesses) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.port = port;
        this.numProcesses = numProcesses;
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            IBuffer[] buffers = new IBuffer[numProcesses];
            ISender[] senders = new ISender[numProcesses];
            IReceiver[] receivers = new IReceiver[numProcesses];

            for (int i = 0; i < numProcesses; i++) {
                if (i != id) {
                    buffers[i] = new Buffer();
                    Socket socket = new Socket(ipAddress, port + i);
                    senders[i] = new Sender(socket);
                    receivers[i] = new Receiver(socket, buffers[id]);
                    new Thread((Runnable) receivers[i]).start();
                }
            }

            for (int i = 0; i < numProcesses; i++) {
                if (i != id) {
                    for (int j = 1; j <= 6; j++) {
                        new Thread(new MessageSender(id, i, j, buffers[i], senders[i])).start();
                    }
                }
            }

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new MessageReceiver(id, clientSocket, buffers[id])).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}