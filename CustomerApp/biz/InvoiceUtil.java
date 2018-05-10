package biz;

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

public final class InvoiceUtil {
	private InvoiceUtil() {
	}
	
	public Invoice[] getInvoices(Customer cust) throws SQLException {
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
	
	public DailyUsage[] getUsages(String username) throws SQLException {
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
	
	public Payment[] getPayments(int invoice) throws SQLException {
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
	
	public boolean payInvoice(int invoice, double amount) throws SQLException {
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
}