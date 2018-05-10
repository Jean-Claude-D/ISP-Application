package db;

import java.sql.Date;

public final class Reminder {
	public int id;
	public Date emissionDate;
	public String details;
	
	public Reminder(int id, Date emissionDate, String details) {
		if(emissionDate == null) {
			throw new IllegalArgumentException();
		}
		
		this.id = id;
		this.emissionDate = emissionDate;
		this.details = details;
	}
}