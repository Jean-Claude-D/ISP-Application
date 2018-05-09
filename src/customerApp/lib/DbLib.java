package lib;

import java.security.SecureRandom;

import java.math.BigInteger;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.util.Base64;

import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;

public final class DbLib {
	private static final SecureRandom RAND = new SecureRandom();
	
	private DbLib() {};
	
	public static String getSalt(int length) {
		return new BigInteger(length * 5, RAND).toString(32);
	}
	public static String getSalt() {
		return getSalt(30);
	}
	
	public static byte[] hash(String password, String salt, int length) {
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			
			PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 1024, length * 4);

			SecretKey key = skf.generateSecret(spec);
			
	        return key.getEncoded();
		}
		catch(NoSuchAlgorithmException | InvalidKeySpecException exc) {
			throw new RuntimeException("Could not hash password", exc);
		}
	}
	
	public static byte[] hash(String password, String salt) {
		return hash(password, salt, 32);
	}
}