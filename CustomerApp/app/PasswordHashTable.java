package app;

import lib.SecurityUtil;

/* For Debug Purposes */
public final class PasswordHashTable {
	private final static char[] HEX = "0123456789ABCDEF".toCharArray();
	
	private PasswordHashTable() {
	}
	
	public static void main(String[] args) {
		String[] myPasswds = {"password", "passwd", "mypassword", "helloWorld", "password123", "1234", "IloveSQL", "4321"};
		generate(myPasswds);
	}
	
	private static void generate(String[] passwords) {
		byte[] hashedPassword;
		String salt;
		
		for(String password : passwords) {
			salt = SecurityUtil.getSalt(30);
			hashedPassword = SecurityUtil.hash(password, salt, 32);
			
			printResult(password, hashedPassword, salt);
		}
	}
	
	private static void printResult(String password, byte[] hashedPassword, String salt) {
		System.out.println("Password :\t" + password);
		
		/* Git this from StackOverflow */
		char[] hexChars = new char[hashedPassword.length * 2];
		for (int j = 0; j < hashedPassword.length; j++) {
			int v = hashedPassword[j] & 0xFF;
			hexChars[j * 2] = HEX[v >>> 4];
			hexChars[j * 2 + 1] = HEX[v & 0x0F];
		}
		System.out.println("Hashed :\t" + new String(hexChars));
		
		System.out.println("Salt :\t\t" + salt + "\n\n");
	}
}