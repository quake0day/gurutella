import java.util.ArrayList;

/**
 * 
 */

/**
 * @author Tianmiao
 *
 */
public class DownloadList {
	private ArrayList<DownloadStorage> _dS;
	
	public DownloadList()
	{
		_dS = new ArrayList<DownloadStorage> ();
	}
	
	public void add2DownList(DownloadStorage ds)
	{
		_dS.add(ds);
	}
	
	public void remove(QueryResult qR)
	{
		for (DownloadStorage s: _dS)
		{
			if (s.isQuery(qR))
				_dS.remove(_dS.indexOf(s));
		}
	}
	
	public DownloadStorage getDownStorage(QueryResult qR)
	{
		for (DownloadStorage s: _dS)
		{
			if (s.isQuery(qR))
				return s;
		}
		return null;
	}
	
	public ArrayList<DownloadStorage> findEndDown()
	{
		ArrayList<DownloadStorage> retnList = new ArrayList<DownloadStorage> ();
		for (DownloadStorage s: _dS)
		{
			if (s.isEnd())
				retnList.add(s);
		}
		if (retnList.isEmpty()) 
			return null;
		else
			return retnList;
	}
}
