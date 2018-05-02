package lib;

public final class ExtraFee {
	private String name;
	public static final int MAX_NAME_LENGTH = 20;
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
	public static final int MAX_DESCRIPTION_LENGTH = 100;
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
	
	private double cost;
	public double getCost() {
		return this.cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public ExtraFee(String name, String description, double cost) {
		setName(name);
		setDescription(description);
		setCost(cost);
	}
	
	public ExtraFee(ExtraFee extraFee) {
		this(extraFee.name, extraFee.description, extraFee.cost);
	}
}