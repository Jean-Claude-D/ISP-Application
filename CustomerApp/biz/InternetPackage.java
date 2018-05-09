package lib;

import java.util.List;
import java.util.ArrayList;

public final class InternetPackage {
	public String name;
	private String description;
	private double speedUploadMbps;
	private double speedDownloadMbps;
	private double bandwidthGb;
	private double monthlyPrice;
	private double overageCostPgb;
	
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
			extraFeatures = new ArrayList<ExtraFeature>();
		}
		
		this.extraFeatures = new ArrayList<ExtraFeature>(extraFeatures.size());
		
		for(ExtraFeature extraFeature : extraFeatures) {
			this.extraFeatures.add(new ExtraFeature(extraFeature));
		}
	}
	
	public InternetPackage(String name, String description, double speedUploadMbps, double speedDownloadMbps, double bandwidthGb, double monthlyPrice, double overageCostPgb, List<ExtraFeature> extraFeatures) {
		if(name == null || description == null) {
			throw new IllegalArgumentException();
		}
		
		this.name = name;
		this.description = description;
		this.speedUploadMbps = speedUploadMbps;
		this.speedDownloadMbps = speedDownloadMbps;
		this.bandwidthGb = bandwidthGb;
		this.monthlyPrice = monthlyPrice;
		this.overageCostPgb = overageCostPgb;
		
		setExtraFeatures(extraFeatures);
	}
	
	public InternetPackage(InternetPackage internetPackage) {
		this(internetPackage.name, internetPackage.description, internetPackage.speedUploadMbps, internetPackage.speedDownloadMbps, internetPackage.bandwidthGb, internetPackage.monthlyPrice, internetPackage.overageCostPgb, internetPackage.extraFeatures);
	}
	
	@Override
	public String toString() {
		return
		"***************\n" +
		"Name : " + name + '\n' +
		"Description : " + description + '\n' +
		"Upload Speed : " + speedUploadMbps + " MB/sec\n" +
		"Download Speed : " + speedDownloadMbps + "MB/sec\n" +
		"Bandwidth : " + bandwidthGb + " GB\n" +
		"Monthly Price : " + monthlyPrice + "$\n" +
		"Overage Cost : " + overageCostPgb + "$/GB\n" +
		"\n***************\n" +
		getExtraFeatureList() + '\n';
	}
	
	private String getExtraFeatureList() {
		StringBuilder featureList = new StringBuilder();
		
		for(ExtraFeature feat : extraFeatures) {
			featureList.append(extraFeature.toString());
		}
		
		return featureList.toString();
	}
}