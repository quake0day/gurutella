import java.net.InetAddress;

/**
 * 
 */

/**
 * @author quake0day
 *
 */
public class ServerInfo {

	/**
	 * 
	 */
	private int port;
	private InetAddress IP;
	private int fileNum;
	private int fileSize;
	
	public ServerInfo(int port, InetAddress IP, int fileNum,
			int fileSize) {
		// TODO Auto-generated constructor stub
		this.port = port;
		this.IP = IP;
		this.fileNum = fileNum;
		this.fileSize = fileSize;
	}
	
	public int getPort(){
		return this.port;
	}
	public InetAddress getIP(){
		return this.IP;
	}
	public int getFileNum(){
		return this.fileNum;
	}
	public double getFileSize(){
		return this.fileSize;
	}

}
