public final class Customer {
	private String username;
	public static final int USERNAME_MAX_LENGTH = 20;
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		if(isValidUsername(username)) {
			this.username = username;
		}
		else {
			throw new IllegalArgumentException("Cannot set username to :\n" + (username == null ? "null" : username) + "\nIs invalid username");
		}
	}
	public static boolean isValidUsername(String username) {
		return username != null && username.length() <= USERNAME_MAX_LENGTH;
	}
	
	private String password;
	public static final int PASSWORD_LENGTH = 30;
	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		if(isValidPassword(password)) {
			this.password = password;
		}
		else {
			throw new IllegalArgumentException("Cannot set password to :\n" + (password == null ? "null" : password) + "\nIs invalid password");
		}
	}
	public static boolean isValidPassword(String password) {
		return password != null && password.length() == PASSWORD_LENGTH;
	}
	
	private String salt;
	public static final int SALT_LENGTH = 30;
	public String getSalt() {
		return this.salt;
	}
	public void setSalt(String salt) {
		if(isValidSalt(salt)) {
			this.salt = salt;
		}
		else {
			throw new IllegalArgumentException("Cannot set salt to :\n" + (salt == null ? "null" : salt) + "\nIs invalid salt");
		}
	}
	public static boolean isValidSalt(String salt) {
		return salt != null && salt.length() == SALT_LENGTH;
	}
	
	private String phone;
	public static final int PHONE_LENGTH = 12;
	public String getPhone() {
		return this.phone;
	}
	public void setPhone(String phone) {
		if(isValidPhone(phone)) {
			this.phone = phone;
		}
		else {
			throw new IllegalArgumentException("Cannot set phone to :\n" + (phone == null ? "null" : phone) + "\nIs invalid phone");
		}
	}
	public static boolean isValidPhone(String phone) {
		return phone != null && phone.length() == PHONE_LENGTH;
	}
	
	private String email;
	public static final int EMAIL_LENGTH = 50;
	public String getEmail() {
		return this.email;
	}
	public boolean hasEmail() {
		return this.email != null;
	}
	public void setEmail(String email) {
		if(isValidEmail(email)) {
			this.email = email;
		}
		else {
			throw new IllegalArgumentException("Cannot set email to :\n" + (email == null ? "null" : email) + "\nIs invalid email");
		}
	}
	public static boolean isValidEmail(String email) {
		return email == null || email.length() <= EMAIL_LENGTH;
	}
	
	private String address;
	public static final int ADDRESS_LENGTH = 50;
	public String getAddress() {
		return this.address;
	}
	public void setAddress(String address) {
		if(isValidAddress(address)) {
			this.address = address;
		}
		else {
			throw new IllegalArgumentException("Cannot set address to :\n" + (address == null ? "null" : address) + "\nIs invalid address");
		}
	}
	public static boolean isValidAddress(String address) {
		return address != null && address.length() <= ADDRESS_LENGTH;
	}
	
	private InternetPackage internetPackage;
	public InternetPackage getInternetPackage() {
		return this.internetPackage;
	}
	public void setInternetPackage(InternetPackage internetPackage) {
		this.internetPackage = internetPackage;
	}
	public boolean hasInternetPackage() {
		return this.internetPackage != null;
	}
	
	private long createdDate;
	public long getCreatedDate() {
		return this.createdDate;
	}
	public Long setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}
	
	private boolean active;
	public boolean isActive() {
		return this.active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public void setActive(char active) {
		this.active = active == '1';
	}
	
	public Customer(String username, String password, String salt, String phone, String email, String address, InternetPackage internetPackage, long createdDate, boolean active) {
		setUsername(username);
		setPassword(password);
		setSalt(salt);
		setPhone(phone);
		setEmail(email);
		setAddress(address);
		setInternetPackage(internetPackage);
		setCreatedDate(createdDate);
		setActive(active);
	}
}