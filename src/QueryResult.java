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
	public QueryResult(int index,InetAddress _IP,int _downloadPort,int _fileSize,String _fileName) {
		// TODO Auto-generated constructor stub
		this._IP = _IP;
		this._downloadPort = _downloadPort;
		this._fileSize = _fileSize;
		this._fileName = _fileName;
		this.index = index;
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
