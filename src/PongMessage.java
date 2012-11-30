import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;

/**
 * 
 */

/**
 * @author quake0day
 *
 */
public class PongMessage extends MessageContainer{

	/**
	 * 
	 */
	private
		byte[] mID = new byte[16];
		byte mType = (byte)0x01;
		byte TTL;
		byte Hops;
		byte[] pLength = new byte[4];
		byte[] payload = null;
		
	public PongMessage() {
		// TODO Auto-generated constructor stub

	}
	

	/**
	 * @param mID2
	 * @param port
	 * @param iP
	 * @param fileNum
	 * @param fileSize
	 * @throws IOException 
	 */
	public PongMessage(byte[] mID2, int port, InetAddress IP, int fileNum,
			double fileSize) throws IOException {
		// TODO Auto-generated constructor stub
		byte[] newPacketLength={0x00,0x00,0x00,0x14};
		byte[] payload = new byte [14];
		mID = mID2;
		TTL = new Integer(7).byteValue();
		Hops = (byte)0x00;
		pLength = newPacketLength;
		payload = addPortToPayload(port,payload);
		payload = addIPaddressToPayload(IP,payload);
		payload  = addFileNumToPayload(fileNum,payload);
		payload = addFileSizeToPayload(fileSize,payload);
		
	}
	public byte[] convertInt2Byte(int i) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(i);
        byte[] b = baos.toByteArray();
        return b;
	}
	public byte[] convertDouble2Byte(double i) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeDouble(i);
        byte[] b = baos.toByteArray();
        return b;
	}
	public byte[] addPortToPayload(int port,byte[] payload) throws IOException{
		// convert in
		byte bPort[] = new byte[2];
		bPort = convertInt2Byte(port);
		System.arraycopy(bPort, 0, payload, 0, 2);
		return payload;
	}
	public byte[] addIPaddressToPayload(InetAddress IP, byte[] payload){
		byte bIP[] = new byte[4];
		bIP = IP.toString().getBytes();
		System.arraycopy(bIP, 0, payload, 2, 4);
		return payload;
	}
	public byte[] addFileNumToPayload(int fileNum, byte[] payload) throws IOException{
		byte bFileNum[] = new byte[4];
		bFileNum = convertInt2Byte(fileNum);
		System.arraycopy(bFileNum, 0, payload, 6, 4);
		return payload;
	}
	
	public byte[] addFileSizeToPayload(double fileSize, byte[] payload) throws IOException{
		byte bFileSize[] = new byte[4];
		bFileSize = convertDouble2Byte(fileSize);
		System.arraycopy(bFileSize, 0, payload, 10, 4);
		return payload;
	}
	public byte[] convertToByte(){
		int messageLength = 23;
		if(payload != null){
			messageLength = messageLength + payload.length;
		}
		byte[] Message = new byte[messageLength];
		System.arraycopy(mID, 0, Message, 0, 16);
		//Message[8] = mID[8]; 
		//Message[15] = mID[15]; 
		Message[16] = mType;
		Message[17] = TTL;
		Message[18] = Hops;
		System.arraycopy(pLength, 0, Message, 19, 4);
		if(payload != null){
			if(payload.length > 0){
				System.arraycopy(payload, 0, Message, 23, payload.length);
			}
		}
		return Message;
	}

}
