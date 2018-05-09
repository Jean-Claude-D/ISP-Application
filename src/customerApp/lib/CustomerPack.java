package lib;

import java.sql.*;
import biz.BizUtil;

public final class CustomerPack {
	private CustomerPack() {
	}
	
	public static void main(String[] args) {
		
	}
	
	public static String getSalt(Connection conn, Customer customer) {
		return getSalt(conn, customer.getUsername());
	}
	
	public static String getSalt(Connection conn, String username) {
		CallableStatement getSaltCall = null;
		try {
			conn.setAutoCommit(false);
			/* Prepare the call */
			getSaltCall = conn.prepareCall(
				"{? = call CUSTOMER_PCKG.GET_SALT(?)}"
			);
			getSaltCall.registerOutParameter(1, Types.VARCHAR);
			getSaltCall.setString(2, username);
		}
		catch(SQLException exc) {
			throw new RuntimeException("getSalt : Preparing callable statement");
		}
		
		String foundSalt = null;
		try {
			/* Call the method */
			getSaltCall.execute();
			
			/* Retrieve the result */
			foundSalt = getSaltCall.getString(1);
		}
		catch(SQLException exc) {
			System.out.println(exc);
			return null;
		}
		
		try {
			conn.commit();
		}
		catch(SQLException exc) {
			try {
				conn.rollback();
			}
			catch(SQLException innerExc) {
				innerExc.initCause(exc);
				throw new RuntimeException("getSalt : Rollback failed", innerExc);
			}
			
			throw new RuntimeException("getSalt : Committing failed");
		}
		
		return foundSalt;
	}
	
	public static Customer login(Connection conn, String username, byte[] hashedPassword) {
		CallableStatement loginCall = null;
		try {
			conn.setAutoCommit(false);
			/* Prepare the call */
			loginCall = conn.prepareCall(
				"{? = call CUSTOMER_PCKG.LOGIN(?,?,?,?,?,?,?,?)}"
			);
			loginCall.registerOutParameter(1, Types.VARCHAR);
			loginCall.setString(2, username);
			loginCall.setBytes(3, hashedPassword);
			loginCall.registerOutParameter(4, Types.VARCHAR);
			loginCall.registerOutParameter(5, Types.VARCHAR);
			loginCall.registerOutParameter(6, Types.VARCHAR);
			loginCall.registerOutParameter(7, Types.VARCHAR);
			loginCall.registerOutParameter(8, Types.VARCHAR);
			loginCall.registerOutParameter(9, Types.DATE);
		}
		catch(SQLException exc) {
			throw new RuntimeException("login : Preparing callable statement");
		}
		
		try {
			/* Call the method */
			loginCall.execute();
		}
		catch(SQLException exc) {
			System.out.println("Exception :\n");
			Throwable excCause = exc;
			while(excCause != null) {
				System.out.println("Exc : " + excCause.toString());
				for(StackTraceElement trace : excCause.getStackTrace()) {
					System.out.println(trace.toString());
				}
				
				excCause = excCause.getCause();
			}
			return null;
		}
		
		try {
			/* Retrieve the result */
			Customer logged = new Customer(
				loginCall.getString(1),
				loginCall.getString(4),
				loginCall.getString(5),
				loginCall.getString(6),
				loginCall.getString(7),
				loginCall.getString(8),
				BizUtil.toLong(loginCall.getDate(9))
			);
			
			conn.commit();
			return logged;
		}
		catch(IllegalArgumentException | SQLException exc) {
			try {
				conn.rollback();
			}
			catch(SQLException innerExc) {
				return null;
			}
			
			return null;
		}
	}
	
	public static  boolean changePassword(Connection conn, String username, byte[] confirmPassword,
	String newSalt, byte[] newPassword) throws SQLException {
		conn.setAutoCommit(false);
		/* Prepare the call */
		CallableStatement passwChangeCall = conn.prepareCall(
			"{call CUSTOMER_PCKG.CHANGE_PASSWORD(?,?,?,?)}"
		);
		passwChangeCall.setString(1, username);
		passwChangeCall.setBytes(2, newPassword);
		passwChangeCall.setString(3, newSalt);
		passwChangeCall.setBytes(4, confirmPassword);
		
		try {
			/* Call the method */
			passwChangeCall.execute();
		}
		catch(SQLException exc) {
			/* Beware : this may throw */
			conn.rollback();
			return false;
		}
		
		conn.commit();
		return true;
	}
	
	public static  InternetPackage[] getAvailablePackages(Connection conn, Customer customer) throws SQLException {
		return getAvailablePackages(conn, customer.getUsername());
	}
	
	public static InternetPackage[] getAvailablePackages(Connection conn, String username) throws SQLException {
		conn.setAutoCommit(false);
		/* Prepare the call */
		CallableStatement getPackageCall = conn.prepareCall(
			"{? = call CUSTOMER_PCKG.GET_AVAILABLE_PACKAGES(?,?,?,?,?,?,?)}"
		);
		getPackageCall.registerOutParameter(1, Types.ARRAY);
		getPackageCall.setString(2, username);
		getPackageCall.registerOutParameter(3, Types.ARRAY);
		getPackageCall.registerOutParameter(4, Types.ARRAY);
		getPackageCall.registerOutParameter(5, Types.ARRAY);
		getPackageCall.registerOutParameter(6, Types.ARRAY);
		getPackageCall.registerOutParameter(7, Types.ARRAY);
		getPackageCall.registerOutParameter(8, Types.ARRAY);
		
		/* Call the method */
		getPackageCall.execute();
		
		return null;
	}
}























