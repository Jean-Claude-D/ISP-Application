package biz;

import lib.ConnectionUtil;

import java.util.LinkedList;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.Types;
import oracle.jdbc.OracleTypes;
import java.sql.ResultSet;
import java.sql.Date;

public final class ServiceUtil {
	private ServiceUtil() {
	}
	
	public Reminder[] getRemindersFor(String username, boolean seen) throws SQLException {
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
	
	public boolean seeReminder(Connection conn, int reminder) throws SQLException {
		CallableStatement seeProc = conn.prepareCall(
			"{? = call CUSTOMER_PCKG.SEE_REMINDER(?,?)}"
		);
		seeProc.registerOutParameter(1, Types.INTEGER);
		seeProc.setInt(2, reminder);
		
		seeProc.execute();
		return seeProc.getInt(1) != 0;
	}
	
	public boolean requestService(String username, Date requestedDate, ServiceType service, String details) throws SQLException {
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
	
	public Appointment[] getAppointmentsFor(String username, boolean accepted) throws SQLException {
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