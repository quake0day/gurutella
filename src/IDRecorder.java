/**
 * 
 */
import java.net.Socket;
/**
 * @author Tianmiao
 *
 */
public class IDRecorder {	//record single ID & Socket, called by MessageIDList
    private byte[] _id;	//16 Bytes Full ID
    private Socket _soc;

    public IDRecorder(byte[] id, Socket soc)
    {
        if (id.length != 16) 
        {
            _id = null;
            _soc = null;
            return;
        }
        else
        {
            _id = id; 
            _soc = soc;
        }
    }

    public IDRecorder(int[] id, Socket soc)
    {	
        if (id.length != 16) 
        {
            _id = null;
            _soc = null;
            return;
        }
        else
        {
            byte[] idNo = new byte[id.length];
            for(int i = 0; i < id.length; i++)	
            {
                if (id[i] > 255 || id[i] < 0)	//Number range legality check
                {
                    _id = null;
                    _soc = null;
                    return;
                }
                else
                {
                    idNo[i] = new Integer(id[i]).byteValue();	
                }
            }
            _id = idNo;
            _soc = soc;
        }
    }

    public byte[] getIDByte()
    {
        if (isValid())
            return _id;
        else
        {
            System.out.println("null ID line found in ID List!");
            return null;
        }
    }

    public int[] getIDInt()
    {
        if (isValid())
        {
            int[] id = new int[16];
            for (int i = 0; i < 16; i ++)
            {
                id[i] = (int) (_id[0] & 0xFF);
            }
            return id;
        }
        else
        {
            System.out.println("null ID line found in ID List!");
            return null;
        }
    }	

    public Socket getSocket()
    {
        if (isValid())
        {
            return _soc;
        }
        else
        {
            System.out.println("null ID line found in ID List!");
            return null;
        }
    }

    public boolean isValid()
    {
        if (_id == null || _soc == null)
            return false;
        else
            return true;
    }
}
