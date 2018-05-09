package app;

import java.sql.*;
import oracle.jdbc.driver.OracleDriver;

import lib.UserInputUtil;
import biz.ConnectionUtil;

public final class CustomerApp {
	private static String LOCATION = "localhost";
	private static String USER = "JC";
	private static String PASSWORD = "Hydral1sk";
	
	private CustomerApp() {
	}
	String userLogged = null;
	
	public static void main(String[] args) {
		CustomerApp app = new CustomerApp();
		
		if(!loadJDBC()) {
			throw new RuntimeException("Could not load JDBC driver");
		}
		
		if(!ConnectionUtil.testConnection(LOCATION, USER, PASSWORD)) {
			throw new RuntimeException("Could not connect to " + LOCATION);
		}
		
		char userInput = (char) 0;
		
		do {
			/* As long as the customer is not logged in, the application will keep asking */
			if(app.userLogged == null) {
				
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