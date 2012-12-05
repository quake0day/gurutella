
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*; 


//import com.sun.corba.se.impl.orbutil.closure.Future;

public class Tcpserver extends Thread
{ 
    //protected Socket clientSocket;
    private ServerSocket _serverSK;
    //private ServerSocket _uploadServer;
    public static int count = 0;
    public Socket[] socketArray;
    //public ArrayList<Socket> clients = null;
    private ConnectionInfoList clients;
    private MessageIDList _routingTable;
    private FileInfoList _fileList;
    private NetworkServerList _nsl;
    private MonitorNetwork _mnl;
    private InetAddress IP;
    private QueryResultList _qrl;
    private int port;
    private int _downPort;
    private GUID _k;
    private ExecutorService threadPool = Executors.newFixedThreadPool(MyConstants.MAX_THREAD_NUM);

    public Tcpserver (ServerSocket soc, int port, int dPort, ConnectionInfoList client,MessageIDList rt,InetAddress IP,FileInfoList filelist,MonitorNetwork mnl,QueryResultList qrl,NetworkServerList nsl,GUID k) throws IOException, InterruptedException
    {
        //set the max size of socket pool
        this.IP = IP;
        this.clients = client;
        this._routingTable = rt;
        this._fileList = filelist;
        this._qrl = qrl;
        this._nsl = nsl;
        this._mnl = mnl;
        this.port = port;
        this._downPort = dPort;
        this._k = k;
        socketArray = new Socket[MyConstants.MAX_THREAD_NUM];
        _serverSK = soc;
    }

    public void run()
    {
        //boolean isAlive = true;
        //PrintWriter outServer = null;
        Socket listenSocket = null;
        //Socket uploadSocket = null;
        //Create a server socket for every accepted connection

        while(true)
        {
            try {
                listenSocket = _serverSK.accept();
                threadPool.submit(new ServerHandler(listenSocket, clients,
                            _routingTable, port, _downPort, IP, _fileList,_mnl,_qrl,_nsl,_k));
                System.out.println(listenSocket.getInetAddress());
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
}

