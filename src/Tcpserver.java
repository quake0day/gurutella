
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
 public int maxsize = 8;
 public Socket[] socketArray;
 //public ArrayList<Socket> clients = null;
 private ClientInfoList clients;
 private MessageIDList _routingTable;
 private int port;
 private ExecutorService threadPool = Executors.newFixedThreadPool(maxsize);
 
 public Tcpserver (int tcpport,ClientInfoList client,MessageIDList rt) throws IOException, InterruptedException
   {
	 //set the max size of socket pool
	 this.port = tcpport;
	 this.clients = client;
	 this._routingTable = rt;
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

 private Tcpserver (ServerSocket serverSoc, ClientInfoList clients,MessageIDList routingTable) throws IOException
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
	Socket listenSocket = null;
	//Create a server socket for every accepted connection
	try {
		listenSocket = _serverSK.accept();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	int tempClientIndex = clients.size(1) - 1;
	//Socket listenSocket = clients.get(1,tempClientIndex);
    try { 
         outServer = new PrintWriter(listenSocket.getOutputStream(), 
                                      true); 
         BufferedReader inServer = new BufferedReader( 
                 new InputStreamReader(listenSocket.getInputStream())); 

         String inputLine; 
         
         InputStream stream = listenSocket.getInputStream();
         while(true){
         byte[] data = new byte[100];
         	int count = stream.read(data);
         	System.out.println(count);
         	String recResult = new String(data);
            // hand shake
            if(recResult.trim().equals("SIMPELLA CONNECT/0.6")){
          	  if(tempClientIndex >= -1 && tempClientIndex < MyConstants.MAX_INCOMING_CONNECTION_NUM - 1){ // We can accpet
          		  outServer.println(MyConstants.STATUS_200);

          		  clients.add_incoming(listenSocket);
				  Thread update = new Thread(new Update(clients));
				  update.start();
          	  }
          	  else{ // We cannot accept
          		  outServer.println(MyConstants.STATUS_503);
					  //new Disconnect(tempClientIndex,1,clients);
          	  }
            }
            else{
         	//System.out.println(res);
            	// regular message, judge message type
            	if((byte)data[16] == 0x00){
            		System.out.println("PING MESSAGE");
            		byte[] mID = new byte[16];
            		System.arraycopy(data, 0, mID, 0, 16);
            		//Iterator<MessageContainer> iter = _routingTable.iterator();
            		boolean hasSameMessageID = false;
            		hasSameMessageID = _routingTable.checkID(mID);
            		if(hasSameMessageID == false){
            			_routingTable.addID(mID);
            			Update sendNext = new Update(clients,listenSocket);
            			sendNext.start();
            		}


            	}
            	if((byte)data[16] == 0x01){
            		System.out.println("PONG MESSAGE");
            	}
            	if((byte)data[16] == 0x80){
            		System.out.println("QUERY MESSAGE");
            	}
            	if((byte)data[16] == 0x81){
            		System.out.println("QUERY HIT MESSAGE");
            	}
            	/*
         	int i = 0;
         	for(i=0 ; i < data.length ; i++){
         		System.out.println("i="+i+" value:"+(int)data[i]);
         	}
         	*/
            }
         }
         /*
         while ((inputLine = inServer.readLine()) != null) 
         { 
        	 
            
              System.out.println ("		echoing: " + inputLine); 
              System.out.println ("		to: IP = "+listenSocket.getInetAddress().toString());
              System.out.println ("		type = tcp");
             
              // hand shake
              if(inputLine.equals("SIMPELLA CONNECT/0.6")){
            	  if(tempClientIndex >= -1 && tempClientIndex < MyConstants.MAX_INCOMING_CONNECTION_NUM - 1){ // We can accpet
            		  outServer.println(MyConstants.STATUS_200);
            		  clients.add_incoming(listenSocket);
					  Thread update = new Thread(new Update(clients));
					  update.start();

            	  }
            	  else{ // We cannot accept
            		  outServer.println(MyConstants.STATUS_503);
					  //new Disconnect(tempClientIndex,1,clients);
            	  }
              }
                       
         } 
         */
        // outServer.close(); 
        // inServer.close(); 
        // listenSocket.close(); 
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
    		threadPool.submit(new Tcpserver(_serverSK, clients,_routingTable));
    	}catch (IOException e) {
    		e.getStackTrace();
    	}
    }	
	//ThreadPool creation finish
    }
}

