import java.io.File;
import java.util.ArrayList;

/**
 * 
 */

/**
 * @author Tianmiao
 *
 */
public class QueryHit extends Thread{
	private String _words;
	private FileInfoList _fileList;
	private ArrayList<QueryResultSet> _qrs;
	private int _NumberOfHits;
	public QueryHit(String words, FileInfoList fL)
	{
		_words = words;
		_fileList = fL;
		_qrs = _fileList.queryFile(words);
		_NumberOfHits = _qrs.size();
	}

	public void run()
	{
		File[] hittedList = _fileList.getFile(_words);
		if (hittedList == null)
			return;
		else
		{
			
		}
	}
}
