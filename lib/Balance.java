package lib;

public final class Balance {
	private int Id;
	public int getId() {
		return this.Id;
	}
	public int setId(int Id) {
		this.Id = Id;
	}
	
	private double subTotal;
	public double getSubTotal() {
		return this.subTotal;
	}
	public double setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	
	private double total;
	public double getTotal() {
		return this.total;
	}
	public double setTotal(double total) {
		this.total = total;
	}
	
	public Balance(int Id, double subTotal, double total) {
		setId(Id);
		setSubTotal(subTotal);
		setTotal(total);
	}
}