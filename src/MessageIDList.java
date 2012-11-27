import java.util.ArrayList;

/**
 * Version 1: Stores all used global MessageID number. ($3.2 in project assignment)
 */

/**
 * @author Tianmiao
 *
 */
public class MessageIDList {
	private ArrayList <byte[]> _IDList;
	
	public MessageIDList()
	{
		_IDList = new ArrayList<byte[]>(); 
	}
	
	public void addID(byte[] id)
	{
		_IDList.add(id); 
	}
	
	public boolean checkID(int[] id)	//ok=true, bad=false
	{
		byte[] idNo = new byte[id.length];
		for(int i = 0; i < id.length; i++)	
		{
			if (id[i] > 255 || id[i] < 0)	//Number range legality check
			{
				return false;
			}
			else
			{
				idNo[i] = new Integer(id[i]).byteValue();	
			}
		}
		return this.checkID(idNo);
	}
	
	public boolean checkID(byte[] id)
	{
		if (_IDList.contains(id))
		{
			return true;
		}
		else return false;
	}
}
