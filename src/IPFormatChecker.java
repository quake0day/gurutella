/**
 * 
 */

/**
 * @author Tianmiao
 *
 */
public class IPFormatChecker {
    private String _ip;

    public IPFormatChecker(String ip)
    {
        this._ip = ip;
    }

    public boolean checkIPFormat()
    {
        String[] ipParg = _ip.split("\\.");
        if (ipParg.length != 4) return false;
        else
        {
            int i = 0;
            int j = 0;
            for (String word: ipParg)
            {
                int temp = Integer.parseInt(word.trim());
                if (temp > 255 || temp < 0)
                    return false;
                else if (temp == 0)
                    i++;
                else if (temp == 255)
                    j++;
            }
            if (i == 4 || j == 4)
                return false;
        }
        return true;
    }
}
