
import java.net.*; 
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*; 


//import com.sun.corba.se.impl.orbutil.closure.Future;

public class Tcpserver extends Thread
{ 
 //protected Socket clientSocket;
 private ServerSocket _serverSK;

 public static int count = 0;
 public int maxsize = 8;
 public Socket[] socketArray;
 //public ArrayList<Socket> clients = null;
 private ClientInfoList clients;
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
	PrintWriter outServer = null;
	//Create a server socket for every accepted connection
	try {
		clients.add_incoming(_serverSK.accept());
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	int tempClientIndex = clients.size(1) - 1;
	Socket listenSocket = clients.get(1,tempClientIndex);
    try { 
         outServer = new PrintWriter(listenSocket.getOutputStream(), 
                                      true); 
         BufferedReader inServer = new BufferedReader( 
                 new InputStreamReader(listenSocket.getInputStream())); 

         String inputLine; 
         
         while ((inputLine = inServer.readLine()) != null) 
         { 
        	 
            /*
              System.out.println ("		echoing: " + inputLine); 
              System.out.println ("		to: IP = "+listenSocket.getInetAddress().toString());
              System.out.println ("		type = tcp");
              */
              // hand shake
              if(inputLine.equals("SIMPELLA CONNECT/0.6")){
            	  if(tempClientIndex >= 0 && tempClientIndex < MyConstants.MAX_INCOMING_CONNECTION_NUM){ // We can accpet
            		  outServer.println(MyConstants.STATUS_200);
            	  }
            	  else{ // We cannot accept
            		  outServer.println(MyConstants.STATUS_503);
					  new Disconnect(tempClientIndex,1,clients);
            	  }
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

