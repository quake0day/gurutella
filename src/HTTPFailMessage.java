/**
 * 
 */

/**
 * @author Administrator
 *
 */
public class HTTPFailMessage {
	private String _fail = "HTTP/1.1 503 File not found.\r\n\r\n";
			String _receive;
			
	public HTTPFailMessage()	//Server
	{
	}
	
	public HTTPFailMessage(byte[] b)	//Client
	{
		_receive = new String(b).split("\\r\\n\\r\\n")[0];
	}
	
	public byte[] getMessage()	//Server
	{
		return _fail.getBytes();
	}
	
	public boolean isFailMessage()	//Client
	{
		if (_receive.split(" ")[1].equals("503"))
			return true;
		else
			return false;
	}
}
