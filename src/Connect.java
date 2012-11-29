

import java.io.BufferedReader;
import java.io.IOException;
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
	private int tempClientIndex;
	public Connect (String targetIPAddressr, String tcp, ClientInfoList client) throws IOException{
		targetIPAddress = targetIPAddressr;
		tcpport = Integer.parseInt(tcp);
		this.clients = client;
        ExecutorService threadPool = Executors.newFixedThreadPool(MyConstants.MAX_THREAD_NUM);
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
			threadPool.submit(new Connect(new Socket(targetIPAddress,tcpport),clients));
			System.out.println("Info: The connection between this machine and "+targetIPAddress+" "+tcpport +" is successfully estabilshed");
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
	public Connect(Socket socket,ClientInfoList clients) {
		// TODO Auto-generated constructor stub
		newEstablishedSocket = socket;
		clients.add_outgoing(socket);	
		tempClientIndex = clients.size(0) - 1;
	}

	public void run(){
        BufferedReader in = null;
        PrintWriter outServer = null;
        String inputLine;
		try {
			in = new BufferedReader(new InputStreamReader(newEstablishedSocket.getInputStream()));
			outServer = new PrintWriter(newEstablishedSocket.getOutputStream(), 
			        true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Do handshake
		outServer.println("SIMPELLA CONNECT/0.6\r\n");
		
		while(true){
			try {
				while((inputLine = in.readLine()) != null){
					//System.out.println("Received:"+inputLine);
					// Get hand shake reply
					if(inputLine.equals(MyConstants.STATUS_200_REC)){
						// Print out <string>
						System.out.println(inputLine.split("200 ")[1]);
					}
					else if(inputLine.equals(MyConstants.STATUS_503_REC)){
						// Print out <string>
						System.out.println(inputLine.split("503 ")[1]);
						new Disconnect(tempClientIndex,0,clients);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				break;
			}
			
		}
		
	}
	
}
