/**
 * 
 */

/**
 * @author Administrator
 *
 */
public class DownloadStorage {
	private QueryResult _qR;
	private byte[] _data;
	private boolean flag = false;
	
	public DownloadStorage(QueryResult qr)
	{
		_qR = qr;
	}
	
	public void addData(byte[] data, int size)
	{
		System.arraycopy(data, 0, _data, _data.length, size);
	}
	
	public int getDownloadSize()
	{
		return _data.length;
	}
	
	public byte[] getByte()
	{
		return _data;
	}
	
	public boolean isQuery(QueryResult qr)
	{
		if(_qR == qr)
			return true;
		else
			return false;
	}
	
	public QueryResult getQuery()
	{
		return _qR;
	}
	
	public void setEnd(){
		flag = true;
	}
	
	public boolean isEnd()
	{
		return flag;
	}
}
