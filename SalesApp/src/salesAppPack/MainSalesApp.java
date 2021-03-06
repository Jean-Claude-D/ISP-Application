package salesAppPack;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.zip.DataFormatException;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class MainSalesApp {
	private static SecureRandom rand = new SecureRandom();
	/*
	 * Main console where the sales rep will pass his login credentials
	 */
	
	public static void main(String[] args) throws SQLException {
		ConnectionToDatabse x = new ConnectionToDatabse();
		Connection c = x.getCon();
		//generateRepresentatives(c);
		Scanner reader = new Scanner(System.in); 
		System.out.println("Enter your sales username ");
		String uname = reader.nextLine();
		System.out.println("Enter you password ");
		String pass = reader.nextLine();
		boolean valid = validSalesRep(uname,pass,c);
		while(!valid) {
			System.out.println("Enter your sales username ");
			uname = reader.nextLine();
			System.out.println("Enter you password ");
			pass = reader.nextLine();
			valid = validSalesRep(uname,pass,c);
		}
		menu(reader,uname);
		reader.close();
				
	}
	/*
	 * THis is the menu that the sales rep will be prompted after entering valid input
	 * He will be able to add new customer
	 * Change user Internet Package
	 * Do cold calls for sales
	 * And modify their own passwords
	 */
	public static void menu(Scanner reader,String username) throws SQLException {
		boolean validChoiceRange = true;
		ConnectionToDatabse x = new ConnectionToDatabse();
		Connection c = x.getCon();
		while(validChoiceRange) {
			System.out.println("------ Choose an Option Among the Following ------");
			System.out.println("------ 1-Choose to register new Customer ------");
			System.out.println("------ 2-Change User Internet Package ------");
			System.out.println("------ 3-Cold Call for sales ------");
			System.out.println("------ 4-Modify your password ------");
			System.out.println("------ 5-Exit Application ------");
			String userChoice = reader.nextLine();
			switch(userChoice) {
				case "1":
					addNewCustomer(reader,username);
					validChoiceRange = true;
					break;
				case "2":
					changeUserPackage(reader);
					validChoiceRange = true;
					break;
				case "3":
					generateColdCall(reader,username);
					validChoiceRange = true;
					break;
				case "4":
					changeUserPassword(reader, username);
					validChoiceRange = true;
					break;
				case "5":
					validChoiceRange = false;
					break;
				default:
					System.out.println("Please enter a valid input");
					break;
			}
			
		}
	}
	/*
	 * This method is just used to change the users password
	 */
	public static void changeUserPassword(Scanner reader, String username) throws SQLException {
		ConnectionToDatabse x = new ConnectionToDatabse();
		Connection c = x.getCon();
		System.out.println("Enter you password ");
		String pass = reader.nextLine();
		String salt = getSalt();
		byte [] hash = hash(pass, salt);
		CallableStatement callableStatement = c.prepareCall("{call salesAppPack.updatePasswordRep(?,?,?)}");
		callableStatement.setString(1, username);
		callableStatement.setBytes(2, hash);
		callableStatement.setString(3, salt);
		callableStatement.execute();
	}
	
	
	/*
	 * This method will generate a random number that the sales will be able to call and tell if it was a successful call or not
	 */
	public static void generateColdCall(Scanner reader,String repName) throws SQLException {
		ConnectionToDatabse x = new ConnectionToDatabse();
		Connection c = x.getCon();
		String [] telephoneArea = {"514","438","450"};
		Random random = new Random();
		int valueThreeDigit = random.nextInt(900) + 100;
		int valueFourDigit = random.nextInt(10000) + 1000;
		String fourDigit = String.format("%04d", valueFourDigit);
		String threeDigit = String.format("%03d", valueThreeDigit);
		
		System.out.println("------ You are calling the following number  :" +telephoneArea[1] + "-"  +threeDigit + "-"  +fourDigit);
		boolean invalidInput = true;
		String sucess= "1";
	    Date ourJavaDateObject = new Date(Calendar.getInstance().getTime().getTime());
	    
		while(invalidInput) {
			System.out.println("------ Did this call result in a successful call? Yes/No ------");
			String userInput = reader.nextLine();
			switch(userInput) {
			case "Yes":
				invalidInput = false;
				break;
			case "No":
				invalidInput = false;
				sucess="0";
				break;
			default:
				System.out.println("------ Please enter either Yes or No ------");
				break;
			}
		}
		CallableStatement callableStatement = c.prepareCall("{call salesAppPack.insertRecruitingCall(?,?,?)}");
		callableStatement.setString(1, repName);
		callableStatement.setDate(2, ourJavaDateObject);
		callableStatement.setString(3,  sucess);
		callableStatement.execute();
		
	}
	/*
	 * Method that will add a new customer to the database
	 */
	public static void addNewCustomer(Scanner reader,String repName) throws SQLException {
			ConnectionToDatabse x = null;
			Connection c = null;
			CallableStatement callableStatement= null;
			CallableStatement callableStatement2 = null;
			try {
				x = new ConnectionToDatabse();
			    c = x.getCon();
				System.out.println("-------- Enter the customer username ");
				String userName = reader.nextLine();
				System.out.println("-------- Enter the customer password ");
				String userPassword = reader.nextLine();
				System.out.println("-------- Enter the customer firstname");
				String fName = reader.nextLine();
				System.out.println("-------- Enter the customer lastname");
				String lName = reader.nextLine();
				System.out.println("-------- Enter the customer phone");
				String phoneNumber = reader.nextLine();
				boolean correctPhoneNumber = phoneNumber.matches("^(\\d){3}[-](\\d){3}[-](\\d){4}$");
				while(!correctPhoneNumber) {
					System.out.println("-------- Enter the customer phone(Must use the following Format(XXX-XXX-XXXX)");
				    phoneNumber = reader.nextLine();
					correctPhoneNumber = phoneNumber.matches("^(\\d){3}[-](\\d){3}[-](\\d){4}$");
				}
				System.out.println("-------- Enter customer email");
				String email = reader.nextLine();
				boolean correctEmail = email.matches("^.*@[A-Z a-z]{1,}[.][a-z A-Z]{2,3}");
				while(!correctEmail) {
					System.out.println("-------- Enter customer email (Must use the following format(XXXX@XXXXX.XXX))");
					email = reader.nextLine();
					correctEmail = email.matches("^.*@[A-Z a-z]{1,}[.][a-z A-Z]{2,3}");
				}
				System.out.println("-------- Enter customer address ( Must use the following format - (Add.Num-StreetName-StreetType(Boul, Street,etc.)");
				String address = reader.nextLine();
				boolean correctAddress = address.matches("^[0-9]{1,4}[-][a-z A-z]{1,}[-][a-z A-Z]{1,}");
				while(!correctAddress) {
					System.out.println("Enter customer address -- (Must use the following format - (Add.Num-StreetName-StreetType(Boul, Street,etc.)");
					 address = reader.nextLine();
					 correctAddress = address.matches("^[0-9]{1,4}[-][a-z A-z]{1,}[-][a-z A-Z]{1,}");
				}
				boolean invalidPackage= true;
				String packageName = null ;
				while(invalidPackage) {
					List<String> intPackages = allPackages(c);
					System.out.println("-------- Chose one of the Following Packages");
					for(int i = 0, j = 1; i < intPackages.size();i+=2, j++) {
						System.out.println((j) +" -- "+intPackages.get(i)+" " + intPackages.get(i+1));
					}
					String choice = reader.nextLine();
					switch(choice) {
						case "1":
							packageName = intPackages.get(0);
							invalidPackage= false;
							break;
						case "2":
							packageName = intPackages.get(1);
							invalidPackage= false;
							break;
						case "3":
							packageName = intPackages.get(2);
							invalidPackage= false;
							break;
						case "4":
							packageName = intPackages.get(3);
							invalidPackage= false;
							break;
						default:
							System.out.println("-------- Chose one of the Following Packages(Choose a valid choice)");
					}
				}
				String salt = getSalt();
				byte [] hashCode = hash(userPassword, salt);
				callableStatement = c.prepareCall("{call salesAppPack.registerNewCustomerPlusSetUp(?,?,?,?,?,?,?,?,?)}");
				callableStatement.setString(1, userName);
				callableStatement.setBytes(2, hashCode);
				callableStatement.setString(3, salt);
				callableStatement.setString(4, fName);
				callableStatement.setString(5, lName);
				callableStatement.setString(6, phoneNumber);
				callableStatement.setString(7, email);
				callableStatement.setString(8, address);
				callableStatement.setString(9, packageName);
				callableStatement.execute();
				/*
				 * Need to add the registration of valid date to set up 
				 */
				callableStatement2 = c.prepareCall("{ call salesAppPack.assignAppointment(?,?)}");
				callableStatement2.setString(1, repName);
				callableStatement2.setString(2, userName);
				callableStatement2.execute();
				
				String s = "SELECT scheduled FROM request WHERE customer = ? ";
				 
				PreparedStatement ps = c.prepareStatement(s);
				ps.setString(1, userName);
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					Date d = rs.getDate(1);
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");

				    String strDate = sdf.format(d);
					System.out.println("Your appointment will be scheduled for the following date : " + strDate);
				}
			}
			catch(SQLException e) {
				
				System.out.println("Something Bad Happened!!!");
				System.exit(0);
			}
			finally {
				c.close();
				callableStatement.close();
				callableStatement2.close();
			}
			
			
			
	}
	
	/*
	 * THis method will allow a sales rep to change a customers package
	 */
	public static void changeUserPackage(Scanner reader) throws SQLException {
		boolean invalidUser = true;
		ConnectionToDatabse x = null;
		Connection c = null;
		try {
			 x = new ConnectionToDatabse();
				c = x.getCon();
				while(invalidUser) {
					 System.out.println("-------- Enter the name of customer to change their package"); 
					 String username= reader.nextLine();
					
					 String s = "SELECT username , INTERNET_PACKAGE FROM customer WHERE username = ? ";
					 
					 PreparedStatement ps = c.prepareStatement(s);
					 ps.setString(1, username);
					 ResultSet rs = ps.executeQuery();
					 if(rs.next()) {
						 swapInternetPackages(rs.getString(2),reader, rs.getString(1));
						invalidUser= false;
					 }
					 else {
						 System.out.println("Username or password is wrong try again");
						 invalidUser =  true;
					}
				}
		}
		catch (SQLException e) {
			// TODO: handle exception
		}
		finally{
			c.close();
		}
		
	}
	public static void swapInternetPackages(String internetPackage,Scanner reader,String username) throws SQLException {
		ConnectionToDatabse cd = null;
		Connection c = null;
		try {
			cd = new ConnectionToDatabse();
			 c = cd.getCon();
			List<String> availablePackagesForUser = new ArrayList<String>();
			String s1 = "Select name from internet_package";
			PreparedStatement ps = c.prepareStatement(s1);
			ResultSet rs = ps.executeQuery();
			
			for(int x= 0; rs.next() ; x++) {
				String s2 = rs.getString(1);
				if(!s2.equals(internetPackage)) {
					availablePackagesForUser.add(s2);
				}
			}
			boolean invalidInput = true;
			String newPackage = "";
			while(invalidInput) {
				System.out.println("-------- Choose one of the following ");
				for(int x = 0 ;x < availablePackagesForUser.size();x++) {
					System.out.println("-------- " + (x+1) +":" +availablePackagesForUser.get(x));
				}
				String userInput = reader.nextLine();
				switch(userInput) {
				case "1":
					newPackage = availablePackagesForUser.get(0);
					invalidInput = false;
					break;
				case "2":
					newPackage = availablePackagesForUser.get(1);
					invalidInput = false;
					break;
				case "3":
					newPackage = availablePackagesForUser.get(2);
					invalidInput = false;
					break;
				default :
					System.out.println("Please enter a valid Input");
					break;
				}
			}
			CallableStatement callableStatement = c.prepareCall("{call salesAppPack.modifyUserPackage(?, ?)}");
			callableStatement.setString(1, username);
			callableStatement.setString(2, newPackage);
			callableStatement.execute();
			c.commit();
			
			System.out.println("Success!!!");
			


			
		
		}
		finally {
			c.close();
		}
		
		
		
	}
	public static List<String> allPackages(Connection c) throws SQLException {
		List<String> internetPackages = new ArrayList<String>();
		String s = "Select name, monthly_price from internet_package";
		PreparedStatement ps = c.prepareStatement(s);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			String name = rs.getString(1);
			int monthPrice = rs.getInt(2);
			internetPackages.add(name);
			internetPackages.add(""+monthPrice);
		}
		return internetPackages;
	}
	
	/*
	 * THis method is used to validate if the sales Rep is a valid user
	 */
	public static boolean validSalesRep(String username,String password ,Connection c) throws SQLException {
		 String s = "SELECT password,salt FROM representative WHERE username = ? ";
		 
		 PreparedStatement ps = c.prepareStatement(s);
		 ps.setString(1, username);
		 ResultSet rs = ps.executeQuery();
		 if(rs.next()) {
			 byte [] b = rs.getBytes(1);
			 String salt = rs.getString(2);
			 byte [] hash = hash(password, salt);
			 for(int x = 0 ; x < b.length; x++) {
				 if(b[x]!= hash[x]) {
					return false; 
				 }
			 }
			 System.out.println("--- Welcome to the Sales App ---");
			 System.out.println("Welcome back " + username);
			 return true;
		 }
		 else {
			 System.out.println("Username or password is wrong try again");
			 return false;
		}
		 
	}

	//This method is used if the database is not filled with hashed users 
	public static void generateRepresentatives(Connection c) throws SQLException {
		
		  String [] users = { "yanik", "mohammed", "alpha","hello"};
		String [] passwords = { "yankolo", "momo", "niska", "world"};
		String [] email = { "yanik@sql.com", "mohammed@sql.com","alpha@sql.com", "hello@sql.com"};
		String [] phone = {"450-445-9898","450-445-9898","450-445-9898","450-445-9898"};
		String [] department = {"TECHNICAL", "SALE","BILLING","TECH SUPPORT"};
		for(int i = 0 ; i < users.length; i++) {
			String salt = getSalt();
			byte [] hashCode = hash(passwords[i], salt);
			String statement = "INSERT INTO representative VALUES(?,?,?,?,?,?,?)";
			PreparedStatement ps = c.prepareStatement(statement);
			ps.setString(1, users[i]);
			ps.setBytes(2, hashCode);
			ps.setString(3, salt);
			ps.setString(4, phone[i]);
			ps.setString(5, email[i]);
			ps.setString(6, "1191 Street HelloWorld");
			ps.setString(7, department[i]);
			ps.executeUpdate();
			
			
			}
	}
	public static String getSalt(){
		return new BigInteger(140, rand).toString(32);
	}
	public static byte[] hash(String password, String salt){
		try{
			SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
			PBEKeySpec spec = new PBEKeySpec( password.toCharArray(), salt.getBytes(), 1024, 256 );

			SecretKey key = skf.generateSecret( spec );
	        byte[] hash = key.getEncoded( );
	        return hash;
        }catch( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
	}
	
}
