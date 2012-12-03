import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 
 */

/**
 * @author quake0day
 *
 */
public class QueryResultList {

	/**
	 * 
	 */
	//private ArrayList<QueryResult> qrl;
	private Set<QueryResult> qrl;// list is some List of Strings
	private String queryString;
	public QueryResultList() {
		// TODO Auto-generated constructor stub
		qrl = new LinkedHashSet<QueryResult>();
		queryString = null;
	}
	public synchronized void setQuery(String query){
		this.queryString = query;
	}
	public  String getQuery(){
		return this.queryString;
	}
	public int getSize(){
		return qrl.size();
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
