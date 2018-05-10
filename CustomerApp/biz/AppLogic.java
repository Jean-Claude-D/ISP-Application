package biz;

import db.*;
import lib.*;
import app.*;
import biz.*;
import java.sql.*;

public final class AppLogic {
	private AppLogic() {
	}
	
	public static Customer login() {
		Customer logged = null;
		String username = UserInputUtil.getStringInput(
			"Please Enter your UserName"
		);
		String password = UserInputUtil.getStringInput(
			"Please Enter your Password"
		);
		
		String salt = null;
		try {
			salt = CustPckg.getSalt(username);
		}
		catch(SQLException exc) {
		}
		
		if(salt != null) {
			byte[] hashedPassword = SecurityUtil.hash(
				password, salt, 32
			);
			
			try {
				logged = CustPckg.login(
					username, hashedPassword
				);
			}
			catch(SQLException exc) {
			}
		}
		
		return logged;
	}
	
	public static boolean changePassword(Customer userLogged) {
		String salt = null;
		try {
			salt = CustPckg.getSalt(userLogged.username);
		}
		catch(SQLException exc) {
			System.err.println("Could not Perform that Action");
		}
		
		String password = UserInputUtil.getStringInput("Please Confirm your Password");
		byte[] hashedPassword = SecurityUtil.hash(password, salt, 32);
		
		String newPassword = UserInputUtil.getStringInput(
			"Please Enter your New Password"
		);
		String confirmPassword = UserInputUtil.getStringInput(
			"Please Confirm your New Password"
		);
		
		boolean changeSuccess = false;
		
		if(newPassword.equals(confirmPassword)) {
			String newSalt = SecurityUtil.getSalt(30);
			byte[] hashedNewPassword = SecurityUtil.hash(newPassword, newSalt, 32);
			
			try {
				changeSuccess = CustPckg.changePassword(userLogged.username, hashedPassword, hashedNewPassword, newSalt);
			}
			catch(SQLException exc) {
				System.out.println(exc);
			}
		}
		else {
			System.err.println("Password do not Match");
		}
		
		return changeSuccess;
	}
	
	public static void printPackages(Customer userLogged) {
		try {
			InternetPackage[] packs = CustPckg.getPackagesFor(userLogged.username);
			
			if(packs.length == 0) {
				System.out.println("No Internet Package Available");
			}
			else {
				for(InternetPackage pack : packs) {
					System.out.println(pack);
				}
			}
		}
		catch(SQLException exc) {
			System.out.println("Could not Retrieve Internet Packages");
		}
	}
	
	public static void upgradePackage(Customer userLogged) {
		String packageName = UserInputUtil.getStringInput(
			"Please Enter the Name of the Internet Package you Want"
		);
		
		try {
			boolean upgraded = CustPckg.upgradePackage(userLogged.username, packageName);
			if(upgraded) {
				System.out.println("Upgrade Successful");
			}
			else {
				System.out.println("Upgrade Not Successful");
			}
		}
		catch(SQLException exc) {
			System.out.println("Could not Perform the Upgrade");
		}
	}
	
	public static void printInvoices(Customer userLogged) {
		try {
			Invoice[] invoices = CustPckg.getInvoices(userLogged);
			
			if(invoices.length == 0) {
				System.out.println("No Invoices Available");
			}
			else {
				for(Invoice invoice : invoices) {
					System.out.println(invoice);
				}
			}
		}
		catch(SQLException exc) {
			System.out.println("Could not Retrieve Invoices");
		}
	}
	
	public static void printUsage(Customer userLogged) {
		try {
			DailyUsage[] usages = CustPckg.getUsages(userLogged.username);
			
			if(usages.length == 0) {
				System.out.println("No Usage Available");
			}
			else {
				for(DailyUsage usage : usages) {
					System.out.println(usage);
				}
			}
		}
		catch(SQLException exc) {
			System.out.println("Could not Retrieve Daily Usages");
		}
	}
	
	public static void printPayments() {
		int invoice = UserInputUtil.getValidInput(
			"Please Enter the Invoice ID for which you want to view the Payments",
			"You Must enter a valid Integer",
			(input) -> {return input.matches("-?\\d+");},
			(input) -> {return Integer.parseInt(input);}
		);
		
		try {
			Payment[] payments = CustPckg.getPayments(invoice);
			
			if(payments.length == 0) {
				System.out.println("No Payment Available");
			}
			else {
				for(Payment payment : payments) {
					System.out.println(payment);
				}
			}
		}
		catch(SQLException exc) {
			System.out.println("Could not Retrieve Payments");
		}
	}
	
	public static void payInvoice() {
		int invoice = UserInputUtil.getValidInput(
			"Please Enter the Invoice ID you want to pay",
			"You Must enter a valid Integer",
			(input) -> {return input.matches("-?\\d+");},
			(input) -> {return Integer.parseInt(input);}
		);
		
		double amount = UserInputUtil.getValidInput(
			"Please Enter the Amount",
			"You Must enter a valid Number",
			(input) -> {return input.matches("\\d+(\\.\\d+)?");},
			(input) -> {return Double.parseDouble(input);}
		);
		
		try {
			if(CustPckg.payInvoice(invoice, amount)) {
				System.out.println("Payment Successful");
			}
			else {
				System.out.println("Could not Pay the Invoice");
			}
		}
		catch(SQLException exc) {
			System.err.println("Error During the Payment, Rolled back");
		}
	}
	
	public static void printInitReminders(Customer userLogged) {
		try {
			Reminder[] reminders = CustPckg.getRemindersFor(userLogged.username, false);
			
			if(reminders.length == 0) {
				System.out.println("No New Reminder");
			}
			else {
				Connection conn = ConnectionUtil.getConnection(false);
				
				for(Reminder reminder : reminders) {
					CustPckg.seeReminder(conn, reminder.id);
					System.out.println(reminder);
				}
				
				conn.commit();
				conn.close();
			}
		}
		catch(SQLException exc) {
			System.out.println("Could not Retrieve Reminders");
		}
	}
	
	public static void printReminders(Customer userLogged) {
		try {
			Reminder[] oldreminders = CustPckg.getRemindersFor(userLogged.username, true);
			
			if(oldreminders.length == 0) {
				System.out.println("No Reminders");
			}
			else {
				for(Reminder reminder : oldreminders) {
					System.out.println(reminder);
				}
			}
		}
		catch(SQLException exc) {
			System.out.println("Could not Retrieve Reminders");
		}
	}
	
	public static void requestAService(Customer userLogged) {
		Date requested = UserInputUtil.getValidDate(
			"Enter the Date at which you want the service"
		);
		
		String details = UserInputUtil.getStringInput(
			"Please Enter any Detail"
		);
		
		try {
			if(CustPckg.requestService(userLogged.username, requested, ServiceType.TECHNICAL_HELP, details)) {
				System.out.println("Succesfull request");
			}
			else {
				System.err.println("Error : Could not process the Request");
			}
		}
		catch(SQLException exc) {
			System.err.println("Error : Could not process the Request");
		}
	}
	
	public static void seeAcceptedAppointments(Customer userLogged) {
		try {
			Appointment[] appoints = CustPckg.getAppointmentsFor(userLogged.username, true);
			
			if(appoints.length == 0) {
				System.out.println("No Accepted Appointments");
			}
			else {
				for(Appointment appoint : appoints) {
					System.out.println(appoint);
				}
			}
		}
		catch(SQLException exc) {
			System.out.println("Could not Retrieve Appointments");
		}
	}
}