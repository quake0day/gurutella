/**
 * 
 */

/**
 * @author Administrator
 *
 */
public class UnitTrans {
	String unit;
	double totalSize;

	public UnitTrans(int totalSize)
	{
		
		if(totalSize > 1000){
			this.totalSize = totalSize /1000.0;
			unit = " kB";
		}
		if(this.totalSize > 1000){
			this.totalSize = this.totalSize /1000.0;
			unit = " MB";
		}
		if(this.totalSize > 1000){
			this.totalSize = this.totalSize /1000.0;
			unit = " GB";
		}
		if(this.totalSize > 1000){
			this.totalSize = this.totalSize /1000.0;
			unit = " TB";
		}
	}
	
	public String unit()
	{
		return unit;
	}
	
	public double num()
	{
		return totalSize;
	}
}
