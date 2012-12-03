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

//TAKE THIS CLASS AS OMNI-MESSAGE CONTAINER WITH its methods to define various messages

public class MessageContainer {	
    private
        byte[] mID = new byte[16];
    byte mType = (byte)0x00;
    byte TTL;
    byte Hops;
    byte[] pLength = new byte[4];
    byte[] payload = null;

    public MessageContainer() {
        //Constructor1
        //Message format initialization
        // TODO: other bytes should have a highly random values
        byte[] newPacketLength={0x00,0x00,0x00,0x00};
        mID[8] = (byte)0xff;
        mID[15] = (byte)0x00;
        TTL = new Integer(7).byteValue();
        Hops = (byte)0x00;
        pLength = newPacketLength;
    }

    public MessageContainer(byte[] id)
    {
        byte[] newPacketLength={0x00,0x00,0x00,0x00};
        this.mID = id;
        mID[8] = (byte)0xff;
        mID[15] = (byte)0x00;
        TTL = new Integer(7).byteValue();
        Hops = (byte)0x00;
        pLength = newPacketLength;
    }

    public MessageContainer(byte[] id, byte type, byte ttl, byte hops, byte[] plength, byte[] payload)
    {
        //Constructor2, for convenience when RECEIVING data from InputStream
        //Message format initialization

        TTL = new Integer(7).byteValue();
        this.mID = id;		
        mID[8] = (byte)0xff;
        mID[15] = (byte)0x00;
        this.mType = type;
        this.TTL = ttl;
        this.Hops = hops;
        this.pLength = plength;
        this.payload = payload;
    }

    public byte[] convertToByte(){	//used after packaging
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

    public boolean setID(int[] num)	//used when random assignment
    {														//for sender
        if (num.length != 14)	//ID length legality check
        {
            return false;
        }
        else
        {
            for(int i = 0, j = 0; i < num.length; i++, j++)	
            {
                mID[j] = new Integer(num[i]).byteValue();
                if (j == 7)
                {
                    j++;
                }
            }
            return true;
        }
    }

    public boolean getID(MessageIDList idList)	//for receiver, read to UI
    {
        if (!idList.checkID(mID))
        {
            return false;
        }
        else
        {
            return true;
        }
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

    public boolean setPayloadLength(int pl)	//big-endian format
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
            pLength[3] = (byte)0x00;
            pLength[2] = (byte)0x00;
            pLength[1] = new Integer(lowBytes).byteValue();
            pLength[0] = new Integer(highBytes).byteValue();
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
