import java.util.ArrayList;
import java.util.Hashtable;
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
	private ArrayList<Integer> qrl;// list is some List of Strings
	private String queryString;
	private Hashtable<Integer, QueryResult> resTable;
	public QueryResultList() {
		// TODO Auto-generated constructor stub
		qrl = new ArrayList<Integer>();
		resTable = new Hashtable<Integer, QueryResult>();
		queryString = null;
	}
	public synchronized void setQuery(String query){
		this.queryString = query;
	}
	public  String getQuery(){
		return this.queryString;
	}

	
	public void add(QueryResult e){	
		if(qrl.contains(e.ID)){
			qrl.remove(qrl.indexOf(e.ID));
		}
		qrl.add(e.ID);
		resTable.put(e.ID, e);
	}
	public Iterator<Integer> getItertor(){
		return qrl.iterator();
}

	public QueryResult getResult(int ID){
		return resTable.get(ID);
	}
	
	public void clearAll(){
		qrl.clear();
		resTable = new Hashtable<Integer, QueryResult>();
	}
	
	public void clear(int fileNO){
		if(fileNO >= 1){
		//fileNO = qrl.size() - fileNO;
		int i = qrl.get(fileNO-1);
		qrl.remove(fileNO-1);
		resTable.remove(i);
		}
	}
	public int length(){
		return qrl.size();
	}
	/**
	 * @param num
	 * @return 
	 */
	public QueryResult getDownload(int fileNO) {
		// TODO Auto-generated method stub
		if(fileNO >= 1){
		//fileNO = qrl.size() - fileNO;
		int i = qrl.get(fileNO-1);
		return resTable.get(i);
		}
		return null;
	}
}
