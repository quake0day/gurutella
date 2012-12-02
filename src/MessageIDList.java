import java.util.ArrayList;

/**
 * Version 1: Stores all used global MessageID number. ($3.2 in project assignment)
 */

/**
 * @author Tianmiao
 *
 */
public class MessageIDList {
	private ArrayList <IDRecorder> _IDList;
	private ArrayList <byte[]> _InnerList;	//declared for convenience
	
	public MessageIDList()
	{
		_IDList = new ArrayList <IDRecorder> (); 
		_InnerList = new ArrayList <byte[]> ();
	}
	
	public void addRecord(IDRecorder rec)	//add single IDRecord to this List
	{
		if (rec.isValid())
		{
			if(_IDList.size() <= 160){
				_IDList.add(rec); 
				_InnerList.add(rec.getIDByte());
			}
			else{
				_IDList.remove(0);
				_InnerList.remove(0);
				_IDList.add(rec); 
				_InnerList.add(rec.getIDByte());
			}
		}
	}
	public IDRecorder getRecord(byte[] id){
		int i= -1;
		if (_InnerList.contains(id)){
			i = _InnerList.indexOf(id);
		}
		else
		{
			System.out.println("You're trying to get a Record which not exist!");
		}
		return _IDList.get(i);
	}
	public IDRecorder getRecord(int i)
	{
		return _IDList.get(i);
	}
	
	public int getSize()
	{
		return _IDList.size();
	}
	
	public boolean checkID(int[] id)	//bad=true, ok=false
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
	
	public boolean checkID(byte[] id)	//Check if contains
	{
		if (_InnerList.contains(id))
			return true;
		else 
			return false;
	}
}
