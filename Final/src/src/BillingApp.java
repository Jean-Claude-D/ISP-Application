package src;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class BillingApp {
	
	private static SecureRandom rand = new SecureRandom();

	public static void main(String[] args) {
	
		String  username = "Farzaneh";
		String password = "Ay22m81f";
		try {
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", username, password);
			
			Scanner kb = new Scanner(System.in);
			System.out.println("Enter user name: ");
			String uName = kb.nextLine();
			System.out.println("password: ");
			String uPass = kb.nextLine();
			byte[] hashPass = hash(uPass, getSalt());
			
			Statement s = conn.createStatement();
			String sql = "SELECT * FROM REPRESENTATIVE WHERE username= \'" +uName+"\' AND password = \'" +uPass+"\'";
			//PreparedStatement ps = conn.prepareStatement(sql);
			//ps.setString(1,  uName);
			//ps.setString(2, uPass);
			//System.out.println(sql);
			ResultSet rs = s.executeQuery(sql);		
			if (rs.next()) {
				String rep = rs.getString(1);
					System.out.println("Hi "+uName+"! \nselect a number to continue:\n"+
						"1. Modify Invoce Date\n"+
						"2. Refund\n"+
						"3. Revenue Report\n"+
						"4. Internet Package Running Cost\n"+
						"5. Internet Package Profit\n" +
						"6. Reminder\n"+
						"7. Change Password\n");
					int input = kb.nextInt();
					repResponsibility(rep, input, conn);
				
				s.close();
			}
			else
				System.out.println("Wrong user name or password!");	
		
		}
		catch(SQLException sqle) {
			System.out.println(sqle.getMessage());
			
		}

	}
	
	public static void repResponsibility(String rep, int input, Connection c) throws SQLException{
		Scanner kb = new Scanner(System.in);
		switch(input) {
		case 1:
			System.out.println("Enter the invoice id/number: ");
			int invoiceId = kb.nextInt();
			modifyInvoice(rep, invoiceId, c);
			break;
			
		case 2:
			System.out.println("Enter the invoice id/number: ");
			int id = kb.nextInt();
			System.out.println("Enter the reason of refunding: ");
			kb.nextLine();
			String reason = kb.nextLine();
			System.out.println("Enter the amount to refund: ");
			int amount = kb.nextInt();
			refund(id, rep, reason, amount, c);
			break;
			
		case 3:
			//List <Customer> = createCustomList();
			
			revenues( c);
			break;
			
		case 4:
			System.out.println("Enter the internet pacage: ");
			String pack = kb.nextLine();
			runningCost(pack,  4, c);
			break;
		
		case 5:
			System.out.println("Enter the internet pacage: ");
			String p = kb.nextLine();
			runningCost(p, 5, c);
			break;
		case 6:
			remindCustomer(rep,c);
			break;
			
		case 7:
			System.out.println("Enter a new password: ");
			String newPass = kb.nextLine();
			changePass(rep, newPass, c);
			break;
		}
		
	}
	
	/**
	 * @param user
	 * @param id
	 * @param s
	 * @throws SQLException
	 */
	
	
	public static void modifyInvoice(String user, int id, Connection conn) throws SQLException {
		String sql = "{call date_modification(?,?)}";
		CallableStatement cs = conn.prepareCall(sql);
		cs.setInt(1,id);
		cs.setString(2, user);
		cs.execute();
	}
	
	/**
	 * 
	 * @param id
	 * @param repId
	 * @param message
	 * @param amount
	 * @param s
	 * @throws SQLException
	 */
	public static void refund(int id, String user, String message, int amount, Connection conn) throws SQLException{
		String sql = "{call refunding (?,?,?,?)}";
		CallableStatement cs = conn.prepareCall(sql);
		cs.setInt(1, id);
		cs.setString(2, user);
		cs.setString(3, message);
		cs.setInt(4, amount);
		cs.execute();
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 * @throws SQLException
	 */
	public static void revenues(Connection conn) throws SQLException{
		int revenue = 0;
		Statement s = conn.createStatement();
		String sql = "SELECT C.USERNAME, SUM(P.AMOUNT_PAID) FROM CUSTOMER C  "
					+ "JOIN INVOICE I ON I.CUSTOMER = C.USERNAME "
					+ "JOIN PAYMENT P ON P.INVOICE = I.ID "
					+ "GROUP BY P.AMOUNT_PAID, C.USERNAME";
		System.out.println(sql);
		ResultSet rs = s.executeQuery(sql);
		while(rs.next()) {
			System.out.print(rs.getString(1)+"\t\t");
			System.out.println(rs.getInt(2));
			revenue += rs.getInt(2);
		}
		System.out.println(revenue);
	}
	/**
	 * 
	 * @param pack
	 * @param b
	 * @param option
	 * @return
	 * @throws SQLException
	 */
	
	public static void runningCost(String pack,  int option, Connection conn) throws SQLException {
		int result = 0;
		Statement s = conn.createStatement();
		String sql ="";
		if (option == 4) {
			sql = "SELECT RUNNING_COST * COUNT(I.INTERNET_PACKAGE)  FROM INTERNET_PACKAGE P\r\n" + 
					" JOIN INVOICE I ON I.INTERNET_PACKAGE = P. NAME\r\n" + 
					" WHERE P.NAME = \'" + pack +"\'"+
					" GROUP BY I.INTERNET_PACKAGE, RUNNING_COST";
		}
		else
			sql = "SELECT MONTHLY_PRICE * COUNT(I.INTERNET_PACKAGE)  FROM INTERNET_PACKAGE P\r\n" + 
					" JOIN INVOICE I ON I.INTERNET_PACKAGE = P. NAME\r\n" + 
					" WHERE P.NAME = \'" + pack +"\'"+
					" GROUP BY I.INTERNET_PACKAGE, MONTHLY_PRICE";
		ResultSet rs = s.executeQuery(sql);
		if (rs.next())
			System.out.println(rs.getInt(1));
	}
	
	/**
	 * 
	 * @param user
	 * @param b
	 * @throws SQLException
	 */
	public static void remindCustomer(String user, Connection conn) throws SQLException {
		String sql = "{call CUSTOMER_REMINDER(?)}";
		CallableStatement cs = conn.prepareCall(sql);
		cs.setString(1, user);
		cs.execute();
	}
	
	/**
	 * 
	 * @param user
	 * @param old
	 * @param newPass
	 * @param b
	 * @throws SQLException
	 */
	public static void changePass(String user, String newPass, Connection conn) throws SQLException {
		String sql = "UPDATE REPRESENTATIVE SET PASSWORD =\'"+ hash(newPass, getSalt()) + "\' , SALT = \'"+ getSalt() +"\' WHERE USERNAME =\'" + user + "\'";
		
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(sql);
		conn.commit();
	}
	
	
	public static void generateRepresentatives(Connection c) throws SQLException {
		
		  String [] users = { "yanik", "mohammed", "alpha","hello"};
		String [] passwords = { "yankolo", "momo", "niska", "world"};
		String [] email = { "yanik@sql.com", "mohammed@sql.com","alpha@sql.com", "hello@sql.com"};
		String [] phone = {"450-445-9898","450-445-9898","450-445-9898","450-445-9898"};
		String [] department = {"SALE", "SALE","SALE","SALE"};
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
			c.commit();
			
			
			}
	}
	public static String getSalt(){
		return new BigInteger(140, rand).toString(32);
	}
	
	//Takes a password and a salt a performs a one way hashing on them, returning an array of bytes.
	public static byte[] hash(String password, String salt){
		try{
			SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
	        
			/*When defining the keyspec, in addition to passing in the password and salt, we also pass in
			a number of iterations (1024) and a key size (256). The number of iterations, 1024, is the
			number of times we perform our hashing function on the input. Normally, you could increase security
			further by using a different number of iterations for each user (in the same way you use a different
			salt for each user) and storing that number of iterations. Here, we just use a constant number of
			iterations. The key size is the number of bits we want in the output hash*/ 
			PBEKeySpec spec = new PBEKeySpec( password.toCharArray(), salt.getBytes(), 1024, 256 );

			SecretKey key = skf.generateSecret( spec );
	        byte[] hash = key.getEncoded( );
	        return hash;
      }catch( NoSuchAlgorithmException | InvalidKeySpecException e ) {
          throw new RuntimeException( e );
      }
	}

}
