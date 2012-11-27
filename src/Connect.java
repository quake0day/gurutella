

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Connect extends Thread{
	
	private String targetIPAddress;
	private int tcpport;
	public int maxsize = 50;
	private Socket newEstablishedSocket = null;
	//private simpella Simpella;
	private ClientInfoList clients;
	public Connect (String targetIPAddressr, String tcp, ClientInfoList client) throws IOException{
		targetIPAddress = targetIPAddressr;
		tcpport = Integer.parseInt(tcp);
		this.clients = client;
		//this.echo = echo;
        ExecutorService threadPool = Executors.newFixedThreadPool(maxsize);
        boolean isAbleToConnect = true;
        //System.out.println(targetIPAddress.toString().equals("127.0.0.1"));
        InetAddress addr = null;
        String localIPAddr = null;
        addr = InetAddress.getLocalHost();
        localIPAddr = addr.getHostAddress().toString();
        if(targetIPAddress.toString().equals("127.0.0.1") || targetIPAddress.toString().equals("localhost") || targetIPAddress.toString().equals(localIPAddr)){
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
			//newEstablishedSocket = new Socket(targetIPAddress, tcpport);
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
		clients.add(socket);	
		//start();
	}

	public void run(){
        PrintWriter out = null;
        BufferedReader in = null;
        String inputLine;
        boolean waitForReply = true;
		try {
			in = new BufferedReader(new InputStreamReader(newEstablishedSocket.getInputStream()));
	        out = new PrintWriter(newEstablishedSocket.getOutputStream(), true); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//The connection initiator sends a string (case-sensitive)
		while(waitForReply){
			out.print("SIMPELLA CONNECT/0.6\r\n");
			try {
				Thread.sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if((inputLine = in.readLine()) != null){
					System.out.print(inputLine);
					waitForReply = false;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		while(true){
			try {
				while((inputLine = in.readLine()) != null){
					System.out.println("Server:"+inputLine);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				break;
			}
			
		}
		
	}
	
}
