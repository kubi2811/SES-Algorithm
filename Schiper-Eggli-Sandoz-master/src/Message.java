import java.io.Serializable;
import java.util.List;

public class Message implements Serializable{

    private String message;
    private List<BufferModel> bufferModelBuffer;
    private int[] timeStamp;
    
    public Message(String message, List<BufferModel> bufferModelBuffer, int[] timeStamp){
        this.message = message;
        this.bufferModelBuffer = bufferModelBuffer;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BufferModel> getsBuffer() {
        return bufferModelBuffer;
    }

    public void setsBuffer(List<BufferModel> bufferModelBuffer) {
        this.bufferModelBuffer = bufferModelBuffer;
    }

    public int[] getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int[] timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    public String toString()
    {
    	return "Message: " +  message + "\tsBuffer: " + Buffer.toString(bufferModelBuffer) + "\tTimeStamp: " + Clock.toString(timeStamp);
    }
    
    
}
