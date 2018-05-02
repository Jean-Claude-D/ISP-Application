public final class InternetPackage {
	private String name;
	public static final int MAX_NAME_LENGTH = 40;
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		if(isValidName(name)) {
			this.name = name;
		}
		else {
			throw new IllegalArgumentException("Cannot set name to :\n" + (name == null ? "null" : name) + "\nIs invalid name");
		}
	}
	public static boolean isValidName(String name) {
		return name != null && name.length() <= MAX_NAME_LENGTH;
	}
	
	private String description;
	public static final int MAX_DESCRIPTION_LENGTH = 250;
	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public static boolean isValidDescription(String description) {
		return description == null || description.length() <= MAX_DESCRIPTION_LENGTH;
	}
	public boolean hasDescription() {
		return this.description != null;
	}
	
	private double speedUploadMbps;
	public double getSpeedUploadMbps() {
		return this.speedUploadMbps;
	}
	public void setSpeedUploadMbps(double speedUploadMbps) {
		this.speedUploadMbps = speedUploadMbps;
	}
	
	private double speedDownloadMbps;
	public double getSpeedDownloadMbps() {
		return this.speedDownloadMbps;
	}
	public void setSpeedDownloadMbps(double speedDownloadMbps) {
		this.speedDownloadMbps = speedDownloadMbps;
	}
	
	private double bandwidthGb;
	public double getBandwidthGb() {
		return this.bandwidthGb;
	}
	public void setBandwidthGb(double bandwidthGb) {
		this.bandwidthGb = bandwidthGb;
	}
	
	private double monthlyPrice;
	public double getMonthlyPrice() {
		return this.monthlyPrice;
	}
	public void setMonthlyPrice(double monthlyPrice) {
		this.monthlyPrice = monthlyPrice;
	}
	
	private overageCostPgb;
	public double getOverageCostPgb() {
		return this.overageCostPgb;
	}
	public void setOverageCostPgb(double overageCostPgb) {
		this.overageCostPgb = overageCostPgb;
	}
	
	public InternetPackage(name, description, speedUploadMbps, speedDownloadMbps, bandwidthGb, monthlyPrice, overageCostPgb) {
		setName(name);
		setDescription(description);
		setSpeedUploadMbps(speedUploadMbps);
		setSpeedDownloadMbps(speedDownloadMbps);
		setBandwidthGb(bandwidthGb);
		setMonthlyPrice(monthlyPrice);
		setOverageCostPgb(overageCostPgb);
	}
}