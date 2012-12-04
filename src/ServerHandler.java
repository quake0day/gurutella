import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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
    ConnectionInfoList _cInfo; 
    PrintWriter _out2Client;
    MessageIDList _idList;
    FileInfoList _fList;
    QueryResultList _qrl;
    NetworkServerList _nsl;
    InetAddress _IP;
    int _port;
    int _downPort;
    int _tempClientIndex;
    boolean _isAlive = true;


    public ServerHandler(Socket serverSoc, ConnectionInfoList cInfo, MessageIDList idList,
            int tcpPort, int tcpDownload, InetAddress IP, FileInfoList fList,MonitorNetwork _mnl,QueryResultList qrl,NetworkServerList nsl)
    {
        _cInfo = cInfo;
        _serverSocThread = serverSoc;
        _idList = idList;
        _fList = fList;
        _port = tcpPort;
        _qrl = qrl;
        _nsl = nsl;
        _downPort = tcpDownload;
        _IP = IP;
        _tempClientIndex = _cInfo.size();
        //Socket listenSocket = clients.get(1,tempClientIndex);
    }
    
    public int byte2int(byte[] i){
        ByteBuffer bc = ByteBuffer.wrap(i);
        return bc.getInt();
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
                        _cInfo.addConnection(new ConnectionInfo(_port, _serverSocThread));
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
                            byte[] payloadLen = new byte [4];
                            System.arraycopy(header,19,payloadLen,2,2);
                            int pLength = byte2int(payloadLen);
                            byte[] data = new byte [pLength];
                            try {
                            	in2Server.read(data);
        					} catch (IOException e1) {
        						// TODO Auto-generated catch block
        						e1.printStackTrace();
        					}
                            System.out.println("server Received Header");
                            if((int)(TTL+Hops) <= 14 && (int)TTL < 8 && (int)TTL > 0 && (int)Hops >= 0 && (int)Hops < 7){		            	

                                System.out.println("HereIn");

                                if (messageType == (byte) 0x00){
                                    System.out.println("toserver PING MESSAGE");
                                    //Iterator<MessageContainer> iter = _routingTable.iterator();
                                    boolean hasSameMessageID = false;
                                    hasSameMessageID = _idList.checkID(mID);
                                    if(hasSameMessageID == false){
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

                                        byte [] pong = new byte[MyConstants.MAX_PAYLOAD_LENGTH];
                                        pong = pongHitContainer.convertToByte();
                                        DataOutputStream outToServer = new DataOutputStream(_serverSocThread.getOutputStream());
                                        outToServer.write(pong);
                                    }
                                    /*
                                    else
                                    {
                                        _isAlive = false;
                                        break;
                                    }
                                    */

                                }
                                else if (messageType == (byte) 0x01){
                                    //byte[] data = new byte[14];
                                    //in2Server.read(data);
                                    System.out.println("server PONG MESSAGE");
                                    System.out.println("toclient PONG");
                                    if(_idList.checkID(mID) == false) { 
                                        // I'm the one who send ping initially
                                        byte[] payload = data;
                                        //System.arraycopy(data,23,payload,0,14);
                                        System.out.println("copy payload");
                                        byte[] port = new byte[4];
                                        byte[] IPaddr = new byte[4];
                                        byte[] fileNum = new byte[4];
                                        byte[] fileSize = new byte[4];
                                        System.arraycopy(payload,0,port,2, 2);
                                        System.arraycopy(payload,2,IPaddr,0,4);
                                        System.arraycopy(payload,6,fileNum,0,4);
                                        System.arraycopy(payload,10,fileSize,0,4);
                                        /*public ServerInfo(int port, InetAddress IP, int fileNum,
                                          double fileSize) {*/
                                        ByteBuffer bb = ByteBuffer.wrap(port);
                                        IntBuffer ib = bb.asIntBuffer();
                                        int nPort = ib.get(0);
                                        InetAddress nIP = null;
                                        try {
                                            nIP = InetAddress.getByAddress(IPaddr);
                                            System.out.println(nIP.getHostAddress());
                                        } catch (UnknownHostException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                            System.out.println("cannot get IP addr string from PONG");
                                        }
                                        ByteBuffer bc = ByteBuffer.wrap(fileNum);
                                        int nFileNum = bc.getInt();
                                        ByteBuffer bd = ByteBuffer.wrap(fileSize);
                                        int nfileSize = bd.getInt();
                                        if(nIP != null){
                                            ServerInfo nServerInfo = new ServerInfo(nPort,nIP,nFileNum,nfileSize);
                                            _nsl.addServer(nServerInfo);
                                        }
                                    }
                                    else{ // I'm the one who send ping when I rec ping from others
                                        System.out.println("I'm the one who send ping when I rec ping from others");
                                        IDRecorder idr = _idList.getRecord(mID);
                                        Socket preSoc = idr.getSocket();
                                        try {
                                            DataOutputStream outToServer = new DataOutputStream(preSoc.getOutputStream());
                                            outToServer.write(data); // send data to the prev node
                                        } catch (IOException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        } 
                                    }
                                }
                                else if (messageType == (byte)0x80){
                                    System.out.println("QUERY MESSAGE");
                                    boolean hasSameMessageID = false;
                                    hasSameMessageID = _idList.checkID(mID);
                                    //byte[] payload = new byte[4096];
                                    byte[] payload = data;
                                    byte[] minimumSpeed = new byte[2];
                                    //int payloadLength = in2Server.read(payload);
                                    byte[] queryString = new byte [pLength-2];
                                    System.out.println("PayloadLength:"+pLength);

                                    System.arraycopy(payload, 0, minimumSpeed, 0, 2);
                                    System.arraycopy(payload, 2, queryString, 0, queryString.length);
                                    //ByteBuffer bb = ByteBuffer.wrap(minimumSpeed);
                                    //IntBuffer ib = bb.asIntBuffer();
                                    //int nMinSpeed = ib.get(0);
                                    // !! very important trim the last \0
                                    byte[] queryStringtrim = new byte[queryString.length-1];
                                    System.arraycopy(queryString, 0, queryStringtrim, 0, queryString.length-1);

                                    String nQueryString = new String(queryStringtrim);
                                    System.out.println("Query:"+nQueryString);
                                    //ByteBuffer bc = ByteBuffer.wrap(queryString);

                                    //String queryString = 
                                    if (hasSameMessageID == false){
                                        _idList.addRecord(new IDRecorder(mID, _serverSocThread));

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
                                            queryHitContainer.setTTL((int)Hops+2);
                                            queryHitContainer.setHops(0);
                                            int Speed = 10000;
                                            String serventID = "12j3l2j3ljlasjdfasdf";
                                            //queryHitContainer.setPayloadLength(14);
                                            //int numberOfHits, int port, InetAddress IP, int Speed, String serventID
                                            QueryHitPayLoad queryPayLoad = new QueryHitPayLoad(_NumberOfHits,_downPort,_IP,Speed,qrs, serventID);
                                            //PongPayload payload = new PongPayload(_port, _IP, _fList.getFileNum(), _fList.getFileSize());
                                            queryHitContainer.addPayLoad(queryPayLoad.getPayLoad());
                                            queryHitContainer.setPayloadLength(queryPayLoad.getPayloadLength());
                                            byte [] queryHit = new byte[MyConstants.MAX_PAYLOAD_LENGTH];
                                            queryHit = queryHitContainer.convertToByte();
                                            DataOutputStream outToServer = new DataOutputStream(_serverSocThread.getOutputStream());
                                            outToServer.write(queryHit);
                                            outToServer.flush();

                                        }
                                    }	
                                }
                                //////////QueryHit received(??)/////////
                                else if (messageType == (byte)0x81){
                                    System.out.println("QUERY HIT MESSAGE");
                                    if(_idList.checkID(mID) == false) { 
                                        // I'm the one who send query initially
                                        //int payloadLength = messageLength-MyConstants.HEADER_LENGTH;
                                        byte[] payload = data;
                                        //System.arraycopy(data,0,payload,0,pLength);
                                        System.out.println("copy payload");
                                        byte[] numberOfHits = new byte[4];
                                        byte[] port = new byte[4];
                                        byte[] IPaddr = new byte[4];
                                        byte[] Speed = new byte[4];
                                        byte[] resSet = new byte[pLength-16-4-4-2-1];
                                        byte[] serventID = new byte[16];
                                        System.arraycopy(payload,0,numberOfHits,3, 1);
                                        System.arraycopy(payload,1,port,2, 2);
                                        System.arraycopy(payload,3,IPaddr,0,4);
                                        System.arraycopy(payload,7,Speed,0,4);
                                        System.arraycopy(payload,11,resSet,0,pLength-16-4-4-2-1);
                                        System.arraycopy(payload,pLength-16,serventID,0,16);
                                        ByteBuffer bk = ByteBuffer.wrap(numberOfHits);
                                        IntBuffer ik = bk.asIntBuffer();
                                        int nNumberOfHits = ik.get(0);
                                        ByteBuffer bb = ByteBuffer.wrap(port);
                                        IntBuffer ib = bb.asIntBuffer();
                                        int nPort = ib.get(0);
                                        InetAddress nIP = null;
                                        try {
                                            nIP = InetAddress.getByAddress(IPaddr);
                                            System.out.println(nIP.getHostAddress());
                                        } catch (UnknownHostException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                            System.out.println("cannot get IP addr string from PONG");
                                        }
                                       // int nSpeed = byte2int(Speed);
                                       // String nServentID = new String(serventID);
                                        byte[] fileIndex = new byte[4];
                                        byte[] fileSize = new byte[4];
                                        byte[] fileName = new byte[resSet.length - 8];
                                        System.arraycopy(resSet,0,fileIndex,0,4);
                                        System.arraycopy(resSet,4,fileSize,0,4);
                                        System.arraycopy(resSet,8,fileName,0,fileName.length);
                                        int nFileIndex = byte2int(fileIndex);
                                        int nFileSize = byte2int(fileSize);
                                        String nFileName = new String(fileName);
                                        System.out.println(nFileName+" FileIndex:"+nFileIndex+" FileSize:"+nFileSize);
                                        if(nIP != null){
                                        	String queryString = _qrl.getQuery();
                                        	QueryResult nQueryResult = new QueryResult(nFileIndex,nIP,nPort,nFileSize,nFileName,queryString);
                                            //int index,InetAddress _IP,int _downloadPort,int _fileSize,String _fileName
                                            //nsl.addServer(nServerInfo);
                                        	_qrl.add(nQueryResult);
                                        }



                                    }
                                    else{ // I'm the one who send ping when I rec ping from others
                                        System.out.println("I'm the one who send query when I rec query from others");
                                        IDRecorder idr = _idList.getRecord(mID);
                                        Socket preSoc = idr.getSocket();
                                        try {
                                            DataOutputStream outToServer = new DataOutputStream(preSoc.getOutputStream());
                                            outToServer.write(data); // send data to the prev node
                                        } catch (IOException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        } 
                                    }
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
