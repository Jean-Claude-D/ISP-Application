package lib;

import java.util.List;
import java.util.ArrayList;

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
	
	private double overageCostPgb;
	public double getOverageCostPgb() {
		return this.overageCostPgb;
	}
	public void setOverageCostPgb(double overageCostPgb) {
		this.overageCostPgb = overageCostPgb;
	}
	
	private List<ExtraFeature> extraFeatures;
	public void addExtraFeature(ExtraFeature extraFeature) {
		if(extraFeature == null) {
			throw new IllegalArgumentException("Cannot add extraFeature to :\nnull\nIs null extraFeature");
		}
		
		this.extraFeatures.add(new ExtraFeature(extraFeature));
	}
	public List<ExtraFeature> getExtraFeatures() {
		List<ExtraFeature> extraFeatures = new ArrayList<ExtraFeature>(this.extraFeatures.size());
		
		for(ExtraFeature extraFeature : this.extraFeatures) {
			extraFeatures.add(new ExtraFeature(extraFeature));
		}
		
		return extraFeatures;
	}
	private void setExtraFeatures(List<ExtraFeature> extraFeatures) {
		if(extraFeatures == null) {
			throw new IllegalArgumentException("Cannot add extraFeatures to :\nnull\nIs null extraFeatures");
		}
		
		this.extraFeatures = new ArrayList<ExtraFeature>(extraFeatures.size());
		
		for(ExtraFeature extraFeature : extraFeatures) {
			this.extraFeatures.add(new ExtraFeature(extraFeature));
		}
	}
	
	public InternetPackage(String name, String description, double speedUploadMbps, double speedDownloadMbps, double bandwidthGb, double monthlyPrice, double overageCostPgb, List<ExtraFeature> extraFeatures) {
		setName(name);
		setDescription(description);
		setSpeedUploadMbps(speedUploadMbps);
		setSpeedDownloadMbps(speedDownloadMbps);
		setBandwidthGb(bandwidthGb);
		setMonthlyPrice(monthlyPrice);
		setOverageCostPgb(overageCostPgb);
		setExtraFeatures(extraFeatures);
	}
	
	public InternetPackage(InternetPackage internetPackage) {
		this(internetPackage.name, internetPackage.description, internetPackage.speedUploadMbps, internetPackage.speedDownloadMbps, internetPackage.bandwidthGb, internetPackage.monthlyPrice, internetPackage.overageCostPgb, internetPackage.extraFeatures);
	}
}