import java.util.ArrayList;
import java.util.HashSet;
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
	private boolean control;
	public ShowQueryRes(QueryResultList qrl, String queryString, boolean control) {
		// TODO Auto-generated constructor stub
		this.qrl = qrl;
		this.query = queryString;
		this.control = control;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(control == false){
		System.out.println("");
		System.out.println("-----------------------------");
		System.out.println("The query was '"+ query+ "'");
		Iterator<Integer> qrlIter = qrl.getItertor();
		int i = 1;
		while(qrlIter.hasNext()){
			 Integer resID = qrlIter.next();
			 QueryResult res = qrl.getResult(resID);
			 if(res.queryString.equals(query)){
				 System.out.println(i+")"+res._IP.getHostAddress()+":"+res._downloadPort+" 			Size:"+res._fileSize);
				 System.out.println("Name:"+res._fileName);
				 i++;
			 }
		}
		System.out.println("");
		}	
	
		else{
			System.out.println("");
			System.out.println("-----------------------------");
			System.out.println("The query was '"+ query+ "'");
			Iterator<Integer> qrlIter = qrl.getItertor();
			int i = 1;
			while(qrlIter.hasNext()){
				 Integer resID = qrlIter.next();
				 QueryResult res = qrl.getResult(resID);
					 System.out.println(i+")"+res._IP.getHostAddress()+":"+res._downloadPort+" 			Size:"+res._fileSize);
					 System.out.println("Name:"+res._fileName);
					 i++;
				 
			}
			System.out.println("");
			
		}
	}

}
