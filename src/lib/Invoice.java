package lib;

public final class Invoice {
	private int Id;
	public int getId() {
		return this.Id;
	}
	public void setId(int Id) {
		this.Id = Id;
	}
	
	private Invoice previous;
	public Invoice getPrevious() {
		return this.previous;
	}
	public void setPrevious(Invoice previous) {
		this.previous = previous;
	}
	public boolean hasPrevious() {
		return this.previous != null;
	}
	
	private Customer customer;
	public Customer getCustomer() {
		return this.customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public boolean hasCustomer() {
		return this.customer != null;
	}
	
	private InternetPackage internetPackage;
	public InternetPackage getInternetPackage() {
		return this.internetPackage;
	}
	public void setInternetPackage(InternetPackage internetPackage) {
		this.internetPackage = internetPackage;
	}
	public boolean hasInternetPackage() {
		return this.internetPackage != null;
	}
	
	private long dueDate;
	public long getDueDate() {
		return this.dueDate;
	}
	public void setDueDate(long dueDate) {
		if(DbLib.isValidDate(dueDate)) {
			this.dueDate = dueDate;
		}
		else {
			throw new IllegalArgumentException("Cannot set dueDate to :\n" + dueDate + "\nIs invalid dueDate");
		}
	}
	
	private long createdDate;
	public long getCreatedDate() {
		return this.createdDate;
	}
	public void setCreatedDate(long createdDate) {
		if(DbLib.isValidDate(createdDate)) {
			this.createdDate = createdDate;
		}
		else {
			throw new IllegalArgumentException("Cannot set createdDate to :\n" + createdDate + "\nIs invalid createdDate");
		}
	}
	
	private Balance balance;
	public Balance getBalance() {
		return new Balance(this.balance);
	}
	public void setBalance(Balance balance) {
		this.balance = new Balance(balance);
	}
	
	public Invoice(int Id, Invoice previous, Customer customer, InternetPackage internetPackage, long dueDate, long createdDate, Balance balance) {
		setId(Id);
		setPrevious(previous);
		setCustomer(customer);
		setInternetPackage(internetPackage);
		setDueDate(dueDate);
		setCreatedDate(createdDate);
		setBalance(balance);
	}
}