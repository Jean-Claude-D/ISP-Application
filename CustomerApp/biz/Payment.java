package biz;

import java.sql.Date;

public final class Payment {
	public Date datePaid;
	public double amount;
	
	public Payment(Date datePaid, double amount) {
		if(datePaid == null) {
			throw new IllegalArgumentException();
		}
		
		this.datePaid = datePaid;
		this.amount = amount;
	}
}