package lib;

public final class ExtraFeature {
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
	public boolean isValidName(String name) {
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
	
	private double monthlyPrice;
	public double getMonthlyPrice() {
		return this.monthlyPrice;
	}
	public void setMonthlyPrice(double monthlyPrice) {
		this.monthlyPrice = monthlyPrice;
	}
	
	private boolean perGb;
	public boolean isPerGb() {
		return this.perGb;
	}
	public void setPerGb(boolean perGb) {
		this.perGb = perGb;
	}
	
	public ExtraFeature(String name, String description, double monthlyPrice, boolean perGb) {
		setName(name);
		setDescription(description);
		setMonthlyPrice(monthlyPrice);
		setPerGb(perGb);
	}
	
	public ExtraFeature(ExtraFeature extraFeature) {
		this(extraFeature.name, extraFeature.description, extraFeature.monthlyPrice, extraFeature.perGb);
	}
}