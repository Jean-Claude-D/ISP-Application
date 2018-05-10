package db;

import java.sql.Date;

public final class Customer {
	public String username;
	public String firstname;
	public String lastname;
	public String phone;
	public String email;
	public String address;
	public Date created;
	
	public Customer(String username, String firstname, String lastname, String phone, String email, String address, Date created) {
		if(username == null || firstname == null || lastname == null || phone == null || address == null || created == null) {
			throw new IllegalArgumentException();
		}
		
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.phone = phone;
		this.email = email;
		this.address = address;
		this.created = created;
	}
	
	@Override
	public String toString() {
		return
		"***************\n" +
		"Username : " + username + '\n' +
		"Firstname : " + firstname + '\n' +
		"Lastname : " + lastname + '\n' +
		"Phone : " + phone + '\n' +
		"Email : " + (email != null ? email : "No Email") + '\n' +
		"Address : " + address + '\n' +
		"Since : " + created.toString() +
		"\n***************\n";
	}
}