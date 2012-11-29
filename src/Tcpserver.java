
import java.net.*; 
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*; 
import java.lang.Object;

//import com.sun.corba.se.impl.orbutil.closure.Future;

public class Tcpserver extends Thread
{ 

 private static final long TIMEOUT = 3000;
 private SocketChannel clientSocketChannel;
 private Selector selector;
 //protected Socket clientSocket;
 private ServerSocket _serverSK;

 public static int count = 0;
 public int maxsize = 8;
 public Socket[] socketArray;
 //public ArrayList<Socket> clients = null;
 private ClientInfoList clients;
 private int bufSize;
 private int port;
 private ExecutorService threadPool = Executors.newFixedThreadPool(maxsize);
 
 public Tcpserver (int tcpport,ClientInfoList client) throws IOException, InterruptedException
   {
	 //set the max size of socket pool
	 this.port = tcpport;
	 this.clients = client;
	 //ServerSocket serverSocket = null; 
	 socketArray = new Socket[maxsize];
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

 private Tcpserver (ServerSocket serverSoc, ClientInfoList clients) throws IOException
 {
     _serverSK = serverSoc;
     //clients.add(clientSoc);
     this.clients = clients;
     System.out.println("get Conn. request from "+
			 _serverSK.accept().getInetAddress().toString()+ "\n The TCP connection is successfully estabilshed");
    // start();
 }
 
 public void run()
   {
	 System.out.println("here you are");
	PrintWriter outServer = null;
	//Create a server socket for every accepted connection
	try {
		clients.add(_serverSK.accept());
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	int index = clients.size() - 1;
	Socket listenSocket = clients.get(index);
    try { 
         outServer = new PrintWriter(listenSocket.getOutputStream(), 
                                      true); 
         BufferedReader inServer = new BufferedReader( 
                 new InputStreamReader(listenSocket.getInputStream())); 

         String inputLine; 
         
         while ((inputLine = inServer.readLine()) != null) 
         { 
        	 
            	
              System.out.println ("		echoing: " + inputLine); 
              System.out.println ("		to: IP = "+listenSocket.getInetAddress().toString());
              System.out.println ("		type = tcp");
                       
         } 
         outServer.close(); 
         inServer.close(); 
         listenSocket.close(); 
        } 
    catch (IOException e) 
        { 
         System.err.println("Problem with Communication Server");
         System.exit(1); 
        }
	//Create additional threads from ThreadPool 
	//for the single branch thread of TCPServer declared in class Simpella 

    while(true){
    	try{
    		threadPool.submit(new Tcpserver(_serverSK, clients));
    	}catch (IOException e) {
    		e.getStackTrace();
    	}
    }	
	//ThreadPool creation finish
    }
}

