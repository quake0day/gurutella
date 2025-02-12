import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.lang.Math;

/**
 * ATTENTION:  THIS class need to check ID Repeat OUTSIDE BEFORE being called!
 *
 */

/**
 * @author quake0day
 * 
 */
public class Query extends Thread {

    /**
     * @param args
     */
    private ConnectionInfoList clients;
    private Socket forbiddenSocket = null;
    private int TTL=7;
    private int Hops=0;
    private int[] _idNum = new int[14];	//ID number
    private byte[] mID = null;
    private MessageIDList _idList;
    private String queryString;
    PrintWriter outServer = null;
    DataOutputStream outToServer = null;
    private boolean isAbleToQuery = true;
    private boolean indexAllFiles = false;
    private ConnectionInfo _cI;
    private InfoParameters _iF;

    public Query(String queryString, ConnectionInfoList client, MessageIDList idList, InfoParameters info){
        this.clients = client;
        this.forbiddenSocket = null;
        this._idList = idList;
        this.queryString = queryString;
        this._iF = info;
    }

    public Query(String queryString, ConnectionInfoList client, Socket forbiddenSocket,int TTL
            , int Hops, MessageIDList idList, byte[] mID, InfoParameters info){
        this.queryString = queryString;
        this.clients = client;
        this.forbiddenSocket = forbiddenSocket;
        this.TTL = TTL;
        this.Hops = Hops;
        this._idList = idList;
        this.mID = mID;
        this._iF = info;
            }

    public Query( ConnectionInfoList client, MessageIDList idList, boolean indexAllFiles, InfoParameters info){
        this.queryString = "    ";
        this.clients = client;
        //this.forbiddenSocket = forbiddenSocket;
       // this.TTL = TTL;
        //this.Hops = Hops;
        this._idList = idList;
        this.indexAllFiles = indexAllFiles;
        this._iF = info;
            }
    public void run(){
        double j = 1;
        for(int i = 0; i < _idNum.length; i++)
        {
            _idNum[i] = (int) (Math.random() * 255.0);
        }
        while(_idList.checkID(_idNum) && j < 4.9E33)	// repeated message ID
        {
            j++;
            for(int i: _idNum)
            {
                _idNum[i] = (int) (Math.random() * 255.0);
            }
        }
        if (j >4.9E33)
        {
            System.out.println("Oops! Message IDs seem to be running out!");
        }

        else {
        	if(mID == null){
            _idList.addRecord(new IDRecorder(_idNum, forbiddenSocket));
        	}
        	else{
                _idList.addRecord(new IDRecorder(mID, forbiddenSocket));
        	}
            byte[] query = null;
            Iterator<ConnectionInfo> iter =clients.iterator();
            //byte[] id, byte type, byte ttl, byte hops, byte[] plength, byte[] payload
            //byte[] mID = new byte[16];
            //byte[] newPacketLength={0x00,0x00,0x00,0x00};
            //byte ttl;
            //byte hops;
            //byte[] payload = null;
            //mID[8] = (byte)0xff;
            //mID[15] = (byte)0x00;
            //byte mtype = (byte)0x00; // ping message
            //ttl = new Integer(TTL).byteValue();
            //hops = new Integer(Hops).byteValue(); Suggest to not use, SEE below
            int payloadLength = queryString.length()+3;
            if(payloadLength > MyConstants.MAX_PAYLOAD_LENGTH || payloadLength > MyConstants.MAX_QUERY_LENGTH){
                System.out.println("You query string is too long");
                System.out.println("Try to enter another one");
                isAbleToQuery = false;
            }
            if(isAbleToQuery){
                byte[] payload = new byte[payloadLength];
                byte[] minSpeed = {0x00,0x00};
                byte[] bQueryString = queryString.getBytes();
                System.arraycopy(minSpeed,0,payload,0,2);
                System.arraycopy(bQueryString,0,payload,2,bQueryString.length);
                payload[payloadLength-1] = '\0';
                //System.arraycopy(src, srcPos, dest, destPos, length)
                MessageContainer queryContainer = null;
                if(mID == null){
                    queryContainer = new MessageContainer();//use in this way
                queryContainer.setID(_idNum);							//would be more readable
                }
                else{
                    queryContainer = new MessageContainer(mID);//use in this way

                }
               if(indexAllFiles == true){
                   queryContainer.setTTL(1);								//MessageContainer.java
                   queryContainer.setHops(0);
               }
               else if(indexAllFiles == false){
                queryContainer.setTTL(TTL);								//MessageContainer.java
                queryContainer.setHops(Hops);
               }
                queryContainer.setPayloadLength(payloadLength);						//see comment in 
                queryContainer.setType(3);
                queryContainer.addPayLoad(payload);


                query = queryContainer.convertToByte();						
                while(iter.hasNext()){			
                	_cI = iter.next();
                	_iF.add(_cI);
                    Socket clientSocket = _cI.getSocket();					
                    if(!clientSocket.equals(forbiddenSocket)){
                        try {
                            outToServer = new DataOutputStream(clientSocket.getOutputStream());
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            //e.printStackTrace();
                            // This could happen when the other side's socket is closed
                            System.out.println("Error when sending ping in iterator");
                        }

                        // send Ping
                        try {
                            outToServer.write(query);
                            _cI.addbO(query.length);
                            _cI.addPO();
                            _cI.addQN();
                            /*
                            System.out.print("sent a QueryID: ");	//for test use
                            for (int i: _idNum)
                            {
                                System.out.print(i + "\t");
                            }
                            System.out.println();
							*/

                            //outToServer.writeChars("\r\n");
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            //e.printStackTrace();
                        }
                        //outServer.close();
                    }
                }
            }
        }
    }
}
