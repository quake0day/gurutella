/**
 * 
 */

/**
 * @author Administrator
 *
 */
public class DownloadStorage {
	private QueryResult _qR;
	private byte[] _data = null;
	private boolean flag = false;
	private int _totalLength = 0;
	
	public DownloadStorage(QueryResult qr)
	{
		_qR = qr;
	}
	
	public void addData(byte[] data, int size)
	{
		byte[] cache = new byte[_totalLength + size];
		System.arraycopy(data, 0, cache, _totalLength, size);
		System.arraycopy(cache, _totalLength, _data, 0, _totalLength);
		_totalLength += size;
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
