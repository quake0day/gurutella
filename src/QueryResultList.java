import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 */

/**
 * @author quake0day
 * @param <iterator>
 *
 */
public class QueryResultList<iterator> {

	/**
	 * 
	 */
	private ArrayList<QueryResult> qrl;
	public QueryResultList() {
		// TODO Auto-generated constructor stub
		qrl = new ArrayList<QueryResult>();
		
	}
	
	public void add(QueryResult e){
		qrl.add(e);
	}
	public Iterator<QueryResult> getItertor(){
		return qrl.iterator();
	}

	public void clearAll(){
		qrl.clear();
	}
	public void clear(int fileNO){
		if(fileNO >= 1){
		qrl.remove(fileNO-1);
		}
	}
	public int length(){
		return qrl.size();
	}
}
