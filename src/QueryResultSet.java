import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 
 */

/**
 * @author quake0day
 *
 */
public class QueryResultSet {

    /**
     * 
     */
    private int _fileIndex;
    private int _fileSize;
    private String _fileName;
    public QueryResultSet(int fileIndex, int fileSize, String fileName) {
        // TODO Auto-generated constructor stub
        this._fileIndex = fileIndex;
        this._fileSize = fileSize;
        this._fileName = fileName;
    }

    public int getFileIndex(){
        return _fileIndex;
    }
    public int getFileSize(){
        return _fileSize;
    }
    public String getFileName(){
        return _fileName;
    }

    public byte[] convert2Byte() throws IOException{

        byte[] _bFileName = _fileName.getBytes();
        byte[] _resultSetSingle = new byte[_bFileName.length+8+1];
        byte[] _bFileIndex = new byte[4];
        byte[] _bFileSize = new byte[4];
        _bFileIndex = convertInt2Byte(_fileIndex);
        _bFileSize = convertInt2Byte(_fileSize);
        System.arraycopy(_bFileIndex, 0, _resultSetSingle, 0, 4);
        System.arraycopy(_bFileSize, 0, _resultSetSingle, 4, 4);
        System.arraycopy(_bFileName, 0, _resultSetSingle, 8, _bFileName.length);
        return _resultSetSingle;
    }


    private byte[] convertInt2Byte(int i) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(i);
        byte[] b = baos.toByteArray();
        return b;
    }

    private byte[] convertShort2Byte(int i) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeShort(i);
        byte[] b = baos.toByteArray();
        return b;
    }

    private byte[] convertDouble2Byte(double i) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeDouble(i);
        byte[] b = baos.toByteArray();
        return b;
    }

}
