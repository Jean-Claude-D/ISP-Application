package biz;

import lib.ConnectionUtil;

import java.util.LinkedList;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.CallableStatement;
import oracle.jdbc.OracleTypes;
import java.sql.ResultSet;

public final class InternetPackageUtil {
	private InternetPackageUtil() {
	}
	
	public static InternetPackage[] getPackagesFor(String username) throws SQLException {
		try(Connection conn = ConnectionUtil.getConnection(false)) {
			CallableStatement packageFunc = conn.prepareCall(
				"{? = call CUSTOMER_PCKG.GET_AVAILABLE_PACKAGES(?)}"
			);
			packageFunc.registerOutParameter(1, OracleTypes.CURSOR);
			packageFunc.setString(2, username);
			
			CallableStatement extraFeatFunc = conn.prepareCall(
				"{? = call CUSTOMER_PCKG.GET_EXTRA_FEATS(?)}"
			);
			
			InternetPackage[] internetPackageArr = new InternetPackage[0];
			
			packageFunc.execute();
			try(ResultSet packagesRS = (ResultSet) packageFunc.getObject(1)) {
				LinkedList<InternetPackage> packages = new LinkedList<InternetPackage>();
				
				while(packagesRS.next()) {
					packages.add(new InternetPackage(
						packagesRS.getString("NAME"),
						packagesRS.getString("DESCRIPTION"),
						packagesRS.getDouble("SPEED_UPLOAD_MBPS"),
						packagesRS.getDouble("SPEED_DOWNLOAD_MBPS"),
						packagesRS.getDouble("BANDWIDTH_GB"),
						packagesRS.getDouble("MONTHLY_PRICE"),
						packagesRS.getDouble("OVERAGE_COST_PGB"),
						null
					));
					
					/* for each internet package, add all of its extra feature */
					extraFeatFunc.registerOutParameter(1, OracleTypes.CURSOR);
					extraFeatFunc.setString(2, packages.getLast().name);
					extraFeatFunc.execute();
					try(ResultSet extraFeatRS = (ResultSet) extraFeatFunc.getObject(1)) {
						while(extraFeatRS.next()) {
							packages.getLast().addExtraFeature(new ExtraFeature(
								extraFeatRS.getString("NAME"),
								extraFeatRS.getString("DESCRIPTION"),
								extraFeatRS.getDouble("MONTHLY_PRICE"),
								extraFeatRS.getString("PER_GB").charAt(0) != '0'
							));
						}
					}
				}
				
				packages.toArray(internetPackageArr);
				conn.commit();
			}
			catch(SQLException exc) {
				ConnectionUtil.rollback(conn, 15);
			}
			
			return internetPackageArr;
		}
		catch(SQLException exc) {
			System.out.println(exc);
			throw exc;
		}
	}
}