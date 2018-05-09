package lib;

public final class ExtraFeature {
	public String name;
	public String description;
	public double monthlyPrice;
	public boolean perGb;
	
	public ExtraFeature(String name, String description, double monthlyPrice, boolean perGb) {
		if(name == null) {
			throw new IllegalArgumentException();
		}
		
		this.name = name;
		this.description = description;
		this.monthlyPrice = monthlyPrice;
		this.perGb = perGb;
	}
	
	public ExtraFeature(ExtraFeature extraFeature) {
		this(extraFeature.name, extraFeature.description, extraFeature.monthlyPrice, extraFeature.perGb);
	}
	
	@Override
	public String toString() {
		return
		"Name : " + name + '\n' +
		"Description : " (description == null ? "No description" : description) + '\n' +
		"Price : " + monthlyPrice + '$' + (perGb ? "/GB" : "/month") + '\n';
	}
}