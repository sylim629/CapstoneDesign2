package manager;


public class PairManager {
	private static PairManager singleton = new PairManager();
	private String phoneNumber;
	
	static public PairManager sharedInstance() {
		return singleton;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
}
