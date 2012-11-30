
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

 private Tcpserver (ServerSocket serverSoc, ClientInfoList clients,MessageIDList routingTable,InetAddress IP, int tcpport,FileInfoList filelist) throws IOException
 {
     _serverSK = serverSoc;
     //clients.add(clientSoc);
     this.clients = clients;
     System.out.println("get Conn. request from "+
			 _serverSK.accept().getInetAddress().toString()+ "\n The TCP connection is successfully estabilshed");
    // start();
 }

public boolean checkMessagePacketValidation(byte[] data,int MessageLength){
	// check if length is larger than 22
	if(MessageLength < 22){
		return false;
	}
	return true;
	
}
 public void run()
   {
	 boolean isAlive = true;
	PrintWriter outServer = null;
	Socket listenSocket = null;
	//Create a server socket for every accepted connection
	try {
		listenSocket = _serverSK.accept();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	int tempClientIndex = clients.size(1);
	//Socket listenSocket = clients.get(1,tempClientIndex);
    try { 
         outServer = new PrintWriter(listenSocket.getOutputStream(), 
                                      true); 
         BufferedReader inServer = new BufferedReader( 
                 new InputStreamReader(listenSocket.getInputStream())); 

         String inputLine; 
         
         InputStream stream = listenSocket.getInputStream();
         while(isAlive){
            byte[] data = new byte[4096];
         	int messageLength = stream.read(data);
         	if(messageLength == -1){ // means a broken socket
         		clients.remove(1, listenSocket);
         		isAlive = false;
         		break;
         	}
         	System.out.println(messageLength);
         	
         	String recResult = new String(data);
            // hand shake
            if(recResult.trim().equals("SIMPELLA CONNECT/0.6")){
          	  if(tempClientIndex >= 0 && tempClientIndex < MyConstants.MAX_INCOMING_CONNECTION_NUM ){ // We can accpet
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
            	if(checkMessagePacketValidation(data,messageLength)){
	        		byte[] mID = new byte[16];
	        		System.arraycopy(data, 0, mID, 0, 16);
	            	byte messageType = (byte)data[16];
	            	byte TTL = (byte)data[17];
	            	byte Hops = (byte)data[18];
	            	if((int)(TTL+Hops) >=7 && (int)(TTL+Hops) <= 15){
		            	if(messageType == 0x00){
		            		System.out.println("PING MESSAGE");
		            		//Iterator<MessageContainer> iter = _routingTable.iterator();
		            		boolean hasSameMessageID = false;
		            		
		            		hasSameMessageID = _routingTable.checkID(mID);
		            		if(hasSameMessageID == false){
		            			_routingTable.addID(mID,listenSocket);
		            				
		            			Update sendNext = new Update(clients,listenSocket,(int)TTL-1,(int)Hops+1);
		            			sendNext.start();
		            			
		            			// reply with PONG
		            			PongMessage PongMessageContainer = new PongMessage(mID,port,IP,_fileList.getFileNum(),_fileList.getFileSize());
		            			byte [] pong = new byte[4096];
		            			pong = PongMessageContainer.convertToByte();
		    					DataOutputStream outToServer = new DataOutputStream(listenSocket.getOutputStream());
		    					outToServer.write(pong);
		            		}
		            		
		            	}
		            	else if(messageType == 0x01){
		            		System.out.println("PONG MESSAGE");
		            	}
		            	else if(messageType == 0x80){
		            		System.out.println("QUERY MESSAGE");
		            	}
		            	else if(messageType == 0x81){
		            		System.out.println("QUERY HIT MESSAGE");
		            	}
		            	else{

		            	}
	            	}
	            	

	         	
	            }
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
    		threadPool.submit(new Tcpserver(_serverSK, clients,_routingTable,IP,port,_fileList));
    	}catch (IOException e) {
    		e.getStackTrace();
    	}
    }	
	//ThreadPool creation finish
    }
}

