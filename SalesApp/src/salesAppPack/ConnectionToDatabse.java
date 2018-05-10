package salesAppPack;

import java.sql.*;

public class ConnectionToDatabse {
	private Connection con;
	public ConnectionToDatabse() throws SQLException {
		if (con == null) {
			String user = "katsuragi";
			String password = "oumbarek123";
			String url = "jdbc:oracle:thin:@localHost:1521:XE";
			con = DriverManager.getConnection(url, user, password);
		}

	}
	
	public Connection getCon() {
		return con;
	}
}
