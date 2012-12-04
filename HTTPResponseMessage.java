/**
 * 
 */

/**
 * @author Administrator
 *
 */
public class HTTPResponseMessage {
	private String _OK = "HTTP/1.1 200 OK\r\n";
			String _server= "Server: Simpella0.6\r\n";
			String _cType = "Content-type: application//binary\r\n";
			String _cLength = "Content-length: ";
			String _size;
			String[] _messagePieces;

	public HTTPResponseMessage(int fileSize)	//Server
	{
		_size = String.valueOf(fileSize);
	}
	
	public HTTPResponseMessage(byte[] b)	//Client
	{
		_messagePieces = new String(b).split("\\r\\n");
	}
	
	public byte[] getMessage()	//Server
	{
		String m =  _OK + _server + _cType + _cLength + _size + "\r\n\r\n";
		return	m.getBytes();
	}
	
	public boolean isResponse()	//Client
	{
		if (_messagePieces[0].split(" ")[1].equals("200"))
			return true;
		else
			return false;
	}
	
	public int getSize()	//Client
	{
		return Integer.parseInt(_messagePieces[3].split(":")[1].trim());
	}
}
