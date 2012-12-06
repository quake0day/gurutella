/**
 * 
 */

/**
 * @author Administrator
 *
 */
public class UnitTrans {
	String unit;

	public UnitTrans(int totalSize)
	{
		
		if(totalSize > 1000){
			totalSize = totalSize /1000;
			unit = " kB";
		}
		if(totalSize > 1000){
			totalSize = totalSize /1000;
			unit = " MB";
		}
		if(totalSize > 1000){
			totalSize = totalSize /1000;
			unit = " GB";
		}
		if(totalSize > 1000){
			totalSize = totalSize /1000;
			unit = " TB";
		}
	}
}
