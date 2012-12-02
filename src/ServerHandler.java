import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * ServerHandler handling Server responses
 */

/**
 * @author Tianmiao
 *
 */
public class ServerHandler extends Thread{
	private Socket _serverSocThread;
			ClientInfoList _cInfo; 
			PrintWriter _out2Client;
			MessageIDList _idList;
			FileInfoList _fList;
			InetAddress _IP;
			int _port;
			int _downPort;
			int _tempClientIndex;
			boolean _isAlive = true;
			

	public ServerHandler(Socket serverSoc, ClientInfoList cInfo, MessageIDList idList,
			int tcpPort, int tcpDownload, InetAddress IP, FileInfoList fList)
	{
		_cInfo = cInfo;
		_serverSocThread = serverSoc;
		_idList = idList;
		_fList = fList;
		_port = tcpPort;
		_IP = IP;
		_tempClientIndex = _cInfo.size(1);
		//Socket listenSocket = clients.get(1,tempClientIndex);
	}
	public void run()
	{
		try {
	         _out2Client = new PrintWriter(_serverSocThread.getOutputStream(), 
	                                      true); 
	         //BufferedReader inServer = new BufferedReader( 
	         //        new InputStreamReader(listenSocket.getInputStream())); 
	         
	         InputStream in2Server = _serverSocThread.getInputStream();
	         while(_isAlive){
	            byte[] conReq = new byte[25];				//Size for connection establishment
	         	int messageLength = in2Server.read(conReq);	//period(With no message container)
	         	
	         	if(messageLength == -1){ // means a broken socket
	         		_cInfo.remove(1, _serverSocThread);
	         		_isAlive = false;
	         		break;
	         	}
	         	
	         	System.out.println(messageLength);
	         	
	         	String recResult = new String(conReq);
	            // hand shake
	            if(recResult.trim().equals("SIMPELLA CONNECT/0.6")){
	          	  if(_tempClientIndex >= 0 && _tempClientIndex < MyConstants.MAX_INCOMING_CONNECTION_NUM ){ // We can accpet
	          		  _out2Client.println(MyConstants.STATUS_200);
	          		  _cInfo.add_incoming(_serverSocThread);
					  new Update(_cInfo, _idList);
					  //update.start();
					  
			    //Connection Established, start reading header
					  while (_isAlive)
					  {

				       	byte[] header = new byte[23];
						int mLength = in2Server.read(header);	
						
						if(mLength == -1){ // means a broken socket
			         		_cInfo.remove(1, _serverSocThread);
				        	_isAlive = false;
				        	break;
				         }
				        						
					  	byte[] mID = new byte[16];
		        		System.arraycopy(header, 0, mID, 0, 16);
		            	byte messageType = (byte)header[16];
		            	byte TTL = (byte)header[17];
		            	byte Hops = (byte)header[18];
		            	System.out.println("server Received Header");
		            	if((int)(TTL+Hops) == 7 && TTL < 8 && TTL > 0 && Hops >= 0 && Hops < 7){		            	
		            		
		            		System.out.println("HereIn");
			            	
		            		boolean hasSameMessageID = false;	//check ID here
		            		hasSameMessageID = _idList.checkID(mID);
		            		
		            		if (hasSameMessageID == false){
		            			
		            			///////////Ping Received/////////
			            		if (messageType == (byte) 0x00){
				            		System.out.println("toserver PING MESSAGE");
				            		//Iterator<MessageContainer> iter = _routingTable.iterator();
				            		_idList.addRecord(new IDRecorder(mID, _serverSocThread));
				            			
				            		Update sendNext = new Update(_cInfo,_serverSocThread
				            				,(int)TTL-1,(int)Hops+1, _idList);
				            		sendNext.start();
				            		
				            		// reply with PONG: REVISED
				            		MessageContainer pongHitContainer = new MessageContainer(mID);//_port,_IP,_fList.getFileNum(),_fList.getFileSize());
				            		pongHitContainer.setType(2);	//Pong Message
				            		pongHitContainer.setTTL(7);
				            		pongHitContainer.setHops(0);
				            		pongHitContainer.setPayloadLength(14);
				            		PongPayload payload = new PongPayload(_port, _IP, _fList.getFileNum(), _fList.getFileSize());
				            		pongHitContainer.addPayLoad(payload.getPayLoad());
				            		
				            		byte [] pong = new byte[4096];
				            		pong = pongHitContainer.convertToByte();
				    				DataOutputStream outToServer = new DataOutputStream(_serverSocThread.getOutputStream());
				    				outToServer.write(pong);
				            	}
			            		///////////Pong Received///////////
				            	else if (messageType == (byte) 0x01){
				            		byte[] data = new byte[14];
									in2Server.read(data);
				            		System.out.println("server PONG MESSAGE");
				            	}
			            		//////////Query Received///////////
				            	else if (messageType == (byte)0x80){
				            		System.out.println("QUERY MESSAGE");
				            		hasSameMessageID = _idList.checkID(mID);
				            		byte[] payload = new byte[4096];
				            		byte[] minimumSpeed = new byte[2];
				            		int payloadLength = in2Server.read(payload);
				            		byte[] queryString = new byte [payloadLength-2];
				            		System.out.println("PayloadLength:"+payloadLength);
				            		
				            		System.arraycopy(payload, 0, minimumSpeed, 0, 2);
				            		System.arraycopy(payload, 2, queryString, 0, queryString.length);
				            		//ByteBuffer bb = ByteBuffer.wrap(minimumSpeed);
				            		//IntBuffer ib = bb.asIntBuffer();
				            		//int nMinSpeed = ib.get(0);
				            		String nQueryString = new String(queryString, "UTF-8");
				            		System.out.println("Query:"+nQueryString);
				            		//ByteBuffer bc = ByteBuffer.wrap(queryString);
				 
				            		//String queryString = 
			            			Query sendNext = new Query(nQueryString,_cInfo,_serverSocThread
			            					,(int)TTL-1,(int)Hops+1, _idList);
			            			sendNext.start();
			            			
			            			// reply with Query Hit
			            			ArrayList<QueryResultSet> _qrs = _fList.queryFile(nQueryString);
			            			int _NumberOfHits = _qrs.size();
			            			Iterator<QueryResultSet> qrsIter = _qrs.iterator();
			            			while(qrsIter.hasNext()){ // create multiple query hit packet
			            				QueryResultSet qrs = qrsIter.next();
				            			MessageContainer queryHitContainer = new MessageContainer(mID);//_port,_IP,_fList.getFileNum(),_fList.getFileSize());
				            			queryHitContainer.setType(4);	//QueryHit Message
				            			queryHitContainer.setTTL(Hops+2);
				            			queryHitContainer.setHops(0);
				            			int Speed = 10000;
				            			String serventID = "12j3l2j3ljlasjdfasdf";
				            			//queryHitContainer.setPayloadLength(14);
				            			//int numberOfHits, int port, InetAddress IP, int Speed, String serventID
				            			QueryHitPayLoad queryPayLoad = new QueryHitPayLoad(_NumberOfHits,_port,_IP,Speed,qrs, serventID);
				            			//PongPayload payload = new PongPayload(_port, _IP, _fList.getFileNum(), _fList.getFileSize());
				            			queryHitContainer.addPayLoad(queryPayLoad.getPayLoad());
				            			byte [] queryHit = new byte[4096];
				            			queryHit = queryHitContainer.convertToByte();
				    					DataOutputStream outToServer = new DataOutputStream(_serverSocThread.getOutputStream());
				    					outToServer.write(queryHit);
			            			}
				            	}	
			            	}
		            		//////////QueryHit received(??)/////////
			            	else if (messageType == (byte)0x81){
			            		System.out.println("QUERY HIT MESSAGE");
			            	}
			            	else
			            	{
			            		break;	 
			            	}
			            }
		            	else{
			            	break;
			            }
					  }
	          	  }
	          	  else{ // We cannot accept
	          		  _out2Client.println(MyConstants.STATUS_503);
						  //new Disconnect(tempClientIndex,1,clients);

	            //Next, start reading header
	
	         	//System.out.println(res);
	            	// regular message, judge message type
	            	//if(checkMessagePacketValidation(conReq, messageLength)){
	
		         	
	        // outServer.close(); 
	        // inServer.close(); 
	        // listenSocket.close(); 
	          	  }
	           }
	    } 
	}catch(IOException e)
	    {
	    	e.getStackTrace();
	    }
	}
}
		//Create additional threads from ThreadPool 
		//for the single branch thread of TCPServer declared in class Simpella 
	    
	    /*public boolean checkMessagePacketValidation(byte[] data,int MessageLength){
	    	// check if length is larger than 22
	    	if(MessageLength < 22){
	    		return false;
	    	}
	    	else
	    		return true;*/
