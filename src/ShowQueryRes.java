import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 */

/**
 * @author quake0day
 *
 */
public class ShowQueryRes implements Runnable{

	/**
	 * 
	 */
	private QueryResultList qrl;
	private String query;
	public ShowQueryRes(QueryResultList qrl, String queryString) {
		// TODO Auto-generated constructor stub
		this.qrl = qrl;
		this.query = queryString;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		System.out.println("-----------------------------");
		System.out.println("The query was '"+ query+ "'");
		Iterator<QueryResult> qrlIter = qrl.getItertor();
		while(qrlIter.hasNext()){
			int i = 1;
			 QueryResult res = qrlIter.next();
			 System.out.println(i+")"+res._IP+":"+res._downloadPort+" 			Size:"+res._fileSize);
			 System.out.println("Name:"+res._fileName);
			 i++;
		}
		System.out.println("");
		
	}

}
