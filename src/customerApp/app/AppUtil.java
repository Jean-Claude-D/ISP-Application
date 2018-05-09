package app;

import java.sql.*;
import oracle.jdbc.driver.OracleDriver;

import biz.BizUtil;

import lib.Customer;
import lib.DbLib;
import lib.CustomerPack;

public final class AppUtil {
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String UNAME = "JC";
	private static final String PASSWD = "Hydral1sk";
	/*private static final String UNAME = "DEMO_CUSTOMER";
	private static final String PASSWD = "UZHaGAzgEd3Kp7LjpXi";*/
	
	private static final String CLASS_TO_LOAD = "oracle.jdbc.driver.OracleDriver";
	
	private AppUtil() {
	}
	
	public static boolean testConnection() {
		try {
			getISPConnection();
		}
		catch(SQLException exc) {
			return false;
		}
		
		return true;
	}
	
	public static void loadJDBC() throws ClassNotFoundException {
		Class.forName(CLASS_TO_LOAD);
	}
	
	private static Connection getISPConnection() throws SQLException {
		return DriverManager.getConnection(URL, UNAME, PASSWD);
	}
	
	public static Customer login() {
		String Username = BizUtil.getValidUserInput(
			"Please enter your username",
			"You must enter a maximum of " + Customer.USERNAME_MAX_LENGTH + " characters",
			(String input) -> {return Customer.isValidUsername(input);},
			(String validInput) -> {return validInput;}
		);
		
		String Password = BizUtil.getValidUserInput(
			"Please enter your password",
			"Invalid password",
			(String input) -> {return true;},
			(String validInput) -> {return validInput;}
		);
		
		Connection conn = null;
		try {
			conn = getISPConnection();
		}
		catch(SQLException exc) {
			System.err.println("login : Error while getting Connection");
			return null;
		}
		
		String userSalt = CustomerPack.getSalt(conn, Username);
		
		Customer loggedCustomer = CustomerPack.login(
			conn,
			Username,
			DbLib.hash(Password, userSalt)
		);
		
		return loggedCustomer;
	}
}