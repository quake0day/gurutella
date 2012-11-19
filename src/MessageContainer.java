/**
 * Message contianer that satisfies Simpella0.6 format
 * Includes MessageID| MessageType| TTL| Hops| PayloadLength| Payload
 * With accessor and mutator methods
 */

/**
 * Wanted:	1.Digit number check before using 
 */

/**
 * @author Tianmiao
 *
 */

public class MessageContainer {	
	private
		byte[] mID = new byte[16];
		byte mType;
		byte TTL;
		byte Hops;
		byte[] pLength = new byte[4];
		byte[] payload = null;
	
	public MessageContainer() {
		//Constructor1
		//Message format initialization
		mID[8] = (byte)0xff;
		mID[15] = (byte)0x00;
		TTL = new Integer(7).byteValue();
		Hops = (byte)0x00;
	}
	
	public MessageContainer(byte[] id, byte type, byte ttl, byte hops, byte[] plength, byte[] payload)
	{
		//Constructor2, for convenience when RECEIVING data
		//Message format initialization
		mID[8] = (byte)0xff;
		mID[15] = (byte)0x00;
		TTL = new Integer(7).byteValue();

		this.mID = id;
		this.mType = type;
		this.TTL = ttl;
		this.Hops = hops;
		this.pLength = plength;
		this.payload = payload;
	}
	
	public boolean setID(int[] num)
	{
		if (num.length != 14)	//ID length legality check
		{
			return false;
		}
		else
		{
			for(int i = 0, j = 0; i < num.length; i++, j++)	
			{
				if (num[i] > 255 || num[i] < 0)	//Number range legality check
				{
					return false;
				}
				else
				{
					mID[j] = new Integer(num[i]).byteValue();
					if (j == 7)
					{
						j++;
					}
				}
			}	
			return true;
		}
	}
	
	public int[] getID()
	{
		int[] num = new int[mID.length - 2];
		for(int i = 0, j = 0; i < mID.length - 1; i++, j++)	
		{
				num[j] = mID[i] & 0xFF;	//Get unsigned integer
				if (i == 7)
				{
					i++;
				}
		}
		return num;
	}
	
	public boolean setType(int type) //1.Ping 2.Pong 3.Query 4.Query Hit
	{
		switch(type) {
		case 1: mType = (byte)0x00; return true;
		case 2: mType = (byte)0x01; return true;
		case 3: mType = (byte)0x80; return true;
		case 4: mType = (byte)0x81; return true;
		default: return false;
		}
	}
	
	public int getType()
	{
		if (mType == (byte)0x00)	//Ping
			return 1;
		else if (mType == (byte)0x01)	//Pong
			return 2;
		else if (mType == (byte)0x80)	//Query
			return 3;
		else if (mType == (byte)0x81)	//Query Hit
			return 4;
		else
			return 0;
	}
	
	public boolean setTTL(int ttl)
	{
		if (ttl > 7 || ttl < 0)
		{
			return false;
		}
		else
		{
			TTL = new Integer(ttl).byteValue();
			return true;
		}
	}
	
	public boolean decrementTTL()
	{
		int temp = new Byte(TTL).intValue();
		if (temp > 0)
		{
			temp --;
			TTL = new Integer(temp).byteValue();
			return true;
		}
		else
			return false;
	}
	
	public int getTTL()
	{
		return (TTL & 0xFF);	//Get unsigned integer
	}
	
	public boolean setHops(int hop)
	{
		if (hop > 7 || hop < 0)
		{
			return false;
		}
		else
		{
			Hops = new Integer(hop).byteValue();
			return true;
		}
	}
	
	public boolean incrementHops()
	{
		int temp = new Byte(Hops).intValue();
		if (temp < 7)
		{
			temp++;
			Hops = new Integer(temp).byteValue();
			return true;
		}
		else
			return false;
	}
	
	public int getHops()
	{
		return (Hops & 0xFF);
	}
	
	public boolean setPayloadLength(int pl)
	{
		if (pl > 4096)
		{
			return false;
		}
		else
		{
			int highBytes = 0;
			int lowBytes = 0;
			if ((pl - 255) > 0)
			{
				highBytes = pl - 255;
				lowBytes = 255;
			}
			else
			{
				lowBytes = pl;
			}
			pLength[0] = new Integer(lowBytes).byteValue();
			pLength[1] = new Integer(highBytes).byteValue();
			pLength[2] = (byte)0x00;
			pLength[3] = (byte)0x00;
			return true;
		}
	}
	
	public int getPayloadLength()
	{
		int length;
		if ((pLength[1] & 0xFF) == 0)
			length = pLength[0] & 0xFF;
		else
			length = (pLength[1] & 0xFF) * 256 + (pLength[0] & 0xFF);
		return length;
	}
	
	public boolean addPayLoad(byte[] data)
	{
		if (data != null)
		{
			payload = data;
			return true;
		}
		else
			return false;
	}
	
	public byte[] getPayLoad()
	{
		return payload;
	}
	
}
