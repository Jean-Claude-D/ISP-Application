package db;

import java.sql.Date;
import java.sql.Connection;

public final class Invoice {
	public int id;
	public String packageName;
	public Date startDate;
	public Date endDate;
	public Date dueDate;
	public Balance balance;
	
	public Invoice(int id, String packageName, Date startDate, Date endDate, Date dueDate, Balance balance) {
		if(packageName == null || startDate == null || endDate == null || dueDate == null) {
			throw new IllegalArgumentException();
		}
		
		this.id = id;
		this.packageName = packageName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dueDate = dueDate;
		this.balance = balance;
	}
	
	@Override
	public String toString() {
		return
		"Invoice : " + id + '\n' +
		"Period : " + startDate.toString() + " - " + endDate.toString() + '\n' +
		"Due : " + dueDate.toString() + '\n' +
		"Balance Info :\n" + balance.toString();
	}
}