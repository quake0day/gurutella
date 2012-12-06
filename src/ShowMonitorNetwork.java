import java.io.IOException;
import java.util.Iterator;

/**
 * 
 */

/**
 * @author quake0day
 *
 */
public class ShowMonitorNetwork   implements Runnable{

	/**
	 * 
	 */
	private MonitorNetwork _mnl;
	public ShowMonitorNetwork(MonitorNetwork mnl) {
		// TODO Auto-generated constructor stub
		this._mnl = mnl;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
    	System.out.println("------------------------");
    	System.out.println("Please enter to continue");
    	Iterator<String> k;
    	boolean first_time = true;
        //Iterator<String> k = _mnl.getIterator();
    	int old_length = 0;
        while(true){
        	int new_length = _mnl.getQuerySet().size();
        	
        	if(old_length != new_length && first_time == true){
        		first_time = false;
            k = _mnl.getIterator();
        	try{
        while(k.hasNext()){
        	System.out.println("Search: '"+k.next()+"'");
        	}
        	}catch (Exception e){
        		continue;
        	}
        	old_length = new_length;
        }
        	else if( old_length != new_length && first_time == false){
        		int diff = new_length - old_length;
        		if(diff > 0){
        			String query_new = _mnl.getQuerySet().get(new_length-diff);
        			System.out.println("Search: '"+query_new+"'");
        			diff --;
        		}
            	old_length = new_length;

        	}
        	else{
        		continue;
        	}
           

        try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			break;
		}
        }
	}

}
