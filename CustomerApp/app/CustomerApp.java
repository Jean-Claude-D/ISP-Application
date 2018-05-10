package app;

import java.sql.*;
import oracle.jdbc.driver.OracleDriver;
import db.*;
import lib.*;
import app.*;
import biz.*;

public final class CustomerApp {
	public static final String LOCATION = "localhost";
	public static final String USER = "JC";
	public static final String PASSWORD = "Hydral1sk";
	
	private CustomerApp() {
	}
	public Customer userLogged = null;
	
	public static void main(String[] args) {
		CustomerApp app = new CustomerApp();
		
		if(!loadJDBC()) {
			throw new RuntimeException("Could not load JDBC driver");
		}
		
		if(!ConnectionUtil.testConnection()) {
			throw new RuntimeException("Could not connect to " + LOCATION);
		}
		
		char userInput = (char) 0;
		
		do {
			/* As long as the customer is not logged in, the application will keep asking */
			if(app.userLogged == null) {
				app.userLogged = AppLogic.login();
				
				if(app.userLogged == null) {
					System.out.println("Login Successful");
				}
				else {
					System.out.println("Login Denied");
				}
			}
			else {
				AppLogic.printInitReminders(app.userLogged);
				
				userInput = UserInputUtil.getValidInput(
					app.printMenu() + "Please enter your choice",
					"Must be a single character",
					(input) -> {return input.length() == 1;},
					(str) -> {return str.charAt(0);}
				);
				
				switch(userInput) {
					case '1':
						app.userLogged = null;
						break;
					case '2':
						AppLogic.changePassword(app.userLogged);
						break;
					case '3':
						AppLogic.printPackages(app.userLogged);
						break;
					case '4':
						AppLogic.upgradePackage(app.userLogged);
						break;
					case '5':
						AppLogic.printInvoices(app.userLogged);
						break;
					case '6':
						AppLogic.printUsage(app.userLogged);
						break;
					case '7':
						AppLogic.printPayments();
						break;
					case '8':
						AppLogic.payInvoice();
						break;
					case '9':
						AppLogic.printReminders(app.userLogged);
						break;
					case 'A':
						AppLogic.requestAService(app.userLogged);
						break;
					case 'B':
						AppLogic.seeAcceptedAppointments(app.userLogged);
						break;
					case 'q':
						System.out.println("Goodbye!");
						break;
					default:
						System.err.println("Invalid choice : " + userInput);
				}
			}
		}while(userInput != 'q');
	}
	
	private static boolean loadJDBC() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			return true;
		}
		catch(ClassNotFoundException exc) {
			return false;
		}
	}
	
	private String printMenu() {
		return
		"1\tLogoff\n" +
		"2\tChange Password\n" +
		"3\tView Available Packages\n" +
		"4\tUpgrade Package\n" +
		"5\tPrint your Invoices\n" +
		"6\tPrint your Usage\n" +
		"7\tPrint your Previous Payments\n" +
		"8\tPay an Invoice\n" +
		"9\tPrint Reminders addressed to you\n" +
		"A\tRequest Technical Support\n" +
		"B\tSee all Confirmed Appointments\n" +
		"q\tExit the Application";
	}
}









