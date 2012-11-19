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
 * @author Tianmiao
 */

public class Monitor /*extends Thread*/{

	/**
	 * @param args
	 */
	private int tcpPort1,tcpPort2;
	private ArrayList<Socket> client;
	private FileInfoList _fileList;
	
	public Monitor (int port1, int port2, ArrayList<Socket> clients, FileInfoList fl){
		   this.tcpPort1 = port1;
		   this.tcpPort2 = port2;
		   this.client = clients;
		   this._fileList = fl;
		   
	//}
	
	 //public void run(){
	     PrintWriter out = null;
	     BufferedReader in = null;
	     BufferedReader stdIn = new BufferedReader(
                 new InputStreamReader(System.in));
	     String userInput;
	     
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
		    //try {
			//	currentThread().sleep(50);
			//} catch (InterruptedException e) {
				// TODO Auto-generated catch block
		//		e.printStackTrace();
		//	}
		 }
	 }
	
}
