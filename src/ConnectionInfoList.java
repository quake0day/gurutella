import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 */

/**
 * @author quake0day
 *
 */
public class ConnectionInfoList {

    /**
     * @param args
     */
    //private ArrayList<Socket> _clients_outgoing;
    //private ArrayList<Socket> _clients_incoming;
    private ArrayList<ConnectionInfo> _connections;	//_clients_all;

    /*
       public ConnectionInfoList(){
    // for type 0 : out going
    _clients_outgoing = new ArrayList <Socket>();
    // for type 1 : incoming
    _clients_incoming = new ArrayList <Socket>();

       }


       public synchronized void add_incoming(Socket clientSoc) {
    // TODO Auto-generated method stub
    _clients_incoming.add(clientSoc);
       }
       public synchronized void add_outgoing(Socket clientSoc) {
    // TODO Auto-generated method stub
    _clients_outgoing.add(clientSoc);
       }

       public synchronized int size(int type) {
    // TODO Auto-generated method stub
    if (type == 0){
    return _clients_outgoing.size();
    }
    else{
    return _clients_incoming.size();
    }
       }



       public synchronized Socket get(int type, int index) {
    // TODO Auto-generated method stub
    if(type == 0){
    return _clients_outgoing.get(index);
    }
    else{
    return _clients_incoming.get(index);
    }
       }



       public synchronized Iterator<Socket> iterator() {
    // Combine two array list
    _clients_all = new ArrayList <Socket>();
    _clients_all.addAll(_clients_outgoing);
    _clients_all.addAll(_clients_incoming);
    return _clients_all.iterator();
       }


       public void remove(int type, int connectionID) {

       if(type == 0){
       _clients_outgoing.remove(connectionID);
       }
       else{
       _clients_incoming.remove(connectionID);
       }
       }
       public void remove(int type, Socket vicSocket){
       int vic_index;
       if(type == 0){
       vic_index = _clients_outgoing.indexOf(vicSocket);
       _clients_outgoing.remove(vic_index);
       }
       else{
       vic_index = _clients_incoming.indexOf(vicSocket);
       _clients_incoming.remove(vic_index);
       }
       }
}*/

public ConnectionInfoList(){
    _connections = new ArrayList<ConnectionInfo> ();
}

public synchronized void addConnection(ConnectionInfo c) {
    // TODO Auto-generated method stub
    _connections.add(c);
}

public synchronized int size() {
    // TODO Auto-generated method stub
    return _connections.size();
}

public synchronized ConnectionInfo get(int index) {
    // TODO Auto-generated method stub
    return _connections.get(index);
}	

public synchronized Iterator<ConnectionInfo> iterator() {
    // Combine two array list
    return _connections.iterator();
}	

public synchronized void remove(int connectionID) {
    _connections.remove(connectionID);
}

public synchronized void remove(int type, Socket vicSocket){
    int vic_index;
    vic_index = _connections.indexOf(vicSocket);
    _connections.remove(vic_index);
}

public synchronized Socket getSocket(String ip)
{
	for (int i = 0; i < _connections.size(); i++)
	{
		if (_connections.get(i).getIP().toString().split("/")[1].matches(ip))
			return _connections.get(i).getSocket();
	}
	return null;
}

}
