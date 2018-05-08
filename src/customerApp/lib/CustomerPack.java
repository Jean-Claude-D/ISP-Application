public final class CustomerPack {
	private CustomerPack() {
	}
	
	public static void main(String[] args) {
		
	}
	
	public String getSalt(Connection conn, Customer customer)
	throws SQLException {
		return getSalt(conn, customer.getUsername());
	}
	
	public String getSalt(Connection conn, String username)
	throws SQLException {
		conn.setAutoCommit(false);
		/* Prepare the call */
		CallableStatement getSaltCall = conn.prepareCall(
			"{? = call CUSTOMER_PCKG.GET_SALT(?)}"
		);
		getSaltCall.registerOutParameter(1, Types.VARCHAR);
		getSaltCall.setString(2, username);
		
		/* Call the method */
		getSaltCall.execute();
		
		/* Retrieve the result */
		String foundSalt = getSaltCall.getString(1);
		
		conn.commit();
		return foundSalt;
	}
	
	public Customer login(Connection conn, String username,
	byte[] hashedPassword) throws SQLException{
		conn.setAutoCommit(false);
		/* Prepare the call */
		CallableStatement loginCall = conn.prepareCall(
			"{? = call CUSTOMER_PCKG.LOGIN(?,?,?,?,?,?,?,?)}"
		);
		loginCall.registerOutParameter(1, Types.VARCHAR);
		loginCall.setString(2, username);
		loginCall.setBytes(3, hashedPassword);
		loginCall.registerOutParameter(4, Types.VARCHAR);
		loginCall.registerOutParameter(5, Types.VARCHAR);
		loginCall.registerOutParameter(6, Types.VARCHAR);
		loginCall.registerOutParameter(7, Types.VARCHAR);
		loginCall.registerOutParameter(8, Types.VARCHAR);
		loginCall.registerOutParameter(9, Types.DATE);
		
		/* Call the method */
		getSaltCall.execute();
		
		try {
			/* Retrieve the result */
			Customer logged = new Customer(
				loginCall.getString(1),
				loginCall.getString(4),
				loginCall.getString(5),
				loginCall.getString(6),
				loginCall.getString(7),
				loginCall.getString(8),
				BizUtil.toLong(loginCall.getDate(9))
			);
			
			conn.commit();
			return logged;
		}
		catch(ArgumentException exc) {
			conn.commit();
			return null;
		}
	}
	
	public boolean changePassword(String username, byte[] confirmPassword,
	String newSalt, byte[] newPassword) throws SQLException {
		conn.setAutoCommit(false);
		/* Prepare the call */
		CallableStatement passwChangeCall = conn.prepareCall(
			"{call CUSTOMER_PCKG.GET_SALT(?,?,?,?)}"
		);
		passwChangeCall.setString(username);
		passwChangeCall.setBytes(newPassword);
		passwChangeCall.setString(newSalt);
		passwChangeCall.setBytes(confirmPassword);
		
		try {
			/* Call the method */
			getSaltCall.execute();
		}
		catch(SQLException exc) {
			/* Beware : this may throw */
			conn.rollback();
			return false;
		}
		
		conn.commit();
		return true;
	}
	
	public InternetPackage[] getAvailablePackages()
}