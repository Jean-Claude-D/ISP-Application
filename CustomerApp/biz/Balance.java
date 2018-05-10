package biz;

import java.util.List;
import java.util.LinkedList;

public final class Balance {
	public double subTotal;
	public double total;
	
	private List<ExtraFee> extraFees;
	public void addExtraFee(ExtraFee extraFee) {
		if(extraFee == null) {
			throw new IllegalArgumentException("Cannot add extraFee :\nnull\nIs null extraFeature");
		}
		
		this.extraFees.add(new ExtraFee(extraFee));
	}
	public List<ExtraFee> getExtraFees() {
		List<ExtraFee> extraFees = new LinkedList<ExtraFee>();
		
		for(ExtraFee extraFee : this.extraFees) {
			extraFees.add(new ExtraFee(extraFee));
		}
		
		return extraFees;
	}
	private void setExtraFees(List<ExtraFee> extraFees) {
		if(extraFees == null) {
			extraFees = new LinkedList<ExtraFee>();
		}
		
		this.extraFees = new LinkedList<ExtraFee>();
		
		for(ExtraFee extraFee : extraFees) {
			this.extraFees.add(new ExtraFee(extraFee));
		}
	}
	
	public Balance(double subTotal, double total, List<ExtraFee> extraFees) {
		this.subTotal = subTotal;
		this.total = total;
		setExtraFees(extraFees);
	}
	
	@Override
	public String toString() {
		return
		listExtraFees() + '\n' +
		"+\t" + subTotal + "$ +Txs\n" +
		"-------------------\n" +
		total + "$\n";
	}
	
	private String listExtraFees() {
		StringBuilder list = new StringBuilder();
		
		for(ExtraFee fee : extraFees) {
			list.append("+\t" + fee.cost + "$\n");
		}
		
		return list.toString();
	}
}