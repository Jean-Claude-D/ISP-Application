package biz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public final class ConnectionUtil {
	public ConnectionUtil() {
	}
	
	public static Connection getConnection(String location, String user, String password) throws SQLException{
		return DriverManager.getConnection("jdbc:oracle:thin:@" + location + ":1521:xe", user, password);
	}
	
	public static boolean testConnection(String location, String user, String password) {
		try(Connection test = getConnection(location, user, password)) {
			return test != null && test.isValid(0);
		}
		catch(SQLException exc) {
			return false;
		}
	}
}