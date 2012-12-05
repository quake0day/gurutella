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
	public synchronized String getQuery(){
		return this.queryString;
	}

	
	public synchronized void add(QueryResult e){	
		if(qrl.contains(e.ID)){
			qrl.remove(qrl.indexOf(e.ID));
		}
		qrl.add(e.ID);
		resTable.put(e.ID, e);
	}
	public synchronized Iterator<Integer> getItertor(){
		return qrl.iterator();
}

	public synchronized QueryResult getResult(int ID){
		return resTable.get(ID);
	}
	
	public synchronized void clearAll(){
		qrl.clear();
		resTable = new Hashtable<Integer, QueryResult>();
	}

	public synchronized void clear(int fileNO){
		if(fileNO >= 1){
		//fileNO = qrl.size() - fileNO;
		int i = qrl.get(fileNO-1);
		qrl.remove(fileNO-1);
		resTable.remove(i);
		}
	}
	public synchronized void clearNull(){
		try{
		for(int i  : qrl){
			if(resTable.get(i).queryString == null){
				resTable.remove(i);
				qrl.remove(i);
			}
		}
		}catch (Exception e){
			
		}
	}
	public synchronized int length(){
		return qrl.size();
	}
	/**
	 * @param num
	 * @return 
	 */
	public synchronized QueryResult getDownload(int fileNO) {
		// TODO Auto-generated method stub
		if(fileNO >= 1){
		//fileNO = qrl.size() - fileNO;
		int i = qrl.get(fileNO-1);
		return resTable.get(i);
		}
		return null;
	}
}
