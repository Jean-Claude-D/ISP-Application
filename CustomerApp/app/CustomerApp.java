package app;

import java.sql.*;
import oracle.jdbc.driver.OracleDriver;

import biz.CustomerUtil;
import biz.Customer;

import lib.UserInputUtil;
import lib.ConnectionUtil;

import lib.SecurityUtil;

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
		String loginError = "Invalid username/password : denied";
		
		do {
			/* As long as the customer is not logged in, the application will keep asking */
			if(app.userLogged == null) {
				String username = UserInputUtil.getStringInput(
					"Please enter your username"
				);
				String password = UserInputUtil.getStringInput(
					"Please enter your password"
				);
				
				String salt = null;
				try {
					salt = CustomerUtil.getSalt(username);
				}
				catch(SQLException exc) {
				}
				
				if(salt != null) {
					byte[] hashedPassword = SecurityUtil.hash(
						password, salt, 32
					);
					
					try {
						app.userLogged = CustomerUtil.login(
							username, hashedPassword
						);
						System.out.println("logged");
					}
					catch(SQLException exc) {
						System.err.println(loginError);
					}
				}
				else {
					System.err.println(loginError);
				}
			}
			else {
				userInput = UserInputUtil.getValidInput(
					app.printMenu() + "Please enter your choice",
					"Must be a single character",
					(input) -> {return input.length() == 1;},
					(str) -> {return str.charAt(0);}
				);
				
				switch(userInput) {
					case '1':
						app.userLogged = null;
						break;
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
	
	private String printMenu() {
		return "";
	}
}