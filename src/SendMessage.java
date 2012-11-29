import java.io.IOException;
import java.io.PrintWriter;


public class SendMessage extends Thread {
	private int id;
	private String messageToSend;
	private ClientInfoList clients;
	public SendMessage(int connid, String message,ClientInfoList client){
		this.clients = client;
		this.id = connid;
		this.messageToSend = message;

	}
	
	public void run(){
		PrintWriter outServer = null;
        try {
			outServer = new PrintWriter(clients.get(id).getOutputStream(), 
			        true);
			outServer.println(messageToSend);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("You're tring to communcate through a invalid socket channel,abort!");
		} 		
	}

}
