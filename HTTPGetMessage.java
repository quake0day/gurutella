/**
 * 
 */

/**
 * @author Tianmiao
 *
 */
public class HTTPGetMessage {
	private String _get = "GET /get/";
			String _fileIndex;
			String _fileName;
			String _version = "HTTP/1.1\r\n";
			
			String _userAgt = "User-Agent: Simpella\r\n";
			
			String _host = "Host: ";
			String _address;
			
			String _connection = "Connection: Keep-Alive\r\n";
			String _range = "Range: bytes=0-\r\n" +
					"\r\n";
			String[] _messagePieces = null;
			
	public HTTPGetMessage(int index, String name, String add)	//Client
	{
		_fileIndex = String.valueOf(index);
		_fileName = name;
		_address = add;
	}
	
	public HTTPGetMessage(byte[] b)	//Server
	{
		_messagePieces = new String(b).split("\\r\\n");
	}
	
	public boolean isGetMessage()	//Server
	{
		if (_messagePieces[0].split(" ")[0] == "Get")
			return true;
		else 
			return false;
	}
	
	public int getRequestNum()	//Server
	{
		return Integer.parseInt(_messagePieces[0].split("/")[2]);
	}
	
	public String getRequestName()	//Server
	{
		return _messagePieces[0].split("/")[3].split(" ")[0];
	}
	
	public String getIPString()	//Server
	{
		return _messagePieces[2].split(":")[1].trim();
	}
	
	public int getPortNum()	//Server
	{
		return Integer.parseInt(_messagePieces[2].split(":")[2]);
	}
	
	public byte[] getMessage()	//Client
	{
		String m = _get + _fileIndex + "/" + _fileName + " " + _version
				+ _userAgt + _host + _address + "\r\n" + _connection
				+ _range;
		return m.getBytes();
	}
}
