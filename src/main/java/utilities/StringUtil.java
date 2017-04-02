package utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class StringUtil {
	public static boolean isNullOrEmpty(String s) {
		if (s == null || s.length() == 0)
			return true;
		return false;
	}

	public static boolean equals(String one, String two, boolean caseSensitive) {
		if (one == null || two == null)
			return false;
		else if (caseSensitive)
			return one.equals(two);
		else
			return one.equalsIgnoreCase(two);
	}

	public static String hashPassword(String pass, byte[] salt) {
		return getSecurePassword(pass, salt);
	}

	public static byte[] getSalt() {
		try {
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			byte[] salt = new byte[16];
			sr.nextBytes(salt);
			return salt;
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		}

		return null;
	}

	private static String getSecurePassword(String passwordToHash, byte[] salt) {
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(salt);
			byte[] bytes = md.digest(passwordToHash.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

}
