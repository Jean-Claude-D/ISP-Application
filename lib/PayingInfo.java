package lib;

public final class PayingInfo {
	private String payType;
	public static final int PAYTYPE_MAX_LENGTH = 20;
	private static final String[] VALID_PAYTYPES = {"CREDIT CARD", "PAYPAL", "CRYPTOCURRENCY"};
	public static getValidPayTypes() {
		String[] newArray = new String[VALID_PAYTYPES.length];
		
		arrayCopy(VALID_PAYTYPES, 0, newArray, 0, VALID_PAYTYPES.length);
		
		return newArray;
	}
	public getPayType() {
		return this.payType;
	}
	public setPayType(String payType) {
		if(isValidPayType(payType)) {
			this.payType = payType;
		}
		else {
			throw new IllegalArgumentException("Cannot set payType to :\n" + (payType == null ? "null" : payType) + "\nIs invalid payType");
		}
	}
	public isValidPayType(String payType) {
		if(payType == null) {
			return false;
		}
		
		for(String validPayType : VALID_PAYTYPES) {
			if(validPayType.equals(payType)) {
				return true;
			}
		}
		
		return false;
	}
	
	private String payId;
	public static final int PAYID_MAX_LENGTH
}