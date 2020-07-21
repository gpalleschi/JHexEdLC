package gpsoft;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

	private static final Pattern HEXADECIMAL_PATTERN = Pattern.compile("\\p{XDigit}+");
	
	/* Check if values is hexadecimal */
	public static boolean isHexadecimal(String input) {
	    final Matcher matcher = HEXADECIMAL_PATTERN.matcher(input);
	    return matcher.matches();
	}
	
	/* Check if is a File */
	public static boolean isFile(String filePathString) {
		  File f = new File(filePathString);
		  if(f.exists() && !f.isDirectory()) { 
		    // do something
	        return true;		 
		  } else {
	        return false;		 
		  }
	}	
	
	public static boolean isNumeric(String str) {
	    if (str == null) {
	        return false;
	    }
	    int sz = str.length();
	    for (int i = 0; i < sz; i++) {
	        if (Character.isDigit(str.charAt(i)) == false) {
	            return false;
	        }
	    }
	    return true;
	}
	
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
}
