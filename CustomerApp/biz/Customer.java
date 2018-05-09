package biz;

import java.sql.Date;

public final class Customer {
	public String username;
	public String firstname;
	public String lastname;
	public String phone;
	public String email;
	public String address;
	public Date created;
	
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