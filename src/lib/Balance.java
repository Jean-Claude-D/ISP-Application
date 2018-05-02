package lib;

public final class Balance {
	private int Id;
	public int getId() {
		return this.Id;
	}
	public void setId(int Id) {
		this.Id = Id;
	}
	
	private double subTotal;
	public double getSubTotal() {
		return this.subTotal;
	}
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	
	private double total;
	public double getTotal() {
		return this.total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	
	public Balance(int Id, double subTotal, double total) {
		setId(Id);
		setSubTotal(subTotal);
		setTotal(total);
	}
	
	public Balance(Balance balance) {
		this(balance.Id, balance.subTotal, balance.total);
	}
}