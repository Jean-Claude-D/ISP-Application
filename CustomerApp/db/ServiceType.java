package db;

public enum ServiceType {
	UPGRADE_PACK("SALES", "Upgrading your Package"), TECHNICAL_HELP("TECH", "Get Technical Help"), INSTALLATION("TECH", "Have an Installation"), BILL_QUESTION("BILL", "Any Question Related to your Bill");
	
	private String dept;
	public String getDept() {
		return this.dept;
	}
	private String displayName;
	public String getDisplayName() {
		return this.displayName;
	}
	private ServiceType(String dept, String displayName) {
		this.dept = dept;
		this.displayName = displayName;
	}
}