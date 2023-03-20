import java.io.Serializable;
import java.util.Arrays;

public class BufferModel implements Serializable{

	int pid;
	int[] timeStamp;
	
	public BufferModel(int pid, int[] timeStamp)
	{
		this.pid = pid;
		this.timeStamp = timeStamp;
	}
	
	public BufferModel clone()
	{
	    BufferModel newBufferModel = new BufferModel(this.pid, Arrays.copyOf(this.timeStamp, this.timeStamp.length));
	    
	    return newBufferModel;
	}
	
	public int[] getTimeStamp()
	{
		return timeStamp;
	}
	
	public int getPid()
	{
		return pid;
	}
	
	public void setPid(int pid)
	{
		this.pid = pid;
	}
	
	public void setTimeStamp(int[] timeStamp)
	{
		this.timeStamp = timeStamp;
	}
	
	public String toString()
	{
		return "(" + pid + ", " + Clock.toString(timeStamp) + ")";
	}
}
