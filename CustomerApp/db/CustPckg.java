package db;

import lib.ConnectionUtil;
import java.util.LinkedList;
import java.util.List;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.Types;
import oracle.jdbc.OracleTypes;
import java.sql.ResultSet;
import java.sql.Date;
import app.CustomerApp;
import java.sql.Statement;

/* Maps OracleDB functions to Java methods */
/* For information, see the package itself */
public final class CustPckg {
	/* Static class behaviour */
	private CustPckg() {
	}
	
	/* Using CUSTOMER_PCKG.GET_SALT */
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
	
	/* Using CUSTOMER_PCKG.LOGIN */
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
	
	/* Using CUSTOMER_PCKG.CHANGE_PASSWORD */
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
	
	/* Using CUSTOMER_PCKG.GET_AVAILABLE_PACKAGES and CUSTOMER_PCKG.GET_EXTRA_FEATS */
	/* Builds appropriate InternetPackage objects */
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
			throw exc;
		}
	}
	
	/* Using CUSTOMER_PCKG.UPGRADE_PACKAGE */
	public static boolean upgradePackage(String username, String packageName) throws SQLException {
		try(Connection conn = ConnectionUtil.getConnection(false)) {
			CallableStatement upgradeProc = conn.prepareCall(
				"{? = call CUSTOMER_PCKG.UPGRADE_PACKAGE(?,?)}"
			);
			upgradeProc.registerOutParameter(1, Types.INTEGER);
			upgradeProc.setString(2, username);
			upgradeProc.setString(2, packageName);
			
			upgradeProc.execute();
			return upgradeProc.getInt(1) != 0;
		}
		catch(SQLException exc) {
			throw exc;
		}
	}
	
	/* Using CUSTOMER_PCKG.GET_INVOICES and CUSTOMER_PCKG.GET_BALANCE */
	/* Builds appropriate Invoice objects */
	public static Invoice[] getInvoices(Customer cust) throws SQLException {
		try(Connection conn = ConnectionUtil.getConnection(false)) {
			CallableStatement invoiceFunc = conn.prepareCall(
				"{? = call CUSTOMER_PCKG.GET_INVOICES(?)}"
			);
			invoiceFunc.registerOutParameter(1, OracleTypes.CURSOR);
			invoiceFunc.setString(2, cust.username);
			
			CallableStatement balanceFunc = conn.prepareCall(
				"{? = call CUSTOMER_PCKG.GET_BALANCE(?,?,?)}"
			);
			balanceFunc.registerOutParameter(1, Types.REAL);
			balanceFunc.registerOutParameter(3, Types.REAL);
			balanceFunc.registerOutParameter(4, OracleTypes.CURSOR);
			
			Invoice[] invoiceArr = new Invoice[0];
			
			invoiceFunc.execute();
			try(ResultSet invoiceRS = (ResultSet) invoiceFunc.getObject(1)) {
				LinkedList<Invoice> invoices = new LinkedList<Invoice>();
				
				boolean first = true;
				Date startDate = null;
				List<ExtraFee> extraFees;
				
				while(invoiceRS.next()) {
					if(first) {
						first = false;
						startDate = cust.created;
					}
					else {
						startDate = invoices.getLast().endDate;
					}
					
					invoices.add(new Invoice(
						invoiceRS.getInt("ID"),
						invoiceRS.getString("INTERNET_PACKAGE"),
						startDate,
						invoiceRS.getDate("CREATED_DATE"),
						invoiceRS.getDate("DUE_DATE"),
						null
					));
					
					balanceFunc.setInt(2, invoices.getLast().id);
					balanceFunc.execute();
					
					try(ResultSet extraFeeRS = (ResultSet) balanceFunc.getObject(4)) {
						extraFees = new LinkedList<ExtraFee>();
						while(extraFeeRS.next()) {
							extraFees.add(new ExtraFee(
								extraFeeRS.getString("NAME"),
								extraFeeRS.getString("DESCRIPTION"),
								extraFeeRS.getDouble("COST")
							));
						}
					}
					
					invoices.getLast().balance = new Balance(
						balanceFunc.getDouble(3),
						balanceFunc.getDouble(1),
						extraFees
					);
				}
				
				invoices.toArray(invoiceArr);
				conn.commit();
			}
			catch(SQLException exc) {
				ConnectionUtil.rollback(conn, 15);
			}
			
			return invoiceArr;
		}
		catch(SQLException exc) {
			throw exc;
		}
	}
	
	/* Using CUSTOMER_PCKG.GET_USAGES */
	public static DailyUsage[] getUsages(String username) throws SQLException {
		try(Connection conn = ConnectionUtil.getConnection(false)) {
			CallableStatement getUsageFunc = conn.prepareCall(
				"{? = call CUSTOMER_PCKG.GET_USAGES(?)}"
			);
			getUsageFunc.registerOutParameter(1, OracleTypes.CURSOR);
			getUsageFunc.setString(2, username);
			
			getUsageFunc.execute();
			
			DailyUsage[] usageArr = new DailyUsage[0];
			try(ResultSet usagesRS = (ResultSet) getUsageFunc.getObject(1)) {
				LinkedList<DailyUsage> usages = new LinkedList<DailyUsage>();
				
				while(usagesRS.next()) {
					usages.add(new DailyUsage(
						usagesRS.getDouble("UPLOAD_GB"),
						usagesRS.getDouble("DOWNLOAD_GB"),
						usagesRS.getDate("DAY_USAGE")
					));
				}
				
				usages.toArray(usageArr);
			}
			catch(SQLException exc) {
				ConnectionUtil.rollback(conn, 15);
			}
			
			conn.commit();
			
			return usageArr;
		}
		catch(SQLException exc) {
			throw exc;
		}
	}
	
	/* Using CUSTOMER_PCKG.GET_PAYMENTS */
	public static Payment[] getPayments(int invoice) throws SQLException {
		try(Connection conn = ConnectionUtil.getConnection(false)) {
			CallableStatement getPaymentFunc = conn.prepareCall(
				"{? = call CUSTOMER_PCKG.GET_PAYMENTS(?)}"
			);
			getPaymentFunc.registerOutParameter(1, OracleTypes.CURSOR);
			getPaymentFunc.setInt(2, invoice);
			
			getPaymentFunc.execute();
			
			Payment[] paymentArr = new Payment[0];
			try(ResultSet paymentRS = (ResultSet) getPaymentFunc.getObject(1)) {
				LinkedList<Payment> payments = new LinkedList<Payment>();
				
				while(paymentRS.next()) {
					payments.add(new Payment(
						paymentRS.getDate("DATE_PAID"),
						paymentRS.getDouble("AMOUNT_PAID")
					));
				}
				
				payments.toArray(paymentArr);
			}
			catch(SQLException exc) {
				ConnectionUtil.rollback(conn, 15);
			}
			
			conn.commit();
			
			return paymentArr;
		}
		catch(SQLException exc) {
			throw exc;
		}
	}
	
	/* Using CUSTOMER_PCKG.PAY */
	public static boolean payInvoice(int invoice, double amount) throws SQLException {
		try(Connection conn = ConnectionUtil.getConnection(false)) {
			CallableStatement payFunc = conn.prepareCall(
				"{? = call CUSTOMER_PCKG.PAY(?,?)}"
			);
			payFunc.registerOutParameter(1, Types.INTEGER);
			payFunc.setInt(2, invoice);
			payFunc.setDouble(2, amount);
			
			payFunc.execute();
			return payFunc.getInt(1) != 0;
		}
		catch(SQLException exc) {
			throw exc;
		}
	}
	
	/* Using CUSTOMER_PCKG.GET_REMINDERS */
	public static Reminder[] getRemindersFor(String username, boolean seen) throws SQLException {
		try(Connection conn = ConnectionUtil.getConnection(false)) {
			CallableStatement getReminderFunc = conn.prepareCall(
				"{? = call CUSTOMER_PCKG.GET_REMINDERS(?)}"
			);
			getReminderFunc.registerOutParameter(1, OracleTypes.CURSOR);
			getReminderFunc.setString(2, username);
			
			getReminderFunc.execute();
			
			Reminder[] remindArr = new Reminder[0];
			try(ResultSet remindRS = (ResultSet) getReminderFunc.getObject(1)) {
				LinkedList<Reminder> reminders = new LinkedList<Reminder>();
				
				while(remindRS.next()) {
					reminders.add(new Reminder(
						remindRS.getInt("ID"),
						remindRS.getDate("EMISSION_DATE"),
						remindRS.getString("DETAILS")
					));
				}
				
				reminders.toArray(remindArr);
			}
			catch(SQLException exc) {
				ConnectionUtil.rollback(conn, 15);
			}
			
			conn.commit();
			
			return remindArr;
		}
		catch(SQLException exc) {
			throw exc;
		}
	}
	
	/* Using CUSTOMER_PCKG.SEE_REMINDER */
	public static boolean seeReminder(Connection conn, int reminder) throws SQLException {
		CallableStatement seeProc = conn.prepareCall(
			"{? = call CUSTOMER_PCKG.SEE_REMINDER(?,?)}"
		);
		seeProc.registerOutParameter(1, Types.INTEGER);
		seeProc.setInt(2, reminder);
		
		seeProc.execute();
		return seeProc.getInt(1) != 0;
	}
	
	/* Using CUSTOMER_PCKG.REQUEST_SERVICE */
	public static boolean requestService(String username, Date requestedDate, ServiceType service, String details) throws SQLException {
		try(Connection conn = ConnectionUtil.getConnection(false)) {
			CallableStatement serviceProc = conn.prepareCall(
			"{call CUSTOMER_PCKG.REQUEST_SERVICE(?,?,?,?)}"
			);
			serviceProc.setString(1, username);
			serviceProc.setDate(2, requestedDate);
			serviceProc.setString(3, service.getDept());
			serviceProc.setString(4, details);
			
			try {
				serviceProc.execute();
			}
			catch(SQLException exc) {
				ConnectionUtil.rollback(conn, 15);
				return false;
			}
			
			conn.commit();
			return true;
		}
		catch(SQLException exc) {
			throw exc;
		}
	}
	
	/* Using CUSTOMER_PCKG.CHECK_APPOINTMENTS */
	public static Appointment[] getAppointmentsFor(String username, boolean accepted) throws SQLException {
		try(Connection conn = ConnectionUtil.getConnection(false)) {
			CallableStatement getAppointFunc = conn.prepareCall(
				"{? = call CUSTOMER_PCKG.CHECK_APPOINTMENTS(?,?,?)}"
			);
			getAppointFunc.registerOutParameter(1, Types.ARRAY);
			getAppointFunc.setString(2, username);
			getAppointFunc.setBoolean(3, accepted);
			getAppointFunc.registerOutParameter(4, Types.ARRAY);
			
			Appointment[] appointArr = new Appointment[0];
			
			try {
				getAppointFunc.execute();
			}
			catch(SQLException exc) {
				ConnectionUtil.rollback(conn, 15);
				return appointArr;
			}
			
			conn.commit();
			
			Date[] scheduledDates = (Date[]) getAppointFunc.getArray(1).getArray();
			String[] detailsArr = (String[]) getAppointFunc.getArray(4).getArray();
			appointArr = new Appointment[detailsArr.length];
			
			for(int i = 0; i < appointArr.length; i++) {
				appointArr[i] = new Appointment(
					accepted,
					scheduledDates[i],
					detailsArr[i]
				);
			}
			
			return appointArr;
		}
		catch(SQLException exc) {
			throw exc;
		}
	}
}