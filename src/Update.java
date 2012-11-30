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
	private int TTL=7;
	private int Hops=0;
	PrintWriter outServer = null;
	DataOutputStream outToServer = null;
	
	public Update(ClientInfoList client){
		this.clients = client;
		this.forbiddenSocket = null;
	}
	
	public Update(ClientInfoList client, Socket forbiddenSocket,int TTL, int Hops){
		this.clients = client;
		this.forbiddenSocket = forbiddenSocket;
		this.TTL = TTL;
		this.Hops = Hops;
	}
	
	public void run(){
		byte[] ping = null;
		Iterator<Socket> iter =clients.iterator();
		//byte[] id, byte type, byte ttl, byte hops, byte[] plength, byte[] payload
		byte[] mID = new byte[16];
		byte[] newPacketLength={0x00,0x00,0x00,0x00};
		byte ttl;
		byte hops;
		byte[] payload = null;
		mID[8] = (byte)0xff;
		mID[15] = (byte)0x00;
		byte mtype = (byte)0x00; // ping message
		ttl = new Integer(TTL).byteValue();
		hops = new Integer(Hops).byteValue();
		
	
		MessageContainer pingContainer = new MessageContainer(mID,mtype,ttl,hops,newPacketLength,payload);
		ping = pingContainer.convertToByte();
		while(iter.hasNext()){
			Socket clientSocket = iter.next();
			if(!clientSocket.equals(forbiddenSocket)){
				try {
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
