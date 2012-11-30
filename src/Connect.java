

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Connect extends Thread{
	
	private String targetIPAddress;
	private int tcpport;
	private Socket newEstablishedSocket = null;
	private ClientInfoList clients;
	private MessageIDList routingTable;
	private int tempClientIndex;
	public Connect (String targetIPAddressr, String tcp, ClientInfoList client,MessageIDList routingTable) throws IOException{
		targetIPAddress = targetIPAddressr;
		tcpport = Integer.parseInt(tcp);
		this.clients = client;
		this.routingTable = routingTable;
        //ExecutorService threadPool = Executors.newFixedThreadPool(MyConstants.MAX_THREAD_NUM);
        boolean isAbleToConnect = true;
        InetAddress addr = null;
        String localIPAddr = null;
        
        
        addr = InetAddress.getLocalHost();
        localIPAddr = addr.getHostAddress().toString();
        if(targetIPAddress.toString().equals(MyConstants.localIPNum) || targetIPAddress.toString().equals(MyConstants.localHost) || targetIPAddress.toString().equals(localIPAddr)){
        	isAbleToConnect = true;
        }
        // judge hostname
		try {
			InetAddress ip_isAbleToConnect = InetAddress.getByName(targetIPAddress);					
		} catch (UnknownHostException e1) {
			System.out.println("Enter valid host name");
			isAbleToConnect = false;
		}
		
		Iterator<Socket> iter = clients.iterator();
		//  duplicate?
		while(iter.hasNext()){
			if(targetIPAddress.equals(iter.next().getInetAddress().toString().split("/")[1])){
				isAbleToConnect = false;
			}
		}
        if(isAbleToConnect){
		try {
			//threadPool.submit(new Connect(new Socket(targetIPAddress,tcpport),clients,routingTable));
			newEstablishedSocket = new Socket(targetIPAddress,tcpport);
			System.out.println("The connection between this machine and "+targetIPAddress+" "+tcpport +" is successfully estabilshed");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("cannot connect to this Host. Connection refused");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("cannot connect to this Host. Connection refused");
		}
        }
        else{
			System.out.println("Cannot connect to itself and it may have duplicate connection.");
		}

	    
	}
	/*public Connect(Socket socket,ClientInfoList clients, MessageIDList routingTable) {
		// TODO Auto-generated constructor stub
		this.clients = clients;
		newEstablishedSocket = socket;
		tempClientIndex = clients.size(0) - 1;
		System.out.println("connect thread pool of thread");
	}*/
	
	public boolean checkMessagePacketValidation(byte[] data,int MessageLength){
		// check if length is larger than 22
		if(MessageLength < 22){
			return false;
		}
		return true;
		
	}

	public void run(){
        //BufferedReader in = null;
        PrintWriter outServer = null;
        String inputLine;
        boolean isAlive = true;
		try {
			//in = new BufferedReader(new InputStreamReader(newEstablishedSocket.getInputStream()));
			outServer = new PrintWriter(newEstablishedSocket.getOutputStream(), 
			        true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Do handshake
		outServer.println("SIMPELLA CONNECT/0.6\r\n");
		InputStream stream = null;
		try {
			stream = newEstablishedSocket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while(isAlive){
            byte[] data = new byte[4096];
         	int messageLength=0;
			try {
				messageLength = stream.read(data);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
         	if(messageLength == -1){ // means a broken socket
         		clients.remove(1, newEstablishedSocket);
         		isAlive = false;
         		break;
         	}
         	System.out.println("************");
         	System.out.println(messageLength);
         	
         	String recResult = new String(data);
         	
					if(recResult.trim().equals(MyConstants.STATUS_200_REC)){
						// Print out <string>
						System.out.println(recResult.split("200 ")[1]);
						clients.add_outgoing(newEstablishedSocket);	
						Thread update = new Update(clients);
						update.start();
						//System.out.println(clients.size(0));
					}
					else if(recResult.trim().equals(MyConstants.STATUS_503_REC)){
						// Print out <string>
						System.out.println(recResult.split("503 ")[1]);
					}
					 else{

				         	//System.out.println(res);
				            	// regular message, judge message type
				            	if(checkMessagePacketValidation(data,messageLength)){
					        		byte[] mID = new byte[16];
					        		System.arraycopy(data, 0, mID, 0, 16);
					            	byte messageType = (byte)data[16];
					            	byte TTL = (byte)data[17];
					            	byte Hops = (byte)data[18];
					            	if((int)(TTL+Hops) >=7 && (int)(TTL+Hops) <= 15){
					            	if(messageType == 0x00){
						            		
						            	}
					            	else{
					            		System.out.println("PONG");
					            	}
					            	}
				
				            	}
					 }
	
			
		}
		
	}
	
}
