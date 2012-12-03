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

    public boolean isSame(Socket soc)
    {
        if (_socket == soc)
            return true;
        else
            return false;
    }

    public Socket getSocket()
    {
        return _socket;
    }

    public int getConnPort()
    {
        return _connectionPort;
    }

    public int getDownProt()
    {
        return _downloadPort;
    }

    public void setDownPort(int port)
    {
        _downloadPort = port;
    }

    public InetAddress getIP()
    {
        return _socket.getInetAddress();
    }
}
