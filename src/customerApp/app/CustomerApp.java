package app;

import java.sql.*;
import biz.Menu;
import lib.Customer;
import lib.DbLib;

public final class CustomerApp {
	private Customer logged;
	private CustomerApp() {
	}
	
	public static void main(String[] args) {
		CustomerApp app = new CustomerApp();
		app.init();
		
		
		String[] passwdList = {"password", "helloworld", "hithere", "johnbobdoe", "suziespassword"};
		for(int i = 0; i < passwdList.length; i++) {
			System.out.println(passwdList[i] + ":");
			
			String salt = DbLib.getSalt(30);
			System.out.println(salt);
			System.out.println(DbLib.hash(passwdList[i], salt, 30));
			
			System.out.println();
		}
		
		/*List<Pair<String, Supplier<String>>> optionList = new ArrayList<Pair<String, Supplier<String>>>();
		
		optionList.add(new Pair<String, Supplier<String>>("Log In", () -> {
			
			return 
		}));
		
		Menu appMenu = new Menu("Customer Application",
		() -> return app.state(), optionList);*/
	}
	
	private void init() {
		try {
			AppUtil.loadJDBC();
		}
		catch(ClassNotFoundException exc) {
			throw new RuntimeException("App init : Error while loading the OJDBC", exc);
		}
		
		try {
			AppUtil.getISPConnection();
		}
		catch(SQLException exc) {
			throw new RuntimeException("App init : Error during initial connection test to the database", exc);
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