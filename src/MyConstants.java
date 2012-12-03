/**
 * 
 */

/**
 * @author quake0day
 *
 */
public interface MyConstants {
    public final static int MAX_THREAD_NUM = 9;
    public final static int MAX_INCOMING_CONNECTION_NUM = 3;
    public final static int MAX_OUTGOING_CONNECTION_NUM = 3;
    public final static int MAX_PAYLOAD_LENGTH = 4096; //bytes
    public final static int MAX_QUERY_LENGTH = 256; //bytes
    public final static int HEADER_LENGTH = 23; //bytes
    public final static String STATUS_200 = "SIMPELLA/0.6 200 OK\r\n";
    public final static String STATUS_200_REC = "SIMPELLA/0.6 200 OK";
    public final static String STATUS_503 = "SIMPELLA/0.6 503 Maximum number of connections reached. Sorry!\r\n";
    public final static String STATUS_503_REC = "SIMPELLA/0.6 503 Maximum number of connections reached. Sorry!";
    public final static String STATUS_OUTGOING_REACHED_LIMIT = "Maximum number of connections reached. Cannot open new connection. Sorry!";
    public final static String localIPNum="127.0.0.1";
    public final static String localHost="localhost";
    public final static String connectFail = "cannot connect to this Host. Connection refused";

}
