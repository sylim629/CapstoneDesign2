package manager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

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

	public void deleteMoneyList(){
		
		this.moneyList = new ArrayList<MoneyInfo>();
	}
	
	public String getMoney(String date) {
		String totalMoney = "0원";
		for (int i = 0; i < moneyList.size(); i++) {
			if (moneyList.get(i).getDate().equals(date)) {
				if (totalMoney.equals("0원")) {
					totalMoney = moneyList.get(i).getMoney();
				} else {
					String tokens[] = moneyList.get(i).getMoney().split(",|\\원");
					String tempMoney = "";
					for (int j = 0; j < tokens.length; j++)
						tempMoney += tokens[j];
					int moreMoney = Integer.parseInt(tempMoney);
					tokens = totalMoney.split(",|\\원");
					tempMoney = "";
					for (int j = 0; j < tokens.length; j++)
						tempMoney += tokens[j];
					int tempTotalMoney = Integer.parseInt(tempMoney);
					tempTotalMoney += moreMoney;
					totalMoney = NumberFormat.getNumberInstance(Locale.US).format(tempTotalMoney) + "원";
				}
			}
		}
		return totalMoney;
	}
}
