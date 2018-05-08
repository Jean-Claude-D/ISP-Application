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
			String username = BizUtil.getValidUserInput(
				"Please enter your username",
				"You must enter a maximum of " + Customer.USERNAME_MAX_LENGTH + " characters",
				(String input) -> {return Customer.isValidUsername(input);},
				(String validInput) -> {return validInput;}
			);
			
			String password = BizUtil.getValidUserInput(
				"Please enter your password",
				"Invalid password",
				(String input) -> {return true;},
				(String validInput) -> {return validInput;}
			);
			
			Customer loggedIn = AppUtil.login(username, password, "login : invalid username/password");
			
			if(loggedIn == null) {
				return "login : invalid username/password";
			}
			else {
				return "login : \n" + loggedIn.toString();
			}
		}));
		
		Menu appMenu = new Menu("Customer Application",
		() -> {return app.state();}, optionList);
		
		appMenu.run();
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