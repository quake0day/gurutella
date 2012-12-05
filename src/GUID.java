/**
 * 
 */
import java.util.Random;
/**
 * @author quake0day
 *
 */
public class GUID {

	/**
	 * 
	 */
	public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private String _GUID;
	public GUID() {
		// TODO Auto-generated constructor stub
		//System.out.println(generateMixString(6));
		//System.out.println(generateMixString(6).toString().getBytes().length);
		this._GUID = generateMixString(16);

	}
	
	public static String generateMixString(int length) 
	{

		StringBuffer sb = new StringBuffer();
		
		Random random = new Random();
		
		for (int i = 0; i < length; i++) {
		
		sb.append(allChar.charAt(random.nextInt(letterChar.length())));
		
		}
		
		return sb.toString();	

	}
	public String get_GUID(){
		return this._GUID;
	}
}

