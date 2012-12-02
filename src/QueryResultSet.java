/**
 * 
 */

/**
 * @author quake0day
 *
 */
public class QueryResultSet {

	/**
	 * 
	 */
	private int _fileIndex;
	private int _fileSize;
	private String _fileName;
	public QueryResultSet(int fileIndex, int fileSize, String fileName) {
		// TODO Auto-generated constructor stub
		this._fileIndex = fileIndex;
		this._fileSize = fileSize;
		this._fileName = fileName;
	}
	
	public int getFileIndex(){
		return _fileIndex;
	}
	public int getFileSize(){
		return _fileSize;
	}
	public String getFileName(){
		return _fileName;
	}


}
