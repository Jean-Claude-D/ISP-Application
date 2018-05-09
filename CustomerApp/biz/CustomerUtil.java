package biz;

import app.CustomerApp;

import lib.ConnectionUtil;

import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.Types;
import java.sql.Connection;

public final class CustomerUtil {
	private CustomerUtil() {
	}
	
	public static String getSalt(CustomerApp app, String username) throws SQLException {
		try(Connection conn = ConnectionUtil.getConnection()) {
			conn.setAutoCommit(false);
			
			CallableStatement getSaltFunc = conn.prepareCall(
				"{? = call CUSTOMER_PCKG.GET_SALT(?)}"
			);
			getSaltFunc.registerOutParameter(1, Types.VARCHAR);
			getSaltFunc.setString(2, username);
			
			getSaltFunc.execute();
			conn.commit();
			
			return getSaltFunc.getString(1);
		}
		catch(SQLException exc) {
			throw exc;
		}
	}
}