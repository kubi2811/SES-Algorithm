import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class SchiperEggliSandoz_main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int numProcesses = 7;
        Thread[][] myThreads = new Thread[numProcesses][6];
        try {
            // Create Registry
            Registry registry = LocateRegistry.createRegistry(1099);


            int[][] destIDs = {{1,2,3,4,5,6},{0,2,3,4,5,6},{0,1,3,4,5,6},{0,1,2,4,5,6},{0,1,2,3,5,6},{0,1,2,3,4,6},{0,1,2,3,4,5}};
//            int[][] destIDs = {{1,2,3,4,5},{0,2,3,4,5},{0,1,3,4,5},{0,1,2,4,5},{0,1,2,3,5},{0,1,2,3,4}};
            String[][] messages = new String[6][150];
            for (int i = 0; i < messages.length; i++) {
                for (int j = 0; j < messages[i].length; j++) {
                    messages[i][j] = "message" + (j+1);
                }
            }
            int[][] delays = new int[6][150];
            Random rand = new Random();
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 150; j++) {
                    delays[i][j] = rand.nextInt(5000);
                }
            }

            for (int i = 0; i < numProcesses; i++) {
                SchiperEggliSandoz process = new SchiperEggliSandoz(i, numProcesses);
                for (int j = 0; j < 6; j++) {
                    int destID = destIDs[i][j];
                    MyProcess p = new MyProcess(process, new int[] { destID }, messages, delays);
                    myThreads[i][j] = new Thread(p);
                }
            }

            for (int i = 0; i < numProcesses; i++) {
                for (int j = 0; j < 6; j++) {
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
    SchiperEggliSandoz process;
    int[][] delays;

    public MyProcess(SchiperEggliSandoz process, int[] destIDs, String[][] messages, int[][] delays) {
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
