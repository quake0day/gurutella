import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

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
		//_data = new byte[];
	}
	
	public void addData(byte[] data, int size) // small file
	{

		byte[] cache = _data;
		_data = new byte[_totalLength + size];
		System.arraycopy(data, 0, _data, _totalLength, size);

		if (_totalLength != 0)
		{
			System.arraycopy(cache, 0, _data, 0, _totalLength);
		}
		_totalLength += size;
	}
	
	public void addData(byte[] data, int size, String hashString, FileInfoList _fileList) throws 
IOException
	{
		/*
		FileOutputStream fin = new FileOutputStream(
				_fileList.getAbsolutePath() + "//" 
						+ hashString);
						*/
		
		RandomAccessFile randomFile = new RandomAccessFile(_fileList.getAbsolutePath() + "//" +hashString, "rw");  
		long fileLength = randomFile.length();  
		randomFile.seek(fileLength); 
		randomFile.write(data);
		randomFile.close();  
		_data = new byte[_totalLength + size];
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
