/*
 * Version 1, 'Share' command
 * Wanted: 	1.Legality check of directory representation format separately
 * 			2.FileInfoList().addFile(File)
 * 			3.FileInfoList().clear() *restore shared file list when calling 'share'
 * 			6.FileInfoList().setParentPath(String) *mutate FileInfoList.parentPath 
 * 			7.FileInfoList().setFileNum(int)
 * 			8.FileInfoList().setFileSize(double)
 * */

/**
 * @author Tianmiao
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Tianmiao
 * 
 */

public class Share{
	private File _dir;
	private FileInfoList _fileList; // reference to whole variable
	
	public Share(FileInfoList f)
	{
		//This constructor used for share -i
		_fileList = f;
	}
	
	public Share(String path, FileInfoList f)
	{
		//This constructor used for share
		_fileList = f;
		_dir = new File(path);
	}
	
	public void sharePerform() throws IOException	//the perform function, open to call
	{			
		if (_dir.exists())
		{		
			_fileList.clear();	//when share is invoked the second time, overrides the old one
			if (_dir.isFile())
			{
				_fileList.addFile(_dir);
				_fileList.setAbsolutePath(_dir.getAbsolutePath());
				_fileList.setFileNum(1);
				_fileList.setFileSize(_dir.length());
				System.out.println("Shared a file: " + _dir.getName());
			}
			else if (_dir.isDirectory())
			{
				this.shareDirectory();
			}
			else
			{
				System.out.println("Cannot recognize shared object...Failed");
			}
		}
		else 
			this.unknownDirectory();
	}
	
	public void shareInfo()	//the share info function, open to call
	{
		if (!_fileList.isNull())
		{
			if (new File(_fileList.getAbsolutePath()).exists())
			{
					System.out.println("sharing " + _fileList.getAbsolutePath());
			}
			else
			{
				_fileList.clear();
				System.out.println("No files are shared currently!");
			}
		}
		else
		{
			System.out.println("No files are shared currently!");
		}
	}
	
	private void shareDirectory()	//Private method
	{
		double size = 0.0;
		if (_dir.list().length == 0)
		{
			_fileList.addFile(_dir);
			_fileList.setAbsolutePath(_dir.getAbsolutePath());
			_fileList.setFileNum(0);
			_fileList.setFileSize(0.0);
			System.out.println("Shared an EMPTY folder: " + _dir.getName());
		}
		else
		{
			File[] tempList = _dir.listFiles();
			for(int i = 0; i < tempList.length; i++)
			{
				_fileList.addFile(tempList[i]);
				size += (double)tempList[i].length();
			}
			_fileList.setAbsolutePath(_dir.getAbsolutePath());
			_fileList.setFileNum(tempList.length);
			_fileList.setFileSize(size);
			if (_dir.getName().length() == 0)
				System.out.println("Shared files in hard disk " + _dir.getPath());
			else
				System.out.println("Shared files in folder: " + _dir.getName());
		}
	}
	
	private void unknownDirectory() throws IOException	//Private method
	{
		System.out.print("Directory(File) doesn't exist, create it as a shared folder now?(y/n): ");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		String ans = input.readLine();
		if ((ans.equals("y") || ans.equals("Y")))
		{
			_fileList.clear();	//overrides the old one
			System.out.println("Creating...");
			_dir.mkdirs();
			System.out.println("Created!");
			this.sharePerform();
		}
		else if ((ans.equals("n") || ans.equals("N")))
		{//Do nothing
			return;
		}
		else 
		{
			System.out.println("Invalid input! Try again.");
			this.unknownDirectory();
		}		
	}
}
