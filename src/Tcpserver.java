
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
 protected Socket clientSocket;

 public static int count = 0;
 public int maxsize = 8;
 public Socket[] socketArray;
 public ArrayList<Socket> clients = null;
 private simpella Simpella;
 private int bufSize;
 private int port;
 public Tcpserver (int tcpport,simpella Simpella) throws IOException, InterruptedException
   {
	 //set the max size of socket pool
	 this.port = tcpport;
	 this.Simpella = Simpella;
	 ServerSocket serverSocket = null; 
	 socketArray = new Socket[maxsize];
	 ExecutorService threadPool = Executors.newFixedThreadPool(maxsize);
	 try{
		 serverSocket = new ServerSocket(port); 
	 } catch(Exception e){
		 System.out.println("You're using a port that cannot Establish TCP connection, program halt...");
		 System.exit(1);
	 }
	// System.out.println ("Connection Socket Created");
     while(true){
    	 threadPool.submit(new Tcpserver(serverSocket.accept()));
     }
     
   }

 private Tcpserver (Socket clientSoc) throws IOException
 {
     clientSocket = clientSoc;
     Simpella.clients.add(clientSoc);
     System.out.println("get Conn. request from "+
			 clientSoc.getInetAddress().toString()+ "\n The TCP connection is successfully estabilshed");
     start();
 }
 public void run()
   {
	PrintWriter outServer = null;
	int index = Simpella.clients.size()-1;
	Socket listenSocket = Simpella.clients.get(index);
    try { 
         outServer = new PrintWriter(listenSocket.getOutputStream(), 
                                      true); 
         BufferedReader inServer = new BufferedReader( 
                 new InputStreamReader(listenSocket.getInputStream())); 

         String inputLine; 
         
         while ((inputLine = inServer.readLine()) != null) 
         { 
        	 String inputMessage=null;
        	 try{
        		 inputMessage = inputLine.split(" ")[1];
        		 inputMessage = inputMessage.split("/")[0];
        	 }
        	 catch(Exception e){
        		 
        	 }
             if (inputMessage.equals("CONNECT")) // Get Connection requestion
             {
            	 System.out.println("haha");
            	 String acceptString ="SIMPELLA/0.6 200 OK\r\n";
            	 outServer.println(acceptString);
             }
             else{
            	 /*
              System.out.println ("		echoing: " + inputLine); 
              System.out.println ("		to: IP = "+listenSocket.getInetAddress().toString());
              System.out.println ("		type = tcp");
              outServer.println(inputLine); 
              */
            	 System.out.println("not equal");
             }
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
    }
}

