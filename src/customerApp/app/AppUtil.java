package app;

import java.sql.*;
import oracle.jdbc.driver.OracleDriver;

public final class AppUtil {
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String UNAME = "DEMO_CUSTOMER";
	private static final String PASSWD = "UZHaGAzgEd3Kp7LjpXi";
	
	private static final String CLASS_TO_LOAD = "oracle.jdbc.driver.OracleDriver";
	
	private AppUtil() {
	}
	
	public static Connection getISPConnection() throws SQLException {
		return DriverManager.getConnection(URL, UNAME, PASSWD);
	}
	
	public static void loadJDBC() throws ClassNotFoundException {
		Class.forName(CLASS_TO_LOAD);
	}
}