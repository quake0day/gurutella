import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




/**
 * 
 */

/**
 * @author quake0day
 * @author Tianmiao
 */
public class simpella /*extends Thread*/{
	// define the max thread num in a threadpool
	private static int MAX_THREAD_NUM = 9;
	private static int tcpPort1 = 6346;
	private static int tcpPort2 = 6745;
	public static ClientInfoList _clientList;
	public static FileInfoList _fileList;
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREAD_NUM);		
		if (args.length > 0){
			tcpPort1 = Integer.parseInt(args[0]);
			tcpPort2 = Integer.parseInt(args[1]);
			if(tcpPort1 < 0 && tcpPort1 > 60000 && tcpPort2 < 0 && tcpPort2 > 60000){
				System.err.println("Error when try to listen to port1:"+tcpPort1+" and port2:"+tcpPort2);
				System.exit(1);
			}
			else if (tcpPort1 == tcpPort2){
				System.err.println("Error when try to listen to port1:"+tcpPort1+" and port2:"+tcpPort2);
				System.exit(1);
			}
		}
		else
		{
			System.out.println("Usage: java Echoer <tcp-port> <udp-port>");
			//System.exit(1);  for test only
		}
		showWelcomeInfo(tcpPort1,tcpPort2);
		 /*
		 Thread thread = new Thread(new Monitor(tcpport,udpport,clients));
		 thread.start();
		 */
		 _fileList = new FileInfoList();
		 _clientList = new ClientInfoList();
		 threadPool.submit(new Monitor(tcpPort1,tcpPort2,_clientList, _fileList));
		 Thread tcp1 = new Thread(new Tcpserver(10023,_clientList));
		 //Thread tcp2 = new Thread(new Tcpserver(10025,clients));
		//_fileList = new FileInfoList();
		//new Monitor(tcpPort1, tcpPort2, clients, _fileList);
		}
	public static void showWelcomeInfo(int tcpPort1,int tcpPort2){
		InetAddress IP = null;
		boolean usePublicDNS = false;
		try {
			DatagramSocket udpSocket = null;
			udpSocket = new DatagramSocket();
			udpSocket.connect(InetAddress.getByName("8.8.8.8"), 53); // Using public DNS google
			IP = udpSocket.getLocalAddress();
			udpSocket.close();
			usePublicDNS = true;
		} catch (SocketException e1) {
			System.out.println("cannot get IP address through 8.8.8.8");
		}
		catch (UnknownHostException e1) {
			System.out.println("cannot get IP address through 8.8.8.8");
		}
		if(!usePublicDNS){ // no internet connection or goolge 8.8.8.8 down
			try {
				IP = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String delimiter = "/";
		String localIP = IP.toString().split(delimiter)[1];
		System.out.println("Local IP:"+localIP);
		System.out.println("Simpella Net Port: "+tcpPort1);
		System.out.println("Downloading Port : "+tcpPort2);
		System.out.println("simpella version 0.6 (c) 2012-2013 CS,DTM");	
		System.out.println(" ");
	}

}
