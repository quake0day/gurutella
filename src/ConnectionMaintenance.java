/**
 * 
 */
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
/**
 * @author Tianmiao
 *
 */
public class ConnectionMaintenance implements ActionListener {
	private ConnectionInfoList _connectionL;
			NetworkServerList _nwkList;
			MessageIDList _mID;
			QueryResultList _queryList;
			FileInfoList _fileList;
			MonitorNetwork _monNetwork;
			GUID _guID;
			InetAddress _myIP;
			int _downPort;
			InfoParameters _iF;
	
	private Monitor _monitor;

	public ConnectionMaintenance (InetAddress IP, int tcpDown, Monitor mo
			, ConnectionInfoList cL, MessageIDList mID, NetworkServerList nL
			, QueryResultList qrL, FileInfoList fIL, MonitorNetwork mN, GUID gID
			, InfoParameters ifo)
	{
		_myIP = IP;
		_connectionL = cL;
		_nwkList = nL;
		_monitor = mo;
		_connectionL = cL;
		_mID = mID;
		_nwkList = nL;
		_queryList = qrL;
		_fileList = fIL;
		_monNetwork = mN;
		_guID = gID;
		_downPort = tcpDown;
		_iF = ifo;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		while (_connectionL.size() > 0 && _connectionL.size() < 2)
		{
			System.out.println("Automatically maintaining a connection...");
			if (_nwkList.size() > _connectionL.size())
			{
				System.out.println("in nwkList");
				ServerInfo sI = _nwkList.getServer(_connectionL);
				if (sI != null)
				{
					System.out.println("go to connect");
					_monitor.createConn(sI.getIP().toString().split("/")[1]
							, String.valueOf(sI.getPort()), _downPort, _connectionL
							, _mID, _nwkList, _queryList, _myIP, _fileList, _monNetwork
							, _guID, _iF);
				}
			}
			else
			{
				System.out.println("to ELSE");
				break;
			}
		}
	}
}
