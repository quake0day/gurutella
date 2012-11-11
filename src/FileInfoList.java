import java.io.File;

/**
 * 
 */

/**
 * @author quake0day
 *
 */


public class FileInfoList {
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
	public String getParentPath(){
		String parentPath = null;
		return parentPath;
	}
	public boolean setParentPath(String path){
		return true;
	}
	public boolean setFileNum(int fileNum){
		return true;
	}
	public boolean setFileSize(double filesize){
		return true;
	}
	public void clear(){
		
	}
	public int getFileNum(){
		int fileNum =0 ;
		return fileNum;
	}
	public double getFileSize(){
		double fileSize = 0;
		return fileSize;
	}
	public boolean addFile(File file){
		return true;
	}
	public String getAbsolutePath(){
		String absolutePath=null;
		return absolutePath;
	}
}
