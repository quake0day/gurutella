import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




/**
 * 
 */

/**
 * @author quake0day
 * @author Tianmiao
 */

public class Monitor extends Thread{

	/**
	 * @param args
	 */
	private int tcpPort1,tcpPort2;
	public ClientInfoList clients;
	private FileInfoList _fileList;
	private static int MAX_THREAD_NUM = 9;

	public Monitor (int port1, int port2, ClientInfoList clients, FileInfoList fl) throws IOException, InterruptedException{
		   this.tcpPort1 = port1;
		   this.tcpPort2 = port2;
		   this.clients = clients;
		   this._fileList = fl;
		  // ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREAD_NUM);		

		   
	}
	
	public void run(){
	     PrintWriter out = null;
	     BufferedReader in = null;
	     BufferedReader stdIn = new BufferedReader(
                 new InputStreamReader(System.in));
	     String userInput;
		// Thread tcpserver = 
		 	     
		 while(true)
		 { 
			 
		 try {
			    System.out.print("simpella>");
			 	//obtain user input
			    userInput = stdIn.readLine();
			    // split user input by space and save it to an matrix
			    String[] command = userInput.split(" "); 
			    // the first character should be a command
			    //userInput = command[0]; this statement would lead to OutOfBoundsExcetion in 'share'
/////////////////quit command//////////////////////			   
			    if (command[0].equalsIgnoreCase("quit"))
			    {
			        System.exit(1);
			    }
/////////////////scan command//////////////////////
			    else if (command[0].equalsIgnoreCase("scan"))
			    {
			    	// create new thread info to handle this request
			    	// see Info.java for more detail
			 	    new Scan(_fileList);			 	   
			    }
/////////////////open command//////////////////////
			    else if (command[0].equalsIgnoreCase("open"))
			    {
			    	// create new thread info to handle this request
			    	// see Connect.java for more detail
			    	// for connect command, a user should provide 3 parameter
			    	if(command.length != 2){
			    		System.out.println("Usage:open <ip-address>:<tcp-port>");
			    	}
			    	else{		
			    	String targetIPAddress = command[1].split(":")[0];
			    	String targetTCPPort = command[1].split(":")[1];
			    	// create new thread connect to handle this request
			    	// see Connect.java for more detail
			    	//Thread connect = new Thread(new Connect(ipaddr,tcp,new echoer()));	
			    	Thread open = new Connect(targetIPAddress,targetTCPPort,clients);
			    	}
			    }
/////////////////Share command/////////////////////		    
			    else if (command[0].equalsIgnoreCase("share")) /*haven't finished*/
			    {
			    	if(command.length > 1)	//command legality detect
			    	{			   
			    		String[] temp = userInput.split(" ", 2);
			    		String dir = temp[1];		
			    		
			    		if (!dir.startsWith(" ")) //Blank start check
			    		{				    		
			    			//Check illegal characters
			    			if (dir.contains("|") || dir.contains("*") || dir.contains("?") || dir.contains("\"") ||dir.contains("<") || dir.contains(">"))
			    			{
			    				System.out.println("Illegal character(s)(|, *, ?, \", <, >) appeared in input path");			 
			    			}
			    			else if (dir.contains("/") && dir.contains("\\"))
			    			{
			    				System.out.println("Character(s) '\\' and '/' cannot appear in the same path");			    			
			    			}
			    			else
			    			{			    			
			    				boolean flag = false;
			    				if (dir.contains("/"))	//Blank pathname head check
			    				{
			    					String[] field = dir.split("/");
			    	   				for (int i = 0; i < field.length; i++)
				    				{
				    					if(field[i].startsWith(" "))
				    					{
				    						System.out.println("Please do not insert space to the start of a child directory");
				    						flag = true;
				    						break;
				    					}
				    					if(i > 0 && field[i].contains(":"))
				    					{
				    						System.out.println("Illegal character ':' appeared in input path");
				    						flag = true;
				    						break;
				    					}
				    				}
			    				}
			    				
			    				else if (dir.contains("\\"))
			    				{
			    					String[] field = dir.split("\\\\");
				    				for (int i = 0; i < field.length; i++)
				    				{
				    					if(field[i].startsWith(" "))
				    					{
				    						System.out.println("Please do not insert space to the start of a child directory");
				    						flag = true;
				    						break;
				    					}
				    					if(i > 0 && field[i].contains(":"))
				    					{
				    						System.out.println("Illegal character ':' appeared in input path");
				    						flag = true;
				    						break;
				    					}
				    				}
			    				}
			    				
			    				if (flag)
			    				{//Do nothing
			    				}
			    				
			    				//Share command call
			    				else if (command[1].equals("-i") && command.length ==2)
						    	{
						    		new Share(_fileList).shareInfo();
						    		
						    	}
						    	else
						    	{
						    		new Share(dir, _fileList).sharePerform();
						    	}
			    			}
			    		}
			    		
			    	}
			    	else
			    	{
			    		System.out.println("Please input a parameter: <pathname|-i>");
			    	}

			    }

/////////////////Unknown Handler//////////////////////////			    
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
