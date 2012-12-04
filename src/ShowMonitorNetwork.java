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
        Iterator<String> k = _mnl.getIterator();
        while(true){
        	try{
        while(k.hasNext()){
        	System.out.println("Search: '"+k.next()+"'");
        	}
        	}catch (Exception e){
        		
        	}
        try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
        }
	}

}
