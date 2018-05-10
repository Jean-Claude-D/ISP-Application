package biz;

import lib.ConnectionUtil;

public final class InternetPackageUtil {
	private InternetPackageUtil() {
	}
	
	public static InternetPackage[] getPackagesFor(String username) throws SQLException {
		try(Connection conn = ConnectionUtil.getConnection(false)) {
			
		}
		catch(SQLException exc) {
			throw exc;
		}
	}
}