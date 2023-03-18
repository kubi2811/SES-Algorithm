package Test;

public class Demo {
    public static void main(String[] args) {
        int numProcesses = 7;

        IProcess[] processes = new IProcess[numProcesses];
        // create processes
        for (int i = 0; i < numProcesses; i++) {
            String ipAddress = "localhost";
            int port = 1234;
            IProcess process = new Process(i, ipAddress, port, numProcesses);
            processes[i] = process;
        }
        // start processes
        for (IProcess process : processes) {
            new Thread(process::start).start();
        }
    }
}
