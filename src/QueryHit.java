import java.io.File;

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

	public QueryHit(String words, FileInfoList fL)
	{
		_words = words;
		_fileList = fL;
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
