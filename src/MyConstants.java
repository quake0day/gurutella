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
	public final static String STATUS_200 = "SIMPELLA/0.6 200 OK\r\n";
	public final static String STATUS_200_REC = "SIMPELLA/0.6 200 OK";
	public final static String STATUS_503 = "SIMPELLA/0.6 503 Maximum number of connections reached. Sorry!\r\n";
	public final static String STATUS_503_REC = "SIMPELLA/0.6 503 Maximum number of connections reached. Sorry!";
	public final static String localIPNum="127.0.0.1";
	public final static String localHost="localhost";
	public final static String connectFail = "cannot connect to this Host. Connection refused";

}
