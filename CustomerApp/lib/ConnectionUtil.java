package lib;

import app.CustomerApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionUtil {
	public ConnectionUtil() {
	}
	
	public static Connection getConnection(boolean autoCommit) throws SQLException{
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@" + CustomerApp.LOCATION + ":1521:xe", CustomerApp.USER, CustomerApp.PASSWORD);
		
		conn.setAutoCommit(autoCommit);
		
		return conn;
	}
	
	public static Connection getConnection() throws SQLException{
		return getConnection(true);
	}
	
	public static boolean testConnection() {
		try(Connection test = getConnection()) {
			return test != null && test.isValid(0);
		}
		catch(SQLException exc) {
			return false;
		}
	}
	
	public static boolean rollback(Connection conn, int tryCount) {
		boolean success = false;
		
		while(!success && tryCount-- > 0) {
			try {
				conn.rollback();
				
				success = true;
			}
			catch(SQLException exc) {
			}
		}
		
		return success;
	}
}