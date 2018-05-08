package lib;

import java.util.Date;
import biz.BizUtil;

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
	
	private String firstname;
	public static final int FIRSTNAME_MAX_LENGTH = 40;
	public String getFirstname() {
		return this.firstname;
	}
	public void setFirstname(String firstname) {
		if(isValidFirstname(firstname)) {
			this.firstname = firstname;
		}
		else {
			throw new IllegalArgumentException("Cannot set firstname to :\n" + (firstname == null ? "null" : firstname) + "\nIs invalid firstname");
		}
	}
	public static boolean isValidFirstname(String firstname) {
		return firstname != null && firstname.length() <= FIRSTNAME_MAX_LENGTH;
	}
	
	private String lastname;
	public static final int LASTNAME_MAX_LENGTH = 40;
	public String getLastname() {
		return this.lastname;
	}
	public void setLastname(String lastname) {
		if(isValidLastname(lastname)) {
			this.lastname = lastname;
		}
		else {
			throw new IllegalArgumentException("Cannot set lastname to :\n" + (lastname == null ? "null" : lastname) + "\nIs invalid lastname");
		}
	}
	public static boolean isValidLastname(String lastname) {
		return lastname != null && lastname.length() <= LASTNAME_MAX_LENGTH;
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
	
	private long createdDate;
	public long getCreatedDate() {
		return this.createdDate;
	}
	public void setCreatedDate(long createdDate) {
		if(BizUtil.isValidDate(createdDate)) {
			this.createdDate = createdDate;
		}
		else {
			throw new IllegalArgumentException("Cannot set createdDate to :\n" + createdDate + "\nIs invalid createdDate");
		}
	}
	
	public Customer(String username, String firstname, String lastname, String phone, String email, String address, long createdDate) {
		setUsername(username);
		setFirstname(firstname);
		setLastname(lastname);
		setPhone(phone);
		setEmail(email);
		setAddress(address);
		setCreatedDate(createdDate);
	}
	
	@Override
	public String toString() {
		return "***************\n" +
		"Username : " + this.getUsername() + '\n' +
		"Firstname : " + this.getFirstname() + '\n' +
		"Lastname : " + this.getLastname() + '\n' +
		"Phone : " + this.getPhone() + '\n' +
		"Email : " + (this.hasEmail() ? this.getEmail() : "No Email") + '\n' +
		"Address : " + this.getAddress() + '\n' +
		"Since : " + (new Date(this.getCreatedDate())).toString() + "\n***************\n";
	}
}