import java.net.InetAddress;
import java.net.Socket;

/**
 * 
 */

/**
 * @author Tianmiao
 *
 */
public class ConnectionInfo {
    private int _connectionPort;
    int _downloadPort;	//Reserved for the future use
    String _ipString;
    Socket _socket;
    int _packIn = 0;
	int _packOut = 0;
	int _bytesIn = 0;
	int _bytesOut = 0;
	int _queryNum = 0;
	int _queryHitNum = 0;
	String IP;
	String port;

    public ConnectionInfo(int connPort, Socket soc)
    {
        _connectionPort = connPort;
        _socket = soc;
    }	

    public synchronized boolean isSame(Socket soc)
    {
        if (_socket == soc)
            return true;
        else
            return false;
    }

    public  synchronized Socket getSocket()
    {
        return _socket;
    }

    public synchronized int getConnPort()
    {
        return _connectionPort;
    }

    public synchronized int getDownProt()
    {
        return _downloadPort;
    }

    public synchronized void setDownPort(int port)
    {
        _downloadPort = port;
    }

    public synchronized InetAddress getIP()
    {
        return _socket.getInetAddress();
    }
    
    public void addPI() {
    	_packIn++;
    }
    
	public int getPI()
	{
		return _packIn;
	}
	
    public void addPO() {
    	_packOut++;
    }
    
	public int getPO()
	{
		return _packOut;
	}

	public void addbI(int in)
	{
		_bytesIn += in;
	}
	
	public int getbI()
	{
		return _bytesIn;
	}
	
	public void addbO(int o)
	{
		_bytesOut += 0;
	}
	
	public int getbO()
	{
		return _bytesOut;
	}
	
	public void addQN()
	{
		_queryNum++;
	}
	
	public int getQN()
	{
		return _queryNum;
	}
	
	public void addQH()
	{
		_queryHitNum++;
	}
	
	public int getQH()
	{
		return _queryHitNum;
	}
}
