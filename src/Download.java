import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
	private QueryResultList _qL;
	private QueryResult _qR = null;
	private ConnectionInfoList _cIL;

	public Download(int n, QueryResultList QL, ConnectionInfoList list)
	{
		_fileNo = n;
		_qL = QL;
		_cIL = list;
	}
	
	public void run()
	{
		if (_qL.getDownload(_fileNo) != null && _cIL != null)
		{
			_qR = _qL.getDownload(_fileNo);
			String _fileName = _qR.getFileName();
			String IPAdd = _qR.getIP().toString().split("/")[1] + ":" + _qR.getDownloadPort();
			byte[] b = new HTTPGetMessage(_fileNo, _fileName, IPAdd).getMessage();
			Socket soc4Down = _cIL.getSocket(_qR.getIP().toString().split("/")[1]);
			
			try {
				DataOutputStream out = new DataOutputStream(soc4Down.getOutputStream());
				out.write(b);
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
					HTTPResponseMessage resp = new HTTPResponseMessage(line1);
					if (resp.isResponse())
					{
						int i = 0;
						byte[] line2 = new byte[100];
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
						System.out.println(MyConstants.STATUS_200_DownLoadAble);
						
					}

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
