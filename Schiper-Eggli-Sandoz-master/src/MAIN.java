import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import java.util.Random;


public class MAIN {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Properties properties = new Properties();

        try {
            FileInputStream file = new FileInputStream(args[0]);
            properties.load(file);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int numProcesses = Integer.valueOf(properties.getProperty("numProcess"));
        int numThread = Integer.valueOf(properties.getProperty("numThread"));
        int numMessages = Integer.valueOf(properties.getProperty("numMessage"));
        int numPort = Integer.valueOf(properties.getProperty("numPort"));
        Thread[][] myThreads = new Thread[numProcesses][numThread];
        try {
            // Create Registry
            Registry registry = LocateRegistry.createRegistry(numPort);


            int[][] destIDs = {{1,2,3,4,5,6},{0,2,3,4,5,6},{0,1,3,4,5,6},{0,1,2,4,5,6},{0,1,2,3,5,6},{0,1,2,3,4,6},{0,1,2,3,4,5}};
            String[][] messages = new String[numThread][numMessages];
            for (int i = 0; i < messages.length; i++) {
                for (int j = 0; j < messages[i].length; j++) {
                    messages[i][j] = "message" + (j+1);
                }
            }
            int[][] delays = new int[numThread][numMessages];
            Random rand = new Random();
            for (int i = 0; i < numThread; i++) {
                for (int j = 0; j < numMessages; j++) {
                    delays[i][j] = rand.nextInt(5000);
                }
            }

            for (int i = 0; i < numProcesses; i++) {
                SES_Algorithm process = new SES_Algorithm(i, numProcesses);
                for (int j = 0; j < numThread; j++) {
                    int destID = destIDs[i][j];
                    MyProcess p = new MyProcess(process, new int[] { destID }, messages, delays);
                    myThreads[i][j] = new Thread(p);
                }
            }

            for (int i = 0; i < numProcesses; i++) {
                for (int j = 0; j < numThread; j++) {
                    myThreads[i][j].start();
                }
            }


        } catch (Exception e) {
            System.err.println("Could not create registry exception: " + e.toString());
            e.printStackTrace();
        }

    }

}

class MyProcess implements Runnable {
    int[] destIDs;
    String[][] messages;
    SES_Algorithm process;
    int[][] delays;

    public MyProcess(SES_Algorithm process, int[] destIDs, String[][] messages, int[][] delays) {
        this.messages = messages;
        this.destIDs = destIDs;
        this.process = process;
        this.delays = delays;
    }

    public void run() {
        for (int i = 0; i < destIDs.length; i++) {
            try {
                for(int j = 0; j < messages[i].length; j ++ ){
                    process.send(destIDs[i], "SchiperEggliSandoz", messages[i][j], delays[i][j]);
                }

            } catch (Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }

        }
    }

}
