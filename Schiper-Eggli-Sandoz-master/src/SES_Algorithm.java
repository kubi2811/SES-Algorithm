import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SES_Algorithm extends UnicastRemoteObject implements SES_RMI {

    private List<Message> messageBuffer;
    private List<BufferModel> bufferModelBuffer;
    private int[] timeStamp;
    public int pid;

    public SES_Algorithm(int pid, int numProcesses) throws RemoteException {
        super();
        messageBuffer = new LinkedList<Message>();
        bufferModelBuffer = new ArrayList<BufferModel>();
        timeStamp = new int[numProcesses];
        this.pid = pid;

        try{
        	// Binding the remote object (stub) in the local registry
        	Registry registry = LocateRegistry.getRegistry();

        	registry.rebind("SchiperEggliSandoz-" + pid, this);
        	System.err.println("Process " + pid + " ready");
        } catch (Exception e) {
        	System.err.println("Server exception: " + e.toString());
        	e.printStackTrace();
     }
    }

    /**
     * Receives a messages. If deliver requirement is met, message is delivered.
     * Otherwise, it is added to the buffer.
     */
    public synchronized void receive(Message m)
    {
    	if (Buffer.deliveryCondition(m.getsBuffer(), new BufferModel(pid, timeStamp))) {
    	    deliver(m);
    	    checkBuffer();
    	} else {
    		println("Current Vector Clock: " + Clock.toString(this.timeStamp));
    		println("Buffering message: " + m.toString());
    	    messageBuffer.add(m);
    	}
    }

    public synchronized void send(int destinationID, String destination, String message) throws MalformedURLException, RemoteException, NotBoundException {

        SES_RMI dest = (SES_RMI) Naming.lookup(destination + "-" + destinationID);

        timeStamp[pid]++;
        List<BufferModel> copy = new ArrayList<BufferModel>();
        for(BufferModel element : bufferModelBuffer) {
            copy.add(element.clone());
        }
        
        Message messageObject = new Message(message, copy, Arrays.copyOf(timeStamp, timeStamp.length));

        println("Sending - " + messageObject.toString());
        int wait = (int) (Math.random()*10000);

        new java.util.Timer().schedule( 
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        try {
                            dest.receive(messageObject);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                },
                wait
        );
        
        Buffer.insert(bufferModelBuffer, new BufferModel(destinationID, Arrays.copyOf(timeStamp, timeStamp.length)));
    }
    
    public synchronized void send(int destinationID, String destination, String message, int delay) throws MalformedURLException, RemoteException, NotBoundException {

        SES_RMI dest = (SES_RMI) Naming.lookup(destination + "-" + destinationID);

        // New operation
        timeStamp[pid]++;
        List<BufferModel> copy = new ArrayList<BufferModel>();
        for(BufferModel element : bufferModelBuffer) {
            copy.add(element.clone());
        }
        
        Message messageObject = new Message(message, copy, Arrays.copyOf(timeStamp, timeStamp.length));

        println("Sending - " + messageObject.toString());

        new java.util.Timer().schedule( 
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        try {
                            dest.receive(messageObject);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                },
                delay
        );
               
        Buffer.insert(bufferModelBuffer, new BufferModel(destinationID, Arrays.copyOf(timeStamp, timeStamp.length)));
    }

    /**
     * Checks the buffer for messages that can be delivered.
     */
    private synchronized void checkBuffer() {
        for(int i = 0; i < messageBuffer.size(); i++) {
            Message m = messageBuffer.get(i);
            if (Buffer.deliveryCondition(m.getsBuffer(), new BufferModel(pid, timeStamp))) {
                deliver(m);
                messageBuffer.remove(i);
                checkBuffer();
                break;
            }
        }
    }

    private synchronized void deliver(Message m) {
    	// Update knowledge of what should have occurred
        bufferModelBuffer = Buffer.merge(bufferModelBuffer, m.getsBuffer());
        println("Delivering - " + m.toString());
        // Merge Vector Clocks
        this.timeStamp = Clock.max(this.timeStamp, m.getTimeStamp());

        // Increment clock for current process
        this.timeStamp[pid]++;
    }

    private void println(String message)
    {
    	String pidStr = "(" + this.pid + ") ";
    	System.err.println(pidStr + message);
    }

}
