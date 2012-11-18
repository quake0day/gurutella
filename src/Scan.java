/*
 * Version 1, 'Scan' command
 * Wanted:	1.Legality check of input command format
 * 			2.FileInfoList().getParentPath() *access FileInfoList.parentPath
 * 			3.FileInfoList().setFileNum(int)
 * 			4.FileInfoList().setFileSize(double)
 * 			5.FileInfoList().clear()
 * 			6.FileInfoList().getFileNum()
 * 			7.FileInfoList().getFileSize()
 **/

/**
 * @author Tianmiao
 */

import java.io.File;

public class Scan {
	private FileInfoList _fileList;
	private File _dir;
	File[] tempList;
	
	public Scan(FileInfoList f)
	{
		_fileList = f;
		if (!_fileList.isNull())
		{
			if (new File(_fileList.getAbsolutePath()).exists())	//Check if file list is null
			{
				System.out.println("Scanning " + _fileList.getAbsolutePath() + " for files...");
				_dir = new File(_fileList.getAbsolutePath());
				_fileList.clear();
				tempList = _dir.listFiles();
				this.scanRefresh();
				System.out.println("Scanned " + _fileList.getFileNum() + " files and " + _fileList.getFileSize() + " bytes.");
			}
			else
			{
				_fileList.clear();
				System.out.println("Please appoint shared directory first!");
			}
		}
		else
		{	
			System.out.println("Please appoint shared directory first!");
		}
	}
	
	
	private void scanRefresh()	//Private method
	{
		double size = 0.0;
		if (tempList.length == 0)
		{
			_fileList.addFile(_dir);
			_fileList.setAbsolutePath(_dir.getAbsolutePath());
			_fileList.setFileNum(0);
			_fileList.setFileSize(0.0);
		}
		else
		{
			for(int i = 0; i < tempList.length; i++)
			{
				_fileList.addFile(tempList[i]);
				size += (double)tempList[i].length();
			}
			_fileList.setAbsolutePath(_dir.getAbsolutePath());
			_fileList.setFileNum(tempList.length);
			_fileList.setFileSize(size);
		}
	}
}
