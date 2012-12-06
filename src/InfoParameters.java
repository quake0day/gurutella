import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 */

/**
 * @author Administrator
 *
 */
public class InfoParameters {
	private ArrayList<ConnectionInfo> _cIPL;
			ConnectionInfoList _cIL;
			int _msgIn = 0;
			int _msgOut = 0;
			
	public InfoParameters(ConnectionInfoList cil)
	{
		this._cIL = cil;
	}
	
	public synchronized void add(ConnectionInfo ci)
	{
		_cIPL.add(ci);
	}
	
	public int getMI()
	{
		return _msgIn;
	}
	
	public int getMO()
	{
		return _msgOut;
	}
	
	public Iterator<ConnectionInfo> getCurrIter()
	{
		return _cIL.iterator();
	}
	
	public Iterator<ConnectionInfo> getTotlIter()
	{
		return _cIPL.iterator();
	}
}
