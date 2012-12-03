import java.net.InetAddress;

/**
 * 
 */

/**
 * @author quake0day
 *
 */
public class QueryResult {

	/**
	 * 
	 */
	private
	int index;
	InetAddress _IP;
	int _downloadPort;
	int _fileSize;
	String _fileName;
	String queryString;
	int ID;
	public QueryResult(int index,InetAddress _IP,int _downloadPort,int _fileSize,String _fileName,String queryString) {
		// TODO Auto-generated constructor stub
		this._IP = _IP;
		this._downloadPort = _downloadPort;
		this._fileSize = _fileSize;
		this._fileName = _fileName;
		this.index = index;
		this.queryString = queryString;
		String ID0 = _IP.getHostAddress();
		String ID1 = ""+_downloadPort;
		String ID2 = ""+_fileSize;
		String ID3 = ""+_fileName;
		this.ID = (ID0+ID1+ID2+ID3).hashCode();
	}
	public int getID(){
		return this.ID;
	}
	public InetAddress getIP(){
		return _IP;
	}
	
	public int getDownloadPort(){
		return _downloadPort;
	}
	public int getFileSize(){
		return _fileSize;
	}
	public String getFileName(){
		return _fileName;
	}
	public int getFileIndex(){
		return index;
	}
}
