import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 */

/**
 * @author quake0day
 *
 */
public class MonitorNetwork {

	/**
	 * 
	 */
	private ArrayList<String> _querySet;
	public MonitorNetwork() {
		// TODO Auto-generated constructor stub
		_querySet = new ArrayList<String>();
	}
	
	public synchronized void saveQuery(String query){
		this._querySet.add(query);
	}
	
	public synchronized ArrayList<String> getQuerySet(){
		return this._querySet;
	}
	public synchronized Iterator<String> getIterator(){
		return this._querySet.iterator();
	}

}
