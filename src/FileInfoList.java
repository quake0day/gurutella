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
        // clear the previous result
        _qrs.clear();
        String[] querySet = queryString.split(" ");
        Iterator <File> ie = _sharedFile.iterator();
        while(ie.hasNext()){
            for (String query: querySet)
            {
                File fileIterNew = ie.next(); //test
                String fileName = fileIterNew.getName().toString().trim();
                if(fileName.toLowerCase().contains(query.toLowerCase())){
                    int fileSize = (int)fileIterNew.length(); // !! may loss some info
                    int fileIndex = _sharedFile.indexOf(fileIterNew);
                    QueryResultSet qrs = new QueryResultSet(fileIndex,fileSize,fileName);
                    _qrs.add(qrs);
                }
            }

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
            //System.out.println("File " + file.getName() + " failed to share...");
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

    public File getThisFile(String filename)
    {
    	for (File f: _sharedFile )
    	{
    		//System.out.println(f.getName());
    		//System.out.println(filename);
    		//System.out.println(f.getName().trim().equals(filename.trim()));
    		if (f.getName().trim().equals(filename.trim()))
    			return f;
    	}
    	
    	return null;
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
