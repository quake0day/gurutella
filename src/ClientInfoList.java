import java.net.ServerSocket;
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
	public static ArrayList<Socket> _clients;
	public ClientInfoList(){
		_clients = new ArrayList<Socket>();
	}
	/**
	 * @param serverSocket
	 */

	/**
	 * @return
	 */
	public Iterator<Socket> iterator() {
		// TODO Auto-generated method stub
		
		return _clients.iterator();
	}
	/**
	 * @return
	 */
	public int size() {
		// TODO Auto-generated method stub
		return _clients.size();
	}
	/**
	 * @param index
	 * @return
	 */
	public Socket get(int index) {
		// TODO Auto-generated method stub
		return _clients.get(index);
	}
	/**
	 * @param socket
	 */
	public void addSocket(Socket socket) {
		// TODO Auto-generated method stub
		_clients.add(socket);
	}

	/**
	 * @param clientSoc
	 */
	public void add(Socket clientSoc) {
		// TODO Auto-generated method stub
		_clients.add(clientSoc);
		
	}

}
