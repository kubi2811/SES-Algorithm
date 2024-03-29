import java.util.Arrays;


public class Clock {

	public static boolean lessThanEqualTo(int[] timeStamp1, int[] timeStamp2)
	{	
		
		for (int i = 0; i < timeStamp1.length; i++)
		{
			if (!(timeStamp1[i] <= timeStamp2[i]))
			{
				return false;
			}
		}
		return true;
	}
	
	public static int[] max(int[] timeStamp1, int[] timeStamp2)
	{
		int[] max_array = timeStamp1;
		for (int i = 0; i < timeStamp1.length; i++)
		{
			if (timeStamp1[i] < timeStamp2[i])
				max_array[i] = timeStamp2[i];
		}
		return max_array;
	}
	
	public static String toString(int[] vectorClock)
	{
		return Arrays.toString(vectorClock);
		
	}
}
