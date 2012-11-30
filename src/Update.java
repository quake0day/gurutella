import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;

/**
 * 
 */

/**
 * @author quake0day
 *
 */
public class Update extends Thread {

	/**
	 * @param args
	 */
	private ClientInfoList clients;
	private Socket forbiddenSocket = null;
	PrintWriter outServer = null;
	DataOutputStream outToServer = null;
	
	public Update(ClientInfoList client){
		this.clients = client;
		this.forbiddenSocket = null;
	}
	
	public Update(ClientInfoList client, Socket forbiddenSocket){
		this.clients = client;
		this.forbiddenSocket = forbiddenSocket;
	}
	
	public void run(){
		byte[] ping = null;
		Iterator<Socket> iter =clients.iterator();
		MessageContainer pingContainer = new MessageContainer();
		ping = pingContainer.convertToByte();
		while(iter.hasNext()){
			Socket clientSocket = iter.next();
			if(forbiddenSocket == null || !clientSocket.equals(forbiddenSocket)){
				try {
					outServer = new PrintWriter(clientSocket.getOutputStream(),true);
					outToServer = new DataOutputStream(clientSocket.getOutputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					// This could happen when the other side's socket is closed
					System.out.println("Error when sending ping in iterator");
				}
				
				// send Ping
				try {
					outToServer.write(ping);
					//outToServer.writeChars("\r\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//outServer.close();
			}
		}
	}

}
