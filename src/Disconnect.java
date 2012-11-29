

import java.io.IOException;
import java.net.Socket;

public class Disconnect extends Thread{
	private int connectionID;
	private int type;
	private ClientInfoList clients;
	public Disconnect(int connid, int type, ClientInfoList client){
		this.connectionID = connid;
		this.type = type;
		this.clients = client;
		start();
	}
	public void run(){
		synchronized(this){
		Socket soc = null;
		
		soc = clients.get(type,connectionID);
		clients.remove(type,connectionID);
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
