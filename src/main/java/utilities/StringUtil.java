package utilities;

public class StringUtil {
	public static boolean isNullOrEmpty(String s) {
		if (s == null || s.length() == 0)
			return true;
		return false;
	}

}
