package biz;

public final class ExtraFee {
	public String name;
	public String description;
	public double cost;
	
	public ExtraFee(String name, String description, double cost) {
		if(name == null) {
			throw new IllegalArgumentException();
		}
		
		this.name = name;
		this.description = description;
		this.cost = cost;
	}
	
	public ExtraFee(ExtraFee extraFee) {
		this(extraFee.name, extraFee.description, extraFee.cost);
	}
}