import java.net.Socket;
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
	private ArrayList <Socket> _socketList;
	public MessageIDList()
	{
		_IDList = new ArrayList<byte[]>(); 
		_socketList = new ArrayList<Socket>();
	}
	
	public void addID(byte[] id,Socket targetsoc)
	{
		if(_IDList.size() <= 160){
			_IDList.add(id); 
			_socketList.add(targetsoc);
		}
		else{
			_IDList.remove(0);
			_IDList.add(id); 
			_socketList.remove(0);
			_socketList.add(targetsoc);
		}
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
