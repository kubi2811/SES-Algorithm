import java.rmi.Remote;
import java.rmi.RemoteException;


public interface SES_RMI extends Remote{
	public void receive(Message m) throws RemoteException;
}
