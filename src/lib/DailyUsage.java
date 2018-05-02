package lib;

public final class DailyUsage {
	private double uploadGb;
	public double getUploadGb() {
		return this.uploadGb;
	}
	public void setUploadGb(double uploadGb) {
		this.uploadGb = uploadGb;
	}
	
	private double downloadGb;
	public double getDownloadGb() {
		return this.downloadGb;
	}
	public void setDownloadGb(double downloadGb) {
		this.downloadGb = downloadGb;
	}
	
	private long dayUsage;
	public long getDayUsage() {
		return this.dayUsage;
	}
	public void setDayUsage(long dayUsage) {
		if(DbLib.isValidDate(dayUsage)) {
			this.dayUsage = dayUsage;
		}
		else {
			throw new IllegalArgumentException("Cannot set dayUsage to :\n" + dayUsage + "\nIs invalid dayUsage");
		}
	}
	
	public DailyUsage(double uploadGb, double downloadGb, long dayUsage) {
		setUploadGb(uploadGb);
		setDownloadGb(downloadGb);
		setDayUsage(dayUsage);
	}
	
	public DailyUsage(DailyUsage dailyUsage) {
		this(dailyUsage.uploadGb, dailyUsage.downloadGb, dailyUsage.dayUsage);
	}
}