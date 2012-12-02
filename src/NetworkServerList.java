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
public class NetworkServerList {

	/**
	 * 
	 */
	private ArrayList<ServerInfo> _networkServerList;
	public NetworkServerList() {
		// TODO Auto-generated constructor stub
		_networkServerList = new ArrayList<ServerInfo>();
	}
	public synchronized void addServer(ServerInfo ser){
		//System.out.println(ser.getIP().getHostAddress());
		//System.out.println(ser.getPort());
		_networkServerList.add(ser);
	}

	public synchronized Iterator<ServerInfo> iterator() {
		return _networkServerList.iterator();
	}

	public synchronized void clearAll(){
		_networkServerList.clear();
	}
	
}
