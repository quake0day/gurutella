import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


/**
 * 
 */

/**
 * @author quake0day
 *
 */
public class Monitor extends Thread{

	/**
	 * @param args
	 */
	private int tcpPort1,tcpPort2;
	private ArrayList<Socket> client;
	public Monitor (int port1, int port2, ArrayList<Socket> clients){
		   this.tcpPort1 = port1;
		   this.tcpPort2 = port2;
		   this.client = clients;
	}
	
	 public void run(){
	     PrintWriter out = null;
	     BufferedReader in = null;
	     BufferedReader stdIn = new BufferedReader(
                 new InputStreamReader(System.in));
	     String userInput;
	     FileInfoList f = null;
	     
		 while(true)
		 { 
			 
		 try {
			    System.out.print("simpella>");
			 	//obtain user input
			    userInput = stdIn.readLine();
			    // split user input by space and save it to an matrix
			    String[] command = userInput.split(" "); 
			    // the first character should be a command
			    userInput = command[0];   
			    if (userInput.equals("quit"))
			    {
			        System.exit(1);
			    }
			    else if (userInput.equals("scan"))
			    {
			    	// create new thread info to handle this request
			    	// see Info.java for more detail
			 	    Thread scan = new Thread(new Scan(f));
			 	   
			    }
			    else{ // for unknown command.
			    	System.out.println("no such command:"+userInput);
			    }
				
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		    try {
				currentThread().sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
	 }
	
}
