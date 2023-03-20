import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Buffer {

	public static void insert(List<BufferModel> bufferModelBuffer, BufferModel newBufferModel)
	{
		boolean flag = false;
		
		// If pid is in list update its timestamp
		for (BufferModel bufferElement : bufferModelBuffer)
		{
			if(bufferElement.getPid() == newBufferModel.getPid())
			{
				bufferElement.setTimeStamp(newBufferModel.getTimeStamp());
				flag = true;
				break;
			}
		}
		
		// If pid not found in list add it to the list
		if (!flag)
		{
			bufferModelBuffer.add(newBufferModel);
		}
		
	}
	
	public static boolean deliveryCondition(List<BufferModel> bufferModelBufferMesg, BufferModel currProcess)
	{
		
		// There exists an (i,V) in Sm and V <= Vi
		for (BufferModel bufferElementMesg : bufferModelBufferMesg)
		{
			if (bufferElementMesg.getPid() == currProcess.getPid())
			{
				return Clock.lessThanEqualTo(bufferElementMesg.getTimeStamp(),
									currProcess.getTimeStamp());
			}
		}
		
		// The message contains no knowledge of what should have been received
		return true;
	}

	public static List<BufferModel> merge(List<BufferModel> ownBuffer, List<BufferModel> incomingBuffer) {
	    if(incomingBuffer.isEmpty()) {
	        return ownBuffer;
	    }
	    
	    List<BufferModel> resultBuffer = new ArrayList<BufferModel>();
	    
       for(int i = 0; i < incomingBuffer.size(); i++) {
           boolean found = false;
           BufferModel incomingBufferModel = incomingBuffer.get(i);
           
           for(int j = 0; j < ownBuffer.size(); j++) {
               BufferModel ownBufferModel = ownBuffer.get(j);
               if(incomingBufferModel.getPid() == ownBufferModel.getPid()) {
                   found = true;
                   int[] maxTimeStamp = Clock.max(incomingBufferModel.getTimeStamp(), ownBufferModel.getTimeStamp());
                   resultBuffer.add(new BufferModel(ownBufferModel.getPid(), maxTimeStamp));
               }
           }
           
           if(!found) {
               resultBuffer.add(incomingBuffer.get(i));
           }
       }
       
       return resultBuffer;
	}
	
	public static String toString(List<BufferModel> bufferModelBuffer)
	{
		return Arrays.toString(bufferModelBuffer.toArray());
	}
}
