import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/**
 * @author Tianmiao
 */

/**
 * @author quake0day
 *
 */
public class simpella /*extends Thread*/{
	// define the max thread num in a threadpool
	private static int MAX_THREAD_NUM = 9;
	private static int tcpPort1 = 6346;
	private static int tcpPort2 = 6745;
	public static ArrayList<Socket> clients = new ArrayList<Socket>();
	public static FileInfoList _fileList;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
		 /*
		 Thread thread = new Thread(new Monitor(tcpport,udpport,clients));
		 thread.start();
		 
		 Thread udpserver = new Thread(new Udpserver(10029));
		 Thread server = new Thread(new server(new echoer()));
		 */
		// echoer echoer = new echoer();
		 //threadPool.submit(new Monitor(tcpPort1,tcpPort2,clients));
		_fileList = new FileInfoList();
		new Monitor(tcpPort1, tcpPort2, clients, _fileList);
//	 Thread quake1 = new Thread(new Justtest());
		}

}
