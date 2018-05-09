package lib;

import java.security.SecureRandom;

import java.math.BigInteger;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;

public final class SecurityUtil {
	/* Cryptographically secure random number generator */
	private static final SecureRandom RAND = new SecureRandom();
	
	private SecurityUtil() {
	}
	
	public static String getSalt(int charLength) {
		return new BigInteger(charLength * 5, RAND).toString(32);
	}
	
	public static byte[] hash(String password, String salt, int byteLength) {
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			
			PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 1024, byteLength * 4);

			SecretKey key = skf.generateSecret(spec);
			
	        return key.getEncoded();
		}
		catch(NoSuchAlgorithmException | InvalidKeySpecException exc) {
			throw new RuntimeException("Could not hash password", exc);
		}
	}
}