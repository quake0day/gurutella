import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

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
	private FileInfoList _fileList;

	public Download(int n, int port, QueryResultList QL, DownloadList dL, FileInfoList fl)
	{
		_fileNo = n;
		_qL = QL;
		_dL = dL;
		_downPort = port;
		_fileList = fl;
	}
	
	public void run()
	{
		_qR = _qL.getDownload(_fileNo);
		if (_qL.getDownload(_fileNo) != null)
		{			
			Socket soc4Down = null;
			try {
				soc4Down = new Socket(_qR.getIP(), _downPort);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String _fileName = _qR.getFileName();
			String IPAdd = soc4Down.getLocalAddress().toString().split("/")[1] + ":" + _qR.getDownloadPort();
			byte[] b = new HTTPGetMessage(_fileNo, _fileName, IPAdd).getMessage();

			//System.out.println("IP\t" + _qR.getIP());
			
			try {
				DataOutputStream out = new DataOutputStream(soc4Down.getOutputStream());
				out.write(b);
				System.out.println("Sent download request...");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e2) {
				System.out.println("Unsuccessful download request...Sorry");
				return;
			}

			try {
				int size = -1;
				DataInputStream in = new DataInputStream(soc4Down.getInputStream());
				byte[] line1 = new byte[32];	//size of fail massage
				try {
					in.read(line1);
				} catch (SocketException e4) {
					System.out.println("\rDownload " + _qR.getFileName() + "is terminated..");
					return;
				} catch (IOException e5) {
					System.out.println("\rDownload " + _qR.getFileName() + "is terminated..");
					return;
				}
				System.out.println("responsed");
				if (new HTTPFailMessage(line1).isFailMessage())
				{
					System.out.println(MyConstants.STATUS_503_queryFail);
				}
				else 
				{
					
					if (new HTTPResponseMessage(line1).isResponse())
					{
						int i = 0;
						byte[] line2 = new byte[400];
						byte[] data;
						int pLength = in.read(line2);
						
						for (byte t: line2)
						{
							i++;
							if ((int) t == 0)
								break;
						}
						
						
						byte[] line =new byte[32 + i];
						System.arraycopy(line1, 0, line, 0, 32);
						System.arraycopy(line2, 0, line, 32, i);
						HTTPResponseMessage resp = new HTTPResponseMessage(line);
						//System.out.println(MyConstants.STATUS_200_DownLoadAble);
						size = resp.getSize();
						
						System.out.print("Downloading '" + _qR.getFileName() + "' ...");
						
						System.out.println("");
						byte[] tempdata = null;
						if(size > MyConstants.MAX_BUFFER_DOWNLOAD){
							tempdata = new byte[MyConstants.MAX_BUFFER_DOWNLOAD];	//size <= real size
						}
						else{
							tempdata = new byte[size];
						}
						DownloadStorage storage = new DownloadStorage(_qR);
						_dL.add2DownList(storage);
						int dataLength = in.read(tempdata);
						if(dataLength == size){ // small files
							storage.addData(tempdata, dataLength);
							FileOutputStream fin = new FileOutputStream(
									_fileList.getAbsolutePath() + "//" 
											+ _qR.getFileName());
							
							fin.write(storage.getByte());
							fin.close();
						}
						else{
							int cacheLength = dataLength;
							String hashString = (_fileName+IPAdd).hashCode()+".tmp";
							while(cacheLength < size)
							{
								storage.addData(tempdata, size,hashString,_fileList);
								dataLength = in.read(tempdata);
								if(dataLength == -1){
									break;
								}
								cacheLength += dataLength;
							}
							File file = new File(_fileList.getAbsolutePath() + "//" +hashString);
							file.renameTo(new File(_fileList.getAbsolutePath() + "//" +_qR.getFileName()));
						}
						//System.out.println(_fileList.getAbsolutePath());
						//System.out.println(_qR.getFileName());

						storage.setEnd();
					}
					else
					{
						System.out.println("Unexpected HTTP Response received! Sorry.");
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
