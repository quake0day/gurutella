import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 */

/**
 * @author Tianmiao
 * 
 *
 */
public class QueryHitPayLoad {
	private static final AbstractList<QueryResultSet> Iter_qrs = null;
	private
	byte[] _NumOfHit = new byte[4];
	byte[] _port = new byte[4];
	byte[] _IPAdress = new byte[4];
	byte[] _Speed = new byte[4];
	//byte[] _NumOfSize = new byte[4];
	byte[] _serventID = new byte[16];
	private QueryResultSet _qrs;
	private byte[] _payload;
	
	public QueryHitPayLoad(int numberOfHits, int port, InetAddress IP, int Speed,QueryResultSet qrs, String serventID) throws IOException {
		// TODO Auto-generated constructor stub
		this._NumOfHit = convertInt2Byte(numberOfHits);
		this._port = convertInt2Byte(port);
		this._IPAdress = convertIP2Byte(IP);
		this._Speed = convertInt2Byte(Speed);
		this._serventID = serventID.getBytes();
		this._qrs = qrs;
	
	}
	
	public byte[] getPayLoad() throws IOException{
		byte[] bQrs = _qrs.convert2Byte();
		int qrsLength = bQrs.length;
		_payload = new byte[qrsLength+16+4+4+2+1];
		System.arraycopy(_NumOfHit, 3, _payload, 0, 1);
		System.arraycopy(_port, 2, _payload, 1, 2);
		System.arraycopy(_IPAdress, 0, _payload, 3, 4);
		System.arraycopy(_Speed, 0, _payload, 7, 4);
		System.arraycopy(bQrs, 0, _payload, 11, qrsLength);
		System.arraycopy(_serventID, 0, _payload, qrsLength+11, 16);
		return _payload;
	}
	
	public int getPayloadLength(){
		return _payload.length;
	}

	private byte[] convertIP2Byte(InetAddress IP) throws NumberFormatException, IOException{
		byte[] b = new byte[4];
		b = IP.getAddress();
		InetAddress IPc = InetAddress.getByAddress(b);
		System.out.println(IPc.getHostAddress());
		return b;
	}
	
	private byte[] convertInt2Byte(int i) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(i);
        byte[] b = baos.toByteArray();
        return b;
	}
	
	private byte[] convertShort2Byte(int i) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeShort(i);
        byte[] b = baos.toByteArray();
        return b;
	}
	
	private byte[] convertDouble2Byte(double i) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeDouble(i);
        byte[] b = baos.toByteArray();
        return b;
	}

}
