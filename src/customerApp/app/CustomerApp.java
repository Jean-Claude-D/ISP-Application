package app;

import java.sql.*;

public final class CustomerApp {
	private CustomerApp() {
	}
	
	public static void main(String[] args) {
		CustomerApp app = new CustomerApp();
		app.init();
		
		
	}
	
	private void init() {
		try {
			AppUtil.loadJDBC();
		}
		catch(ClassNotFoundException exc) {
			throw new RuntimeException("Error while loading the OJDBC", exc);
		}
		
		try {
			AppUtil.getISPConnection();
		}
		catch(SQLException exc) {
			throw new RuntimeException("Error during initial connection test to the database", exc);
		}
	}
}