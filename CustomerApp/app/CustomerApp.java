package app;

import java.sql.*;
import oracle.jdbc.driver.OracleDriver;

import biz.Customer;
import biz.CustomerUtil;
import biz.InternetPackage;
import biz.InternetPackageUtil;

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
					case '2':
						app.changePassword();
						break;
					case '3':
						app.printPackages();
						break;
					case '4':
						app.upgradePackage();
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
	
	private void changePassword() {
		String salt = null;
		try {
			salt = CustomerUtil.getSalt(this.userLogged.username);
		}
		catch(SQLException exc) {
			System.err.println("Could not perform the action");
		}
		
		String password = UserInputUtil.getStringInput(
			"Please enter your old password"
		);
		byte[] hashedPassword = SecurityUtil.hash(password, salt, 32);
		
		String newPassword = UserInputUtil.getStringInput(
			"Please enter your new password"
		);
		String confirmPassword = UserInputUtil.getStringInput(
			"Please confirm your new password"
		);
		
		if(newPassword.equals(confirmPassword)) {
			String newSalt = SecurityUtil.getSalt(30);
			byte[] hashedNewPassword = SecurityUtil.hash(newPassword, newSalt, 32);
			
			boolean changeSuccess = false;
			try {
				changeSuccess = CustomerUtil.changePassword(this.userLogged.username, hashedPassword, hashedNewPassword, newSalt);
			}
			catch(SQLException exc) {
				System.out.println(exc);
			}
			
			if(changeSuccess) {
				System.out.println("Change succeeded");
			}
			else {
				System.out.println("Change failed");
			}
		}
		else {
			System.err.println("Passwords do not match");
		}
	}
	
	private void printPackages() {
		try {
			InternetPackage[] packs = InternetPackageUtil.getPackagesFor(userLogged.username);
			
			if(packs.length == 0) {
				System.out.println("No internet package available");
			}
			else {
				for(InternetPackage pack : packs) {
					System.out.println(pack);
				}
			}
		}
		catch(SQLException exc) {
			System.out.println("Could not retrieve internet packages");
		}
	}
	
	private void upgradePackage() {
		String packageName = UserInputUtil.getStringInput(
			"Please enter the package you want to get"
		);
		
		try {
			boolean upgraded = InternetPackageUtil.upgradePackage(userLogged.username, packageName);
			if(upgraded) {
				System.out.println("Upgrade successful");
			}
			else {
				System.out.println("Upgrade unsuccessful");
			}
		}
		catch(SQLException exc) {
			System.out.println("Could not perform the upgrade");
		}
	}
}









