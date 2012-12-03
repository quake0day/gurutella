

import java.io.IOException;
import java.net.Socket;

public class Disconnect extends Thread{
	private int connectionID;
	private ConnectionInfoList clients;
	public Disconnect(int connid, int type, ConnectionInfoList client){
		this.connectionID = connid;
		this.clients = client;
		start();
	}
	public void run(){
		synchronized(this){
		Socket soc = null;
		
		soc = clients.get(connectionID).getSocket();
		clients.remove(connectionID);
		
		try {		
			soc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("cannot close this id...");
		}
		}
		System.out.println("The connection (ID:"+connectionID+") is successfully disconnect :)");
	}

}
