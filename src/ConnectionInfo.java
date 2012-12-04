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
}
