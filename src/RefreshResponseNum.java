/**
 * 
 */

/**
 * @author quake0day
 *
 */
public class RefreshResponseNum  implements Runnable{

	/**
	 * 
	 */
	private int response;
	private QueryResultList qrl;
	public RefreshResponseNum(QueryResultList qrl) {
		// TODO Auto-generated constructor stub
		this.response = 0;
		this.qrl = qrl;
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			response = qrl.length();
			System.out.print(response+" response received.");
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
            try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}


}
