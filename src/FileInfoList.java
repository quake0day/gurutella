import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 */

/**
 * @author quake0day
 * @author Tianmiao
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
	 * add something
	 **/
	private int fileNum = 0;
	private double fileSize = 0.0;
	private String absolutePath = null;
	private ArrayList <File> _sharedFile;
	private ArrayList <QueryResultSet> _qrs;
	
	public FileInfoList()
	{
		_sharedFile = new ArrayList <File>();
		_qrs = new ArrayList <QueryResultSet>();
	}
	
	public String getAbsolutePath(){
		return absolutePath;
	}
	
	public void setAbsolutePath(String path){
		File temp = new File(path).getAbsoluteFile();
		absolutePath = temp.getAbsolutePath();
	}
	public int getFileIndex(String fileName){
		return _sharedFile.indexOf(fileName);
	}
	public void setFileNum(int fileNum){
		this.fileNum = fileNum;
	}
	
	public void setFileSize(double filesize){
		fileSize = filesize;
	}
	
	public void setFileSize(long filesize)
	{
		fileSize = (double)filesize;
	}

	public int getFileNum(){
		return fileNum;
	}
	
	public double getFileSize(){
		return fileSize;
	}
	
	public ArrayList <QueryResultSet> queryFile(String queryString){
		String[] querySet = queryString.split(" ");
		String query = queryString;
		Iterator <File> ie = _sharedFile.iterator();
		while(ie.hasNext()){
			File fileIterNew = ie.next();
			String fileName = fileIterNew.getName().toString().trim();
			System.out.println(">?????aa?"+fileName);
			byte[] qu = fileName.getBytes();
			byte[] qua = query.getBytes();
			System.out.println(qu);
			System.out.println(qua);
			String quq = new String(qua);
			String quz = new String(qu);
			System.out.println("quz:"+quz.contains("soc"));
			System.out.println("quq:"+quq.contains("soc"));
			System.out.println("quq:"+quq.contains(quz));
			
			System.out.println(">?@@@"+querySet[0].contains("soc"));
			for(int i=0 ; i< querySet.length; i++){
				if(fileName.contains(querySet[i]) == true){
					break;
				}
			}
			/*
			for (String query: querySet)
			{
				String fileName = fileIterNew.getName().toString().trim();
				String q = query;
				System.out.println("NAME:"+fileName);
				System.out.println("query:"+query.contains("s"));
				System.out.println("query:"+query.contains("o"));

				System.out.println("IS:"+fileName.contains(q));
				String k = "soc.c";
				System.out.println(fileName.contains("soc"));
				if(fileName.toLowerCase().contains(query.toLowerCase())){
					int fileSize = (int)fileIterNew.length(); // !! may loss some info
					int fileIndex = _sharedFile.indexOf(fileName);
					QueryResultSet qrs = new QueryResultSet(fileIndex,fileSize,fileName);
					_qrs.add(qrs);
				}
			}
			*/
		}
		return _qrs;
	}
	
	public int getQRSsize(){
		return _qrs.size();
	}
	public boolean addFile(File file){
		 if (_sharedFile.add(file))
			 return true;
		 else
		 {
			System.out.println("File " + file.getName() + " failed to share...");
			return false;
		 }
	}
	
	
	public File[] getFile(String filename)	//Should ONLY input LEGAL parameter
	{
		String[] rqstField = filename.split(" ");
		ArrayList <File> matchList = new ArrayList <File>();
		for(int i = 0; i < _sharedFile.size(); i++)
		{
			String[] field = _sharedFile.get(i).getName().split(" ");
			for(int j = 0; j < rqstField.length; j++)
				for(int k = 0; k < field.length; k++)
				{
					if (rqstField[j].equals(field[k]))
					{
						matchList.add(_sharedFile.get(i));
					}
				}
		}
		if (matchList.isEmpty()) 
			return null;
		else
			return (File[]) matchList.toArray();
	}	
	
	public void clear(){
		if (!this.isNull())
		{
			fileNum = 0;
			fileSize = 0.0;
			absolutePath = null;
			_sharedFile.clear();
		}
	}
	
	public boolean isNull()
	{
		if (fileNum == 0 && fileSize == 0.0 && absolutePath ==null && _sharedFile.isEmpty())
			return true;
		else 
			return false;
	}

}
