package db;

import java.sql.Date;

public final class Appointment {
	public boolean confirmed;
	public Date scheduled;
	public String details;
	
	public Appointment(boolean confirmed, Date scheduled, String details) {
		if(scheduled == null) {
			throw new IllegalArgumentException();
		}
		
		this.confirmed = confirmed;
		this.scheduled = scheduled;
		this.details = details;
	}
}