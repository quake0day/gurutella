import java.util.ArrayList;

/**
 * TO BE FINISHED:
 * This List should store all used global MessageID number. 
 */

/**
 * @author Tianmiao
 *
 */
public class MessageIDList {
	private ArrayList <int[]> _IDList;
	
	public MessageIDList()
	{
		ArrayList<int[]> _IDList = new ArrayList<int[]>(); 
	}
	
	public void addID(int[] id)
	{
		_IDList.add(id); 
	}
	
	public boolean checkID()
	{
		return false;
	}
}
