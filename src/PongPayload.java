import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;

/**
 * @param mID2
 * @param port
 * @param iP
 * @param fileNum
 * @param fileSize
 * @throws IOException 
 */


/**
 * @author quake0day
 * @author Tianmiao
 */

public class PongPayload {
	private
		byte[] _port = new byte[2];
		byte[] _IPAdress = new byte[4];
		byte[] _NumOfFiles = new byte[4];
		byte[] _NumOfSize = new byte[4];
		byte[] _payLoad = new byte[14];
		

	public PongPayload(int port, InetAddress IP, int fileNum,
		double fileSize) throws IOException {
		_port = convertInt2Byte(port);
		_IPAdress = convertIP2Byte(IP);
		_NumOfFiles = convertInt2Byte(fileNum);
		_NumOfSize = convertInt2Byte((int)(fileSize/1000));	
	}
		
	public byte[] getPayLoad(){
		System.arraycopy(_NumOfSize, 0, _payLoad, 10, 4);
		System.arraycopy(_NumOfFiles, 0, _payLoad, 6, 4);
		System.arraycopy(_IPAdress, 0, _payLoad, 2, 4);
		System.arraycopy(_port, 2, _payLoad, 0, 2);
		return _payLoad;
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
