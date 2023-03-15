import java.io.FileInputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        try {
            Properties props = new Properties();
            FileInputStream in = new FileInputStream("C:/Users/hieut/Desktop/HTPT/SES-Algorithm/SESPROJECT/src/main/java/config.properties");
            props.load(in);
            in.close();

            int port = Integer.parseInt(props.getProperty("port"));
            int numClients = Integer.parseInt(props.getProperty("numClients"));

            ServerSocket serverSocket = new ServerSocket(port);
            Thread t1 = new Thread(serverSocket);
            t1.start();

            for (int i = 0; i < numClients; i++) {
                String host = props.getProperty("host" + i);
                int clientId = i;
                ClientSocket clientSocket = new ClientSocket(host, port, clientId, numClients);
                Thread t2 = new Thread(clientSocket);
                t2.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}