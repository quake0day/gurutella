
import java.net.*; 
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*; 


//import com.sun.corba.se.impl.orbutil.closure.Future;

public class Tcpserver extends Thread
{ 
 //protected Socket clientSocket;
 private ServerSocket _serverSK;

 public static int count = 0;
 public Socket[] socketArray;
 //public ArrayList<Socket> clients = null;
 private ClientInfoList clients;
 private MessageIDList _routingTable;
 private FileInfoList _fileList;
 private InetAddress IP;
 private int port;
 private ExecutorService threadPool = Executors.newFixedThreadPool(MyConstants.MAX_THREAD_NUM);
 
 public Tcpserver (int tcpport,ClientInfoList client,MessageIDList rt,InetAddress IP,FileInfoList filelist) throws IOException, InterruptedException
   {
	 //set the max size of socket pool
	 this.port = tcpport;
	 this.IP = IP;
	 this.clients = client;
	 this._routingTable = rt;
	 this._fileList = filelist;
	 //ServerSocket serverSocket = null; 
	 socketArray = new Socket[MyConstants.MAX_THREAD_NUM];
	 /*ExecutorService threadPool = Executors.newFixedThreadPool(maxsize);
	 try{
		 serverSocket = new ServerSocket(port); 
	 } catch(Exception e){
		 System.out.println("You're using a port that cannot Establish TCP connection, program halt...");
		 System.exit(1);
	 }
	// System.out.println ("Connection Socket Created");
     while(true){
    	 threadPool.submit(new Tcpserver(serverSocket.accept(), clients));
     }*/
	try{
		_serverSK = new ServerSocket(port);   
		} catch(Exception e){
			System.out.println("You're using a port that cannot Establish TCP connection, program halt...");
			System.exit(1);
		}
   }

 /*private Tcpserver (ServerSocket serverSoc, ClientInfoList clients,MessageIDList routingTable,InetAddress IP, int tcpport,FileInfoList filelist) throws IOException
 {
     _serverSK = serverSoc;
     //clients.add(clientSoc);
     this.clients = clients;
     System.out.println("get Conn. request from "+
			 _serverSK.accept().getInetAddress().toString()+ "\n The TCP connection is successfully estabilshed");
    // start();
 }*/


 public void run()
   {
	 //boolean isAlive = true;
	//PrintWriter outServer = null;
	Socket listenSocket = null;
	//Create a server socket for every accepted connection
	
	while(true)
	{
		try {
			listenSocket = _serverSK.accept();
			threadPool.submit(new ServerHandler(listenSocket, clients,
					_routingTable, port, IP, _fileList));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
   }
 
		/*private void serverHandler()
		{
			
		
		    while(true){
		    	try{
		    		threadPool.submit(new Tcpserver(_serverSK, clients,_routingTable,IP,port,_fileList));
		    	}catch (IOException e) {
		    		e.getStackTrace();
		    	}
		    }	
			//ThreadPool creation finish
		   }*/
}

