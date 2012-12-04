import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * 
 */

/**
 * @author Administrator
 *
 */
public class Download extends Thread{
	private int _fileNo;
	private int _downPort;
	private QueryResultList _qL;
	private QueryResult _qR = null;
	private DownloadList _dL;

	public Download(int n, int port, QueryResultList QL, DownloadList dL)
	{
		_fileNo = n;
		_qL = QL;
		_dL = dL;
		_downPort = port;
	}
	
	public void run()
	{
		if (_qL.getDownload(_fileNo) != null)
		{
			_qR = _qL.getDownload(_fileNo);
			String _fileName = _qR.getFileName();
			String IPAdd = _qR.getIP().toString().split("/")[1] + ":" + _qR.getDownloadPort();
			byte[] b = new HTTPGetMessage(_fileNo, _fileName, IPAdd).getMessage();
			Socket soc4Down = null;
			try {
				soc4Down = new Socket(_qR.getIP(), _downPort);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				DataOutputStream out = new DataOutputStream(soc4Down.getOutputStream());
				out.write(b);
				System.out.println("Sent download request...");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				int size = -1;
				DataInputStream in = new DataInputStream(soc4Down.getInputStream());
				byte[] line1 = new byte[32];	//size of fail massage
				in.read(line1);
				if (new HTTPFailMessage(line1).isFailMessage())
				{
					System.out.println(MyConstants.STATUS_503_queryFail);
				}
				else 
				{
					
					if (new HTTPResponseMessage(line1).isResponse())
					{
						int i = 0;
						byte[] line2 = new byte[100];
						byte[] data;
						in.read(line2);
						for (byte t: line2)
						{
							i++;
							if ((int) t == 0)
								break;
						}
						
						byte[] line =new byte[32 + i];
						System.arraycopy(line, 0, line1, 0, 32);
						System.arraycopy(line, 32, line2, 0, i);
						HTTPResponseMessage resp = new HTTPResponseMessage(line);
						System.out.println(MyConstants.STATUS_200_DownLoadAble);
						size = resp.getSize();
						
						System.out.print("Downloading '" + _qR.getFileName() + "' ...");
						
						System.out.println("\n");
						byte[] tempdata = new byte[MyConstants.MAX_BUFFER_DOWNLOAD];	//size <= real size
						DownloadStorage storage = new DownloadStorage(_qR);
						_dL.add2DownList(storage);
						int dataLength = in.read(tempdata);
						while(dataLength != -1)
						{
							storage.addData(tempdata, dataLength);
							dataLength = in.read(tempdata);
						}
						storage.setEnd();
					}
					System.out.println("Unexpected HTTP Response received! Sorry.");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
