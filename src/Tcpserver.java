
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
    public Socket[] socketArray;
    //public ArrayList<Socket> clients = null;
    private ConnectionInfoList clients;
    private MessageIDList _routingTable;
    private FileInfoList _fileList;
    private MonitorNetwork _mnl;
    private InetAddress IP;
    private QueryResultList _qrl;
    private int port;
    private int _downPort;
    private ExecutorService threadPool = Executors.newFixedThreadPool(MyConstants.MAX_THREAD_NUM);

    public Tcpserver (int tcpport,int tcpDown, ConnectionInfoList client,MessageIDList rt,InetAddress IP,FileInfoList filelist,MonitorNetwork _mnl,QueryResultList qrl) throws IOException, InterruptedException
    {
        //set the max size of socket pool
        this.port = tcpport;
        this._downPort = tcpDown;
        this.IP = IP;
        this.clients = client;
        this._routingTable = rt;
        this._fileList = filelist;
        socketArray = new Socket[MyConstants.MAX_THREAD_NUM];
        try{
            _serverSK = new ServerSocket(port);   
        } catch(Exception e){
            System.out.println("You're using a port that cannot Establish TCP connection, program halt...");
            System.exit(1);
        }
    }

    public void run()
    {
        //boolean isAlive = true;
        //PrintWriter outServer = null;
        Socket listenSocket = null;
        //Create a server socket for every accepted connection

        while(true)
        {
            try {
                listenSocket = _serverSK.accept();
                threadPool.submit(new ServerHandler(listenSocket, clients,
                            _routingTable, port, _downPort, IP, _fileList,_mnl,_qrl));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
}

