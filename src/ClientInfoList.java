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
	private ArrayList<Socket> _clients_outgoing;
	private ArrayList<Socket> _clients_incoming;
	private ArrayList<Socket> _clients_all;


	public ClientInfoList(){
		_clients_outgoing = new ArrayList <Socket>();
		_clients_incoming = new ArrayList <Socket>();

	}
	/**
	 * @param clientSoc
	 */
	public synchronized void add_incoming(Socket clientSoc) {
		// TODO Auto-generated method stub
		_clients_incoming.add(clientSoc);
	}
	public synchronized void add_outgoing(Socket clientSoc) {
		// TODO Auto-generated method stub
		_clients_outgoing.add(clientSoc);
	}
	/**
	 * @return
	 */
	public synchronized int size(int i) {
		// TODO Auto-generated method stub
		if (i == 0){
			return _clients_outgoing.size();
		}
		else{
			return _clients_incoming.size();
		}
	}

	/**
	 * @param index
	 * @return
	 */
	public synchronized Socket get(int i, int index) {
		// TODO Auto-generated method stub
		if(i == 0){
			return _clients_outgoing.get(index);
		}
		else{
			return _clients_incoming.get(index);
		}
	}

	/**
	 * @return
	 */
	public synchronized Iterator<Socket> iterator() {
		// Combine two array list
		_clients_all = new ArrayList <Socket>();
		_clients_all.addAll(_clients_outgoing);
		_clients_all.addAll(_clients_incoming);
		return _clients_all.iterator();
	}
}
