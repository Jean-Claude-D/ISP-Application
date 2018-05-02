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
	
	private List<ExtraFee> extraFees;
	public void addExtraFee(ExtraFee extraFee) {
		if(extraFee == null) {
			throw new IllegalArgumentException("Cannot add extraFee :\nnull\nIs null extraFee");
		}
		
		this.extraFees.add(new ExtraFee(extraFee));
	}
	public List<ExtraFee> getExtraFees() {
		List<ExtraFee> extraFees = new ArrayList<ExtraFee>(this.extraFees);
		
		for(ExtraFee extraFee : this.extraFees) {
			extraFees.add(extraFee);
		}
		
		return extraFees;
	}
	private void setExtraFees(List<ExtraFee> extraFees) {
		this.extraFees = new ArrayList<ExtraFee>(extraFees.size());
		
		for(ExtraFee extraFee : extraFees) {
			this.extraFees.add(extraFee);
		}
	}
	
	public Balance(int Id, double subTotal, double total, List<ExtraFee> extraFees) {
		setId(Id);
		setSubTotal(subTotal);
		setTotal(total);
		setExtraFees(extraFees);
	}
	
	public Balance(Balance balance) {
		this(balance.Id, balance.subTotal, balance.total, balance.extraFees);
	}
}