package app;

import java.sql.*;
import oracle.jdbc.driver.OracleDriver;

import biz.CustomerUtil;
import biz.Customer;

import lib.UserInputUtil;
import lib.ConnectionUtil;

public final class CustomerApp {
	public static final String LOCATION = "localhost";
	public static final String USER = "JC";
	public static final String PASSWORD = "Hydral1sk";
	
	private CustomerApp() {
	}
	public Customer userLogged = null;
	
	public static void main(String[] args) {
		CustomerApp app = new CustomerApp();
		
		if(!loadJDBC()) {
			throw new RuntimeException("Could not load JDBC driver");
		}
		
		if(!ConnectionUtil.testConnection()) {
			throw new RuntimeException("Could not connect to " + LOCATION);
		}
		
		char userInput = (char) 0;
		
		do {
			/* As long as the customer is not logged in, the application will keep asking */
			if(app.userLogged == null) {
				String username = UserInputUtil.getStringInput(
					"Please enter your username",
					"Must be a valid username",
					(in) -> {return in.length() <= 20;}
				);
				
				try {
					System.out.println(CustomerUtil.getSalt(username));
				}
				catch(SQLException exc) {
					System.err.println("Something went wrong when getting the salt");
				}
			}
			else {
				userInput = UserInputUtil.getCharInput(
					"Please enter your choice",
					"Must be a valid character",
					(c) -> {return true;}
				);
				
				switch(userInput) {
					case 'q':
						System.out.println("Goodbye!");
						break;
					default:
						System.err.println("Invalid choice : " + userInput);
				}
			}
		}while(userInput != 'q');
	}
	
	private static boolean loadJDBC() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			return true;
		}
		catch(ClassNotFoundException exc) {
			return false;
		}
	}
}