package app;

import java.util.List;
import java.util.ArrayList;
import biz.BizUtil;
import java.sql.*;
import biz.Menu;
import lib.Customer;
import lib.DbLib;
import lib.Pair;
import java.util.function.Supplier;

public final class CustomerApp {
	private Customer logged;
	private CustomerApp() {
	}
	
	public static void main(String[] args) {
		CustomerApp app = new CustomerApp();
		app.init();
		
		List<Pair<String, Supplier<String>>> optionList = new ArrayList<Pair<String, Supplier<String>>>();
		
		optionList.add(new Pair<String, Supplier<String>>("Log In", () -> {
			Customer loggedIn = AppUtil.login();
			
			if(loggedIn == null) {
				return "\nlogin : invalid username/password";
			}
			else {
				return "\nlogin : \n" + loggedIn.toString();
			}
		}));
		
		Menu appMenu = new Menu("Customer Application",
		() -> {return app.state();}, optionList);
		
		appMenu.run();
		
		/*String[] passwds = {"password", "passwd", "myPassw"};
		for(String passwd : passwds) {
			String salt = DbLib.getSalt();
			byte[] hashed = DbLib.hash(passwd, salt);
			
			System.out.println(passwd);
			System.out.println(salt);
			System.out.println(bytesToHex(hashed));
		}*/
	}
	
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
	
	private void init() {
		try {
			AppUtil.loadJDBC();
		}
		catch(ClassNotFoundException exc) {
			throw new RuntimeException("App init : Error while loading the OJDBC", exc);
		}
		
		if(!AppUtil.testConnection()) {
			throw new RuntimeException("App init : Error during initial connection test to the database");
		}
	}
	
	private String state() {
		String state;
		
		if(this.logged == null) {
			state = "Not Logged In";
		}
		else {
			state = "Logged In As : \n" + this.logged.toString();
		}
		
		return state;
	}
}