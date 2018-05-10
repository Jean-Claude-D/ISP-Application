package biz;

import java.sql.Date;

public final class DailyUsage {
	public double uploadGb;
	public double downloadGb;
	public Date day;
	
	public DailyUsage(double uploadGb, double downloadGb, Date day) {
		if(day == null) {
			throw new IllegalArgumentException();
		}
		
		this.uploadGb = uploadGb;
		this.downloadGb = downloadGb;
		this.day = day;
	}
	
	public DailyUsage(DailyUsage dailyUsage) {
		this(dailyUsage.uploadGb, dailyUsage.downloadGb, dailyUsage.day);
	}
	
	@Override
	public String toString() {
		return
		"For : " + day.toString() + '\n' +
		"Upload : " + uploadGb + "GB\n" +
		"Download : " + downloadGb + "GB\n";
	}
}