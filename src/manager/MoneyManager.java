package manager;

import java.util.ArrayList;

import model.MoneyInfo;

public class MoneyManager {
	private static MoneyManager singleton = new MoneyManager();
	private ArrayList<MoneyInfo> moneyList;
	
	static public MoneyManager sharedInstance() {
		return singleton;
	}

	private MoneyManager() {
		moneyList = new ArrayList<>();
	}

	public ArrayList<MoneyInfo> getMoneyList() {
		return moneyList;
	}
	
	public void setMoneyList(ArrayList<MoneyInfo> moneyList) {
		this.moneyList = moneyList;
	}
	
	public String getMoney(String date) {
		for (int i = 0; i < moneyList.size(); i++) {
			if (moneyList.get(i).equals(date)) {
				return moneyList.get(i).getMoney();
			}
		}
		return "0원";
	}
}
