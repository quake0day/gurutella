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
public class ClientInfoList {

	/**
	 * @param args
	 */
	private ArrayList<Socket> _clients;

	public ClientInfoList(){
		 _clients = new ArrayList <Socket>();
	}
	/**
	 * @param clientSoc
	 */
	public synchronized void add(Socket clientSoc) {
		// TODO Auto-generated method stub
		_clients.add(clientSoc);
	}

	/**
	 * @return
	 */
	public synchronized int size() {
		// TODO Auto-generated method stub
		return _clients.size();
	}

	/**
	 * @param index
	 * @return
	 */
	public synchronized Socket get(int index) {
		// TODO Auto-generated method stub
		return _clients.get(index);
	}

	/**
	 * @return
	 */
	public synchronized Iterator<Socket> iterator() {
		// TODO Auto-generated method stub
		return _clients.iterator();
	}

}
