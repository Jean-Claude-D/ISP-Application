package biz;

import app.CustomerApp;

import lib.ConnectionUtil;

import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.Types;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.Statement;

public final class CustomerUtil {
	private CustomerUtil() {
	}
	
	public static String getSalt(String username) throws SQLException {
		try(Connection conn = ConnectionUtil.getConnection(false)) {
			CallableStatement getSaltFunc = conn.prepareCall(
				"{? = call CUSTOMER_PCKG.GET_SALT(?)}"
			);
			getSaltFunc.registerOutParameter(1, Types.VARCHAR);
			getSaltFunc.setString(2, username);
			
			String foundSalt = null;
			try {
				getSaltFunc.execute();
				foundSalt = getSaltFunc.getString(1);
			}
			catch(SQLException exc) {
				/* If execution goes wrong, means could not get the salt */
			}
			
			conn.commit();
			
			return foundSalt;
		}
		catch(SQLException exc) {
			throw exc;
		}
	}
	
	public static Customer login(String username, byte[] hashedPassword) throws SQLException {
		try(Connection conn = ConnectionUtil.getConnection(false)) {
			CallableStatement loginFunc = conn.prepareCall(
				"{? = call CUSTOMER_PCKG.LOGIN(?,?,?,?,?,?,?,?)}"
			);
			loginFunc.registerOutParameter(1, Types.VARCHAR);
			loginFunc.setString(2, username);
			loginFunc.setBytes(3, hashedPassword);
			loginFunc.registerOutParameter(4, Types.VARCHAR);
			loginFunc.registerOutParameter(5, Types.VARCHAR);
			loginFunc.registerOutParameter(6, Types.VARCHAR);
			loginFunc.registerOutParameter(7, Types.VARCHAR);
			loginFunc.registerOutParameter(8, Types.VARCHAR);
			loginFunc.registerOutParameter(9, Types.DATE);
			
			Customer loggedCust = null;
			try {
				loginFunc.execute();
				
				loggedCust = new Customer(
					loginFunc.getString(1),
					loginFunc.getString(4),
					loginFunc.getString(5),
					loginFunc.getString(6),
					loginFunc.getString(7),
					loginFunc.getString(8),
					loginFunc.getDate(9)
				);
			}
			catch(SQLException exc) {
			}
			
			conn.commit();
			
			return loggedCust;
		}
		catch(SQLException exc) {
			throw exc;
		}
	}
	
	public static boolean changePassword(String customer, byte[] hashedPassword, byte[] newPassword, String salt) throws SQLException {
		try(Connection conn = ConnectionUtil.getConnection(false)) {
			CallableStatement changeFunc = conn.prepareCall(
				"{? = call CUSTOMER_PCKG.CHANGE_PASSWORD(?,?,?,?)}"
			);
			changeFunc.registerOutParameter(1, Types.INTEGER);
			changeFunc.setString(2, customer);
			changeFunc.setBytes(3, newPassword);
			changeFunc.setString(4, salt);
			changeFunc.setBytes(5, hashedPassword);
			
			boolean success = false;
			
			try {
				changeFunc.execute();
				
				success = changeFunc.getInt(1) != 0;
			}
			catch(SQLException exc) {
				System.out.println(exc);
			}
			
			if(success) {
				conn.commit();
			}
			else {
				ConnectionUtil.rollback(conn, 15);
			}
			
			return success;
		}
		catch(SQLException exc) {
			throw exc;
		}
	}
}