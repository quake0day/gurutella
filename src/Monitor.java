import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;




/**
 * 
 */

/**
 * @author quake0day
 * @author Tianmiao
 */

public class Monitor {

	/**
	 * @param args
	 */
	private int tcpPort1,tcpPort2;
	private ClientInfoList _client;
	private FileInfoList _fileList;
	private MessageIDList _routingTable;
	private boolean isQuit = false;

	public Monitor (int port1, int port2, ClientInfoList clients, FileInfoList fl, MessageIDList rt) throws IOException, InterruptedException{
		 this.tcpPort1 = port1;
		 this.tcpPort2 = port2;
		 this._client = clients;
		 this._fileList = fl;
		 this._routingTable = rt;
		 //System.out.println("test");  
	     BufferedReader stdIn = new BufferedReader(
                 new InputStreamReader(System.in));
	     String userInput;
		 	     
		 while(!isQuit)
		 { 
			 
		 try {
			    System.out.print("simpella>");
			 	//obtain user input
			    userInput = stdIn.readLine();
			    // split user input by space and save it to an matrix
			    String[] command = userInput.split(" "); 
			    // the first character should be a command
			    //userInput = command[0]; this statement would lead to OutOfBoundsException in 'share'
/////////////////quit command//////////////////////			   
			    if (command[0].equalsIgnoreCase("quit"))
			    {
			    	isQuit = true;
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
			    	targetIPAddress.trim();
			    	String targetTCPPort = command[1].split(":")[1];
			    	targetIPAddress.trim();
			    	// create new thread connect to handle this request
			    	// see Connect.java for more detail
			    	//Thread connect = new Thread(new Connect(ipaddr,tcp,new echoer()));
			    	if(_client.size(0) < MyConstants.MAX_OUTGOING_CONNECTION_NUM){
/*Inappropriate*/   	Thread connect = new Thread(new Connect(targetIPAddress,targetTCPPort,_client,_routingTable));
			    	}
			    	else{
			    		System.out.println(MyConstants.STATUS_OUTGOING_REACHED_LIMIT);
			    	}
			    	}
			    }
			    else if (command[0].equalsIgnoreCase("update")){
			    	// send PING to all neighbors
					Thread update = new Thread(new Update(_client));
					update.start();
			    }
			    else if (command[0].equals("send"))
			    {
			    	String message = null;
			    	int connid = 0;
			    	// for send command, a user should provide at least 3 parameters
			    	if(command.length < 3){
			    		System.out.println("Usage:send <conn-id> <message>");
			    	}
			    	else{		
			    		try{
			    			connid = Integer.parseInt(command[1]);
			    		} catch(NumberFormatException e){
			    			System.out.println("The conn-id you input is not a valid one ");
			    		}
			    	for(int i=2; i < command.length; i++){
			    		if(message == null){
			    			message = command[i];
			    		}
			    		else{
			    			message = message +" "+ command[i] ;
			    		}
			    	}
			    	}
			    	/*
			    	Thread send = new Thread(new SendMessage(connid,message,_client));	
			    	send.start();
			    	*/
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
		 }
		 stdIn.close();
	     System.exit(1);
	 }
}
