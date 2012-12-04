import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


/**
 * 
 */

/**
 * @author Tianmiao
 *
 */
public class ServerUpload extends Thread{
	private ServerSocket _serverSoc;
	private Socket _uploadSoc;
	private int _fileNum;
	private String _fileName;
	
	private String _downIP;
	private int _downPort;
	
	private FileInfoList _fileList;
	
	public ServerUpload(int downPort, FileInfoList fList)
	{
		//_serverSoc = Soc;
		_downPort = downPort;
		_fileList = fList;
	}
	
	public void run()
	{
		try {
			_serverSoc = new ServerSocket(_downPort);
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} 
		try {
			_uploadSoc = _serverSoc.accept();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		DataOutputStream out;
		DataInputStream in;
		try {
			out = new DataOutputStream(_uploadSoc.getOutputStream());
			in = new DataInputStream(_uploadSoc.getInputStream());
			System.out.println("Initializing");
		
			byte[] tempIn = new byte[30];

			in.read(tempIn);

			long t0 = new Date().getTime();
			while ((new Date().getTime() - t0) < (10 * 60 * 1000))
			{
				System.out.println("GetM");
				HTTPGetMessage gotMessage = new HTTPGetMessage(tempIn);
				if (gotMessage.isGetMessage())
				{
					_fileNum = gotMessage.getRequestNum();
					_fileName = gotMessage.getRequestName();
					_downIP = gotMessage.getIPString();
					_downPort = gotMessage.getPortNum();
					if (_uploadSoc.getInetAddress().toString().split("/")[1]
						.equals(_downIP))
					{
						File[] file = _fileList.getFile(_fileName);
						HTTPResponseMessage resp = new HTTPResponseMessage((int) file[0].length());
						byte[] response = resp.getMessage();
						try {
							out.write(response);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					System.out.println("sending file");
						InputStream inFile;
						try {
							int byteread = 0;
							while(byteread != -1)
							{
								byte[] tempbytes = new byte[20000];
								inFile = new FileInputStream(file[0]);
								byteread = inFile.read(tempbytes);
								out.write(tempbytes);
							}
						} catch (Exception e1) {
							System.out.println("Wrong in reading uploading file!");
						}
					}
				}
			}
		} catch(Exception e2) {
			e2.getStackTrace();
		}
	}
}
