package lib;

import java.math.BigInteger;
import java.sql.Date;
import java.security.Security;
import java.security.SecureRandom;

import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class DbLib {
	private static final SecureRandom RAND = new SecureRandom();
	
	private DbLib() {};
	
	public static boolean isValidDate(long date) {
		return date >= 0;
	}
	
	public static Date toDate(long date) {
		return new Date(date);
	}
	
	public static long toLong(Date date) {
		return date.getTime();
	}
	
	public static String getSalt(int length) {
		return new BigInteger(length * 8, RAND).toString(32);
	}
	public static String getSalt() {
		return getSalt(30);
	}
	
	public static String hash(String password, String salt, int length) {
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			
			PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 1024, length);

			SecretKey key = skf.generateSecret(spec);
			
	        String hash = byteArrayToHex(key.getEncoded());
			
	        return hash;
		}
		catch(NoSuchAlgorithmException | InvalidKeySpecException exc) {
			throw new RuntimeException("Could not hash password", exc);
		}
	}
	
	private static String byteArrayToHex(byte[] arr) {
		if(arr == null) {
			throw new IllegalArgumentException("Cannot convert arr to Hex : is null");
		}
		
		StringBuilder hex = new StringBuilder();
		for(byte hexByte : arr) {
			hex.append((char)(128 + hexByte));
		}
		
		return hex.toString();
	}
}