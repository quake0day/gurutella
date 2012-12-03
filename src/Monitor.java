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
    private ConnectionInfoList _client;
    private FileInfoList _fileList;
    private MessageIDList _routingTable;
    private NetworkServerList _nsl;
    private boolean isQuit = false;
    private QueryResultList _qrl;
    private String queryString;

    @SuppressWarnings("deprecation")
	public Monitor (int port1, int port2, ConnectionInfoList clients, FileInfoList fl, MessageIDList rt, NetworkServerList nsl) throws IOException, InterruptedException{
        this.tcpPort1 = port1;
        this.tcpPort2 = port2;
        this._client = clients;
        this._fileList = fl;
        this._nsl = nsl;
        this._routingTable = rt;
        
        _qrl = new QueryResultList();
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
                    System.out.println("Good Bye!");
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
                        if (!command[1].contains(":"))
                        {
                            System.out.println("Usage:open <ip-address>:<tcp-port>");
                        }
                        else {
                            String targetIPAddress = command[1].split(":")[0];
                            targetIPAddress.trim();
                            if (!new IPFormatChecker(targetIPAddress).checkIPFormat())
                                System.out.println("Invalid IP Address!");
                            else {
                                String targetTCPPort = command[1].split(":")[1];
                                targetIPAddress.trim();						 
                                if (Integer.parseInt(targetTCPPort) < 0) {
                                    System.out.println("Invalid TCP Port!");
                                }
                                else {
                                    // create new thread connect to handle this request
                                    // see Connect.java for more detail
                                    //Thread connect = new Thread(new Connect(ipaddr,tcp,new echoer()));
                                    if(_client.size() < MyConstants.MAX_OUTGOING_CONNECTION_NUM){
                                        Thread connect = new Connect(targetIPAddress,targetTCPPort,_client,_routingTable,_nsl,_qrl);
                                        connect.start();
                                    }
                                    else{
                                        System.out.println(MyConstants.STATUS_OUTGOING_REACHED_LIMIT);
                                    }
                                }
                            }
                        }
                    }
                }
                /////////////////update command////////////////////   
                else if (command[0].equalsIgnoreCase("update")){
                    // clear NetworkServerList before update
                    nsl.clearAll();

                    // send PING to all neighbors
                    Thread update = new Thread(new Update(_client, rt));
                    update.start();
                }
                /////////////////find command////////////////////   
                else if (command[0].equalsIgnoreCase("find")){
                    if(command.length < 2){
                        System.out.println("Usage:find word1 [word2]");
                    }
                    queryString = userInput.substring(command[0].length());
                    queryString = queryString.trim();
                    //check legality			    		
                    //Check illegal characters
                    if (queryString.contains("|") || queryString.contains("*") || queryString.contains("?") 
                            || queryString.contains("\"") ||queryString.contains("<") || queryString.contains(">")
                            || queryString.contains("/") || queryString.endsWith("\\"))
                    {
                        System.out.println("Illegal character(s)(|, *, ?, \", <, >, /, \\) appeared in input path");			 
                    }
                    else
                    {	
                        System.out.println("Searching Simpella Network for `"+queryString+"'");
                        _qrl.setQuery(queryString);
                        // send Query to all neighbors
                        Thread query = new Thread(new Query(queryString, _client,rt));
                        query.start();
                        
                        System.out.println("Press Enter to Continue.");
                        Thread ref = new Thread(new RefreshResponseNum(_qrl));

                        //ref.start();
                        Thread.sleep(450);
                        Thread showRes = new Thread(new ShowQueryRes(_qrl,queryString,false));
                        showRes.start();
                    }
                }
                /////////////////list command////////////////////   
                else if (command[0].equalsIgnoreCase("list")){
                    Thread showRes = new Thread(new ShowQueryRes(_qrl,queryString,true));
                    showRes.start();
                	
                }
                /////////////////download command////////////////////   
                else if (command[0].equalsIgnoreCase("download")){
                	boolean downloadAble = false;
                	QueryResult fileInfo = null;
                	if(command.length != 2){
                		System.out.println("Usage: download <file-no>");
                	}
                	else if(command.length == 2){
                		try{
                		    int num = Integer.parseInt(command[1]);
                		    if(num <= _qrl.length() && num > 0){
                		    	fileInfo = _qrl.getDownload(num);
                		    	if(fileInfo != null){
                    		    	downloadAble = true;
                		    	}
                		    }
                		}catch(NumberFormatException e){
                		    System.out.println("Please enter a correct file number");
                		    downloadAble = false;
                		}
                		
                	}
                	if(downloadAble == false){
                		System.out.println("Cannot download target file, try again.");
                	}
                	if(downloadAble == true){
                		//fileInfo._downloadPort
                		
                    //Thread showRes = new Thread(new ShowQueryRes(_qrl,queryString,true));
                    //showRes.start();
                	}
                	
                }
                /////////////////clear command////////////////////   
                else if (command[0].equalsIgnoreCase("clear")){
                	boolean finished = false;
                	if(command.length > 2){
                		System.out.println("Usage: clear [file-no]");
                	}
                	else if(command.length == 1){
                		_qrl.clearAll();
                		finished = true;
                	}
                	else if(command.length == 2){
                		try{
                		    int num = Integer.parseInt(command[1]);
                		    if(num <= _qrl.length() && num > 0){
                		    	_qrl.clear(num);
                		    	finished = true;
                		    }
                		}catch(NumberFormatException e){
                		    System.out.println("Please enter a correct file number");
                		    finished = false;
                		}
                		
                	}
                	if(finished == false){
                		System.out.println("Cannot clear target file, try again.");
                	}
                	if(finished == true){
                    Thread showRes = new Thread(new ShowQueryRes(_qrl,queryString,true));
                    showRes.start();
                	}
                	
                }
                ////////////////send command//////////////////////
                else if (command[0].equals("send"))
                {
                    String message = null;
                    // for send command, a user should provide at least 3 parameters
                    if(command.length < 3){
                        System.out.println("Usage:send <conn-id> <message>");
                    }
                    else{
                        int connid = 0;		
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
