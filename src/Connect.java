

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Iterator;

public class Connect extends Thread{
	
	private String targetIPAddress;
	private int tcpport;
	private Socket newEstablishedSocket = null;
	private ConnectionInfoList clients;
	private MessageIDList routingTable;
	private NetworkServerList nsl;
	
	public Connect (String targetIPAddressr, String tcp, ConnectionInfoList client,MessageIDList routingTable,NetworkServerList nsl) throws IOException{
		targetIPAddress = targetIPAddressr;
		tcpport = Integer.parseInt(tcp);
		this.clients = client;
		this.routingTable = routingTable;
		this.nsl = nsl;
        //ExecutorService threadPool = Executors.newFixedThreadPool(MyConstants.MAX_THREAD_NUM);
        boolean isAbleToConnect = true;
        InetAddress addr = null;
        String localIPAddr = null;
        
        
        addr = InetAddress.getLocalHost();
        localIPAddr = addr.getHostAddress().toString();
        if(targetIPAddress.toString().equals(MyConstants.localIPNum) 
        		|| targetIPAddress.toString().equals(MyConstants.localHost) 
        		|| targetIPAddress.toString().equals(localIPAddr)) {
        	isAbleToConnect = true;
        }
        // judge hostname
		try {
			InetAddress ip_isAbleToConnect = InetAddress.getByName(targetIPAddress);					
		} catch (UnknownHostException e1) {
			System.out.println("Enter valid host name");
			isAbleToConnect = false;
		}
		
		Iterator<ConnectionInfo> iter = clients.iterator();
		//  duplicate?
		while(iter.hasNext()){
			if(targetIPAddress.equals(iter.next().getIP().toString().split("/")[1])){
				isAbleToConnect = false;
			}
		}
        if(isAbleToConnect){
		try {
			//threadPool.submit(new Connect(new Socket(targetIPAddress,tcpport),clients,routingTable));
			newEstablishedSocket = new Socket(targetIPAddress,tcpport);
			System.out.println("Trying to connect "+targetIPAddress+": "+tcpport +" ...");
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
	/*public Connect(Socket socket,ConnectionInfoList clients, MessageIDList routingTable) {
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
	
	public int byte2int(byte[] i){
		ByteBuffer bc = ByteBuffer.wrap(i);
		return bc.getInt();
	}

	public void run(){
        //BufferedReader in = null;
        PrintWriter outServer = null;
        boolean isAlive = true;
		try {
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
            byte[] data = new byte[MyConstants.MAX_PAYLOAD_LENGTH];
         	int messageLength=0;
			try {
				messageLength = stream.read(data);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
         	if (messageLength == -1){ // means a broken socket
         		clients.remove(0, newEstablishedSocket);
         		isAlive = false;
         		break;
         	}

         	System.out.println("client received: " + messageLength);
         	
         	String recResult = new String(data);
         	
			if (recResult.trim().equals(MyConstants.STATUS_200_REC)){
				// Print out <string>
				System.out.println(recResult.split("200 ")[1]);
	
				clients.addConnection(new ConnectionInfo(tcpport, newEstablishedSocket));
				System.out.println("Connection established!");
				Thread update = new Update(clients, routingTable);
				update.start();
				//System.out.println(clients.size(0));
			}
			else if (recResult.trim().equals(MyConstants.STATUS_503_REC)){
				// Print out <string>
				System.out.println(recResult.split("503 ")[1]);
				isAlive = false;
				break;
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
		            	
		            	if((int)(TTL+Hops) >=0 && (int)(TTL+Hops) <= 15) {
		            	if(messageType == (byte)0x00){
	            		System.out.println("toclient PING");
			            	}
		            	else if(messageType == (byte)0x01){
		            		System.out.println("toclient PONG");
		            		if(routingTable.checkID(mID) == false) { 
		            			// I'm the one who send ping initially
			            		byte[] payload = new byte[14];
			            		System.arraycopy(data,23,payload,0,14);
			            		System.out.println("copy payload");
			            		byte[] port = new byte[4];
			            		byte[] IPaddr = new byte[4];
			            		byte[] fileNum = new byte[4];
			            		byte[] fileSize = new byte[4];
			            		System.arraycopy(payload,0,port,2, 2);
			            		System.arraycopy(payload,2,IPaddr,0,4);
			            		System.arraycopy(payload,6,fileNum,0,4);
			            		System.arraycopy(payload,10,fileSize,0,4);
			            		/*public ServerInfo(int port, InetAddress IP, int fileNum,
			            				double fileSize) {*/
			            		ByteBuffer bb = ByteBuffer.wrap(port);
			            		IntBuffer ib = bb.asIntBuffer();
			            		int nPort = ib.get(0);
			            		InetAddress nIP = null;
			            		try {
			            			nIP = InetAddress.getByAddress(IPaddr);
									System.out.println(nIP.getHostAddress());
								} catch (UnknownHostException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									System.out.println("cannot get IP addr string from PONG");
								}
				            		ByteBuffer bc = ByteBuffer.wrap(fileNum);
				            		int nFileNum = bc.getInt();
				            		ByteBuffer bd = ByteBuffer.wrap(fileSize);
				            		int nfileSize = bd.getInt();
				            		if(nIP != null){
				           			ServerInfo nServerInfo = new ServerInfo(nPort,nIP,nFileNum,nfileSize);
				           			nsl.addServer(nServerInfo);
				           			}
			           		}
				       		else{ // I'm the one who send ping when I rec ping from others
				       			System.out.println("I'm the one who send ping when I rec ping from others");
				       			IDRecorder idr = routingTable.getRecord(mID);
				       			Socket preSoc = idr.getSocket();
				       			try {
				       				DataOutputStream outToServer = new DataOutputStream(preSoc.getOutputStream());
				       				outToServer.write(data); // send data to the prev node
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} 
			           		}
			           	}
		            	else if(messageType == (byte)0x80){
		            		System.out.println("QUERY");
		            	}
		            	
		            	else if(messageType == (byte)0x81){
		            		System.out.println("QUERYHIT");
		            		
		            		if(routingTable.checkID(mID) == false) { 
		            			// I'm the one who send query initially
		            			int payloadLength = messageLength-MyConstants.HEADER_LENGTH;
			            		byte[] payload = new byte[payloadLength];
			            		System.arraycopy(data,MyConstants.HEADER_LENGTH,payload,0,payloadLength);
			            		System.out.println("copy payload");
			            		byte[] numberOfHits = new byte[4];
			            		byte[] port = new byte[4];
			            		byte[] IPaddr = new byte[4];
			            		byte[] Speed = new byte[4];
			            		byte[] resSet = new byte[payloadLength-16-4-4-2-1];
			            		byte[] serventID = new byte[16];
			            		System.arraycopy(payload,0,numberOfHits,3, 1);
			            		System.arraycopy(payload,1,port,2, 2);
			            		System.arraycopy(payload,3,IPaddr,0,4);
			            		System.arraycopy(payload,7,Speed,0,4);
			            		System.arraycopy(payload,11,resSet,0,payloadLength-16-4-4-2-1);
			            		System.arraycopy(payload,payloadLength-16,serventID,0,16);
			            		ByteBuffer bk = ByteBuffer.wrap(numberOfHits);
			            		IntBuffer ik = bk.asIntBuffer();
			            		int nNumberOfHits = ik.get(0);
			            		ByteBuffer bb = ByteBuffer.wrap(port);
			            		IntBuffer ib = bb.asIntBuffer();
			            		int nPort = ib.get(0);
			            		InetAddress nIP = null;
			            		try {
			            			nIP = InetAddress.getByAddress(IPaddr);
									System.out.println(nIP.getHostAddress());
								} catch (UnknownHostException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									System.out.println("cannot get IP addr string from PONG");
								}
			            		int nSpeed = byte2int(Speed);
			            		String nServentID = new String(serventID);
			            		byte[] fileIndex = new byte[4];
			            		byte[] fileSize = new byte[4];
			            		byte[] fileName = new byte[resSet.length - 8];
			            		System.arraycopy(resSet,0,fileIndex,0,4);
			            		System.arraycopy(resSet,4,fileSize,0,4);
			            		System.arraycopy(resSet,8,fileName,0,fileName.length);
			            		int nFileIndex = byte2int(fileIndex);
			            		int nFileSize = byte2int(fileSize);
			            		String nFileName = new String(fileName);
			            		System.out.println(nFileName+" FileIndex:"+nFileIndex+" FileSize:"+nFileSize);
				            		if(nIP != null){
				           			//ServerInfo nServerInfo = new ServerInfo(nPort,nIP,nFileNum,nfileSize);
				           			//nsl.addServer(nServerInfo);
				           			}
			           		}
				       		else{ // I'm the one who send ping when I rec ping from others
				       			System.out.println("I'm the one who send query when I rec query from others");
				       			IDRecorder idr = routingTable.getRecord(mID);
				       			Socket preSoc = idr.getSocket();
				       			try {
				       				DataOutputStream outToServer = new DataOutputStream(preSoc.getOutputStream());
				       				outToServer.write(data); // send data to the prev node
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} 
			           		}
		            		
		            		

		            	}
	            	}
			
	          	}
		 }
	
			
	}
	
	}
	
}

