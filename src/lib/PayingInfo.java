package lib;

public final class PayingInfo {
	private String payType;
	public static final int PAYTYPE_MAX_LENGTH = 20;
	private static final String[] VALID_PAYTYPES = {"CREDIT CARD", "PAYPAL", "CRYPTOCURRENCY"};
	public static String[] getValidPayTypes() {
		String[] newArray = new String[VALID_PAYTYPES.length];
		
		System.arraycopy(VALID_PAYTYPES, 0, newArray, 0, VALID_PAYTYPES.length);
		
		return newArray;
	}
	public String getPayType() {
		return this.payType;
	}
	public void setPayType(String payType) {
		if(isValidPayType(payType)) {
			this.payType = payType;
		}
		else {
			throw new IllegalArgumentException("Cannot set payType to :\n" + (payType == null ? "null" : payType) + "\nIs invalid payType");
		}
	}
	public boolean isValidPayType(String payType) {
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
	public static final int PAYID_LENGTH = 30;
	public String getPayId() {
		return this.payId;
	}
	public void setPayId(String payId) {
		if(isValidPayId(payId)) {
			this.payId = payId;
		}
		else {
			throw new IllegalArgumentException("Cannot set payId to :\n" + (payId) + "\nIs invalid payId");
		}
	}
	public static boolean isValidPayId(String payId) {
		return payId != null && payId.length() == PAYID_LENGTH;
	}
	
	private String salt;
	public static final int SALT_LENGTH = 30;
	public String getSalt() {
		return this.salt;
	}
	public void setSalt(String salt) {
		if(isValidSalt(salt)) {
			this.salt = salt;
		}
		else {
			throw new IllegalArgumentException("Cannot set salt to :\n" + (salt == null ? "null" : salt) + "\nIs invalid salt");
		}
	}
	public static boolean isValidSalt(String salt) {
		return salt != null && salt.length() == SALT_LENGTH;
	}
	
	public PayingInfo(String payType, String payId, String salt) {
		setPayType(payType);
		setPayId(payId);
		setSalt(salt);
	}
	
	public PayingInfo(PayingInfo payingInfo) {
		this(payingInfo.payType, payingInfo.payId, payingInfo.salt);
	}
}