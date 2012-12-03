

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

public class Connect extends Thread{

    private String targetIPAddress;
    private int tcpport;
    private Socket newEstablishedSocket = null;
    private ConnectionInfoList _cInfo;
    private MessageIDList _idList;
    private NetworkServerList nsl;
    private QueryResultList qrl;
    private InetAddress _IP; 
    private FileInfoList _fList;
    private MonitorNetwork _mnl;
    private int _downPort;

    public Connect (String targetIPAddressr, String tcp, int tcpDownload, ConnectionInfoList cInfo,MessageIDList idList,NetworkServerList nsl, QueryResultList qrl,InetAddress IP, FileInfoList fList, MonitorNetwork mnl) throws IOException{
        targetIPAddress = targetIPAddressr;
        tcpport = Integer.parseInt(tcp);
        this._cInfo = cInfo;
        this._idList = idList;
        this.nsl = nsl;
        this.qrl = qrl;
        this._IP = IP;
        this._fList = fList;
        this._downPort = tcpDownload;
        this._mnl = mnl;
        /*    public ServerHandler(Socket serverSoc, ConnectionInfoList cInfo, MessageIDList idList,
            int tcpPort, int tcpDownload, InetAddress IP, FileInfoList fList, MonitorNetwork mnl)
    {
        _cInfo = cInfo;
        _serverSocThread = serverSoc;
        _idList = idList;
        _fList = fList;
        _port = tcpPort;
        _mnl = mnl;
        _downPort = tcpDownload;
        _IP = IP;
        _tempClientIndex = _cInfo.size();
        //Socket listenSocket = clients.get(1,tempClientIndex);
    }
    */
        //ExecutorService threadPool = Executors.newFixedThreadPool(MyConstants.MAX_THREAD_NUM);
        boolean isAbleToConnect = true;
        InetAddress addr = null;
        String localIPAddr = null;


        addr = InetAddress.getLocalHost();
        localIPAddr = addr.getHostAddress().toString();
        if(targetIPAddress.toString().equals(MyConstants.localIPNum) 
                || targetIPAddress.toString().equals(MyConstants.localHost) 
                || targetIPAddress.toString().equals(localIPAddr)) {
            isAbleToConnect = true;
                }
        // judge hostname
        try {
            InetAddress.getByName(targetIPAddress);					
        } catch (UnknownHostException e1) {
            System.out.println("Enter valid host name");
            isAbleToConnect = false;
        }

        Iterator<ConnectionInfo> iter = _cInfo.iterator();
        //  duplicate?
        while(iter.hasNext()){
            if(targetIPAddress.equals(iter.next().getIP().toString().split("/")[1])){
                isAbleToConnect = false;
            }
        }
        if(isAbleToConnect){
            try {
                //threadPool.submit(new Connect(new Socket(targetIPAddress,tcpport),clients,routingTable));
                newEstablishedSocket = new Socket(targetIPAddress,tcpport);
                System.out.println("Trying to connect "+targetIPAddress+": "+tcpport +" ...");
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                System.out.println("cannot connect to this Host. Connection refused");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("cannot connect to this Host. Connection refused");
            }
        }
        else{
            System.out.println("Cannot connect to itself and it may have duplicate connection.");
        }


    }
    /*public Connect(Socket socket,ConnectionInfoList clients, MessageIDList routingTable) {
    // TODO Auto-generated constructor stub
    this.clients = clients;
    newEstablishedSocket = socket;
    tempClientIndex = clients.size(0) - 1;
    System.out.println("connect thread pool of thread");
    }*/

    public boolean checkMessagePacketValidation(byte[] data,int MessageLength){
        // check if length is larger than 22
        if(MessageLength < 22){
            return false;
        }
        return true;

    }

    public int byte2int(byte[] i){
        ByteBuffer bc = ByteBuffer.wrap(i);
        return bc.getInt();
    }

    public void run(){
        //BufferedReader in = null;
        PrintWriter outServer = null;
        boolean isAlive = true;
        try {
            outServer = new PrintWriter(newEstablishedSocket.getOutputStream(), 
                    true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Do handshake
        outServer.println("SIMPELLA CONNECT/0.6\r\n");
        InputStream stream = null;
        try {
            stream = newEstablishedSocket.getInputStream();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        while(isAlive){
            byte[] header = new byte[MyConstants.HEADER_LENGTH];
            int messageLength=0;
            try {
                messageLength = stream.read(header);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            if (messageLength == -1){ // means a broken socket
                _cInfo.remove(0, newEstablishedSocket);
                isAlive = false;
                break;
            }

            System.out.println("client received: " + messageLength);

            String recResult = new String(header);

            if (recResult.trim().equals(MyConstants.STATUS_200_REC)){
                // Print out <string>
                System.out.println(recResult.split("200 ")[1]);

                _cInfo.addConnection(new ConnectionInfo(tcpport, newEstablishedSocket));
                System.out.println("Connection established!");
                Thread update = new Update(_cInfo, _idList);
                update.start();
                //System.out.println(clients.size(0));
            }
            else if (recResult.trim().equals(MyConstants.STATUS_503_REC)){
                // Print out <string>
                System.out.println(recResult.split("503 ")[1]);
                isAlive = false;
                break;
            }
            else{
                //System.out.println(res);
                // regular message, judge message type
                if(checkMessagePacketValidation(header,messageLength)){
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
						 stream.read(data);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                    if((int)(TTL+Hops) >=0 && (int)(TTL+Hops) <= 15) {
                        if(messageType == (byte)0x00){
                            System.out.println("toclient PING");
                        }
                        else if(messageType == (byte)0x01){
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
                                    nsl.addServer(nServerInfo);
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
                        else if(messageType == (byte)0x80){
                            System.out.println("QUERY");
                            System.out.println("QUERY MESSAGE");
                            boolean hasSameMessageID = false;
                            hasSameMessageID = _idList.checkID(mID);
                           // byte[] payload = new byte[4096];
                            byte[] minimumSpeed = new byte[2];
                            //int payloadLength = 0;
                            /*
							try {
								payloadLength = stream.read(payload);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							*/
                            byte[] queryString = new byte [pLength-2];
                            System.out.println("PayloadLength:"+pLength);

                            System.arraycopy(data, 0, minimumSpeed, 0, 2);
                            System.arraycopy(data, 2, queryString, 0, queryString.length);
                            //ByteBuffer bb = ByteBuffer.wrap(minimumSpeed);
                            //IntBuffer ib = bb.asIntBuffer();
                            //int nMinSpeed = ib.get(0);
                            // !! very important trim the last \0
                            byte[] queryStringtrim = new byte[queryString.length-1];
                            System.arraycopy(queryString, 0, queryStringtrim, 0, queryString.length-1);

                            String nQueryString = new String(queryStringtrim);
                            System.out.println("Query:"+nQueryString);
                            // add Query to Monitor the whole network
                            _mnl.saveQuery(nQueryString);

                            
                            //ByteBuffer bc = ByteBuffer.wrap(queryString);

                            //String queryString = 
                            if (hasSameMessageID == false){
                                _idList.addRecord(new IDRecorder(mID, newEstablishedSocket));

                                Query sendNext = new Query(nQueryString,_cInfo,newEstablishedSocket
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
                                    QueryHitPayLoad queryPayLoad = null;
									try {
										queryPayLoad = new QueryHitPayLoad(_NumberOfHits,_downPort,_IP,Speed,qrs, serventID);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
                                    //PongPayload payload = new PongPayload(_port, _IP, _fList.getFileNum(), _fList.getFileSize());
                                    try {
										queryHitContainer.addPayLoad(queryPayLoad.getPayLoad());
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
                                    queryHitContainer.setPayloadLength(queryPayLoad.getPayloadLength());
                                    byte [] queryHit = new byte[MyConstants.MAX_PAYLOAD_LENGTH];
                                    queryHit = queryHitContainer.convertToByte();
                                    DataOutputStream outToServer = null;
									try {
										outToServer = new DataOutputStream(newEstablishedSocket.getOutputStream());
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
                                    try {
										outToServer.write(queryHit);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
                                    try {
										outToServer.flush();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

                                }
                            }	
                        }

                        else if(messageType == (byte)0x81){
                            System.out.println("QUERYHIT");
                           // while(pLength+23 <= messageLength){ // means more than one packets in the queue

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
                                    String nServentID = new String(serventID);
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
                                    	String queryString = qrl.getQuery();
                                    	QueryResult nQueryResult = new QueryResult(nFileIndex,nIP,nPort,nFileSize,nFileName,queryString);
                                        //int index,InetAddress _IP,int _downloadPort,int _fileSize,String _fileName
                                        //nsl.addServer(nServerInfo);
                                    	qrl.add(nQueryResult);
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

                                /*
                                if(pLength+23 < messageLength){
                                    payloadLen = new byte [4];
                                    messageLength = messageLength - pLength - 23;
                                    System.arraycopy(data,19+pLength+23,payloadLen,2,2);
                                    int pLength_new = byte2int(payloadLen);
                                    pLength = pLength_new;

                                }
                                else if(pLength+23 == messageLength){
                                    pLength = pLength+24;
                                }
                                

                            }
*/
                        }
                    }


                }
            }


        }

    }

}

