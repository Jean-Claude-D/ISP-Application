package lib;

import java.sql.Date;

public final class DbLib {
	private DbLib() {};
	
	public static boolean isValidDate(long date) {
		return date >= 0;
	}
	
	public static Date toDate(long date) {
		return new Date(date);
	}
	
	public static long toLong(Date date) {
		return date.getTime();
	}
}