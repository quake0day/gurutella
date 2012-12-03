import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;



/**
 * 
 */

/**
 * @author quake0day
 * @author Tianmiao
 */
public class simpella /*extends Thread*/{
    // define the max thread number in a thread-pool
    private static int tcpPort1 = 6346;	//Connection
    private static int tcpPort2 = 5635;	//Download
    public static FileInfoList _fileList;
    public static ConnectionInfoList _clients;
    public static MessageIDList _routingTable;
    public static NetworkServerList _networkServerList;
    public static MonitorNetwork _mnl;
    private static InetAddress IP = null;


    /**
     * @param args
     * @throws InterruptedException 
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        // TODO Auto-generated method stub
        //ExecutorService threadPool = Executors.newFixedThreadPool(MyConstants.MAX_THREAD_NUM);		
        if (args.length == 2){
            tcpPort1 = Integer.parseInt(args[0]);
            tcpPort2 = Integer.parseInt(args[1]);
            if(tcpPort1 < 0 && tcpPort1 > 60000 && tcpPort2 < 0 && tcpPort2 > 60000){
                System.err.println("Error when try to listen to port1:"+tcpPort1+" and port2:"+tcpPort2);
                System.exit(1);
            }
            else if (tcpPort1 == tcpPort2){
                System.err.println("Error when try to listen to port1:"+tcpPort1+" and port2:"+tcpPort2);
                System.exit(1);
            }
        }
        else
        {
            System.out.println("Usage: java Echoer <tcp-port> <udp-port>");
        }

        showWelcomeInfo(tcpPort1,tcpPort2);

        _clients = new ConnectionInfoList();
        _fileList = new FileInfoList();
        _routingTable = new MessageIDList();
        _networkServerList = new NetworkServerList();
        _mnl = new MonitorNetwork();
        //TCPServer thread start
        Tcpserver _tcpServer = new Tcpserver(10025, tcpPort2, _clients,_routingTable,IP,_fileList,_mnl);
        _tcpServer.start();
        new Monitor(tcpPort1,tcpPort2,_clients, _fileList,_routingTable,_networkServerList,_mnl,IP);
        //threadPool.submit(new Tcpserver(10025,_clients));
        // download port
        //threadPool.submit(new Tcpserver(tcpPort2,_clients));
    }
    public static void showWelcomeInfo(int tcpPort1,int tcpPort2){
        boolean usePublicDNS = false;
        try {
            DatagramSocket udpSocket = null;
            udpSocket = new DatagramSocket();
            udpSocket.connect(InetAddress.getByName("8.8.8.8"), 53); // Using public DNS google
            IP = udpSocket.getLocalAddress();
            udpSocket.close();
            usePublicDNS = true;
        } catch (SocketException e1) {
            System.out.println("cannot get IP address through 8.8.8.8");
        }
        catch (UnknownHostException e1) {
            System.out.println("cannot get IP address through 8.8.8.8");
        }
        if(!usePublicDNS){ // no internet connection or goolge 8.8.8.8 down
            try {
                IP = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                System.out.println("Cannot obtain local IP address");
            }
        }
        String delimiter = "/";
        String localIP = IP.toString().split(delimiter)[1];
        System.out.println("Local IP:"+localIP);
        System.out.println("Simpella Net Port: "+tcpPort1);
        System.out.println("Downloading Port : "+tcpPort2);
        System.out.println("simpella version 0.6 (c) 2012-2013 CS,DTM");	
        System.out.println(" ");
    }

}
