package lib;

import app.CustomerApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionUtil {
	public ConnectionUtil() {
	}
	
	public static Connection getConnection() throws SQLException{
		return DriverManager.getConnection("jdbc:oracle:thin:@" + CustomerApp.LOCATION + ":1521:xe", CustomerApp.USER, CustomerApp.PASSWORD);
	}
	
	public static boolean testConnection() {
		try(Connection test = getConnection()) {
			return test != null && test.isValid(0);
		}
		catch(SQLException exc) {
			return false;
		}
	}
}