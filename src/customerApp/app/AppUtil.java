package app;

import java.sql.*;
import oracle.jdbc.driver.OracleDriver;

import biz.BizUtil;

import lib.Customer;
import lib.DbLib;

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
	
	public static Customer login(String Username, String Password, String invalidLoginMessage) {
		RuntimeException wrongLogin = new RuntimeException(invalidLoginMessage);
		
		Connection conn = null;
		try {
			conn = getISPConnection();
			conn.setAutoCommit(false);
		}
		catch(SQLException exc) {
			throw new RuntimeException("login : Error while getting Connection");
		}
		
		CallableStatement saltQuery = null;
		try {
			String saltQueryStr = "{? = call CUSTOMER_PCKG.GET_SALT(?)}";
			saltQuery = conn.prepareCall(saltQueryStr);
			
			saltQuery.registerOutParameter(1, Types.VARCHAR);
			saltQuery.setString(2, Username);
		}
		catch(SQLException exc) {
			throw wrongLogin;
		}
		
		String userSalt = null;
		try {
			saltQuery.execute();
			
			userSalt = saltQuery.getString(1);
			
			conn.commit();
		}
		catch(SQLException exc) {
			throw wrongLogin;
		}
		
		String hashedPassword = DbLib.hash(Password, userSalt);
		
		CallableStatement loginCursor = null;
		try {
			loginCursor = conn.prepareCall("{? = call CUSTOMER_PCKG.LOGIN(?, ?)}");
			
			loginCursor.registerOutParameter(1, Types.OTHER);
			loginCursor.setString(2, Username);
			loginCursor.setString(3, hashedPassword);
		}
		catch(SQLException exc) {
			System.out.println("a");
			System.out.println(exc);
			/* here */
			throw wrongLogin;
		}
		
		Customer loggedCustomer = null;
		try {
			loginCursor.execute();
			ResultSet customerRS = (ResultSet) loginCursor.getObject(1);
			
			if(customerRS.next()) {
				loggedCustomer = new Customer(
					customerRS.getString("USERNAME"),
					customerRS.getString("PHONE"),
					customerRS.getString("EMAIL"),
					customerRS.getString("ADDRESS"),
					BizUtil.toLong(customerRS.getDate("CREATED_DATE"))
				);
			}
			
			if(customerRS.next()) {
				loggedCustomer = null;
			}
			
			conn.commit();
			return loggedCustomer;
		}
		catch(SQLException exc) {
			System.out.println("b");
			System.out.println(exc);
			/* here */
			throw wrongLogin;
		}
	}
}