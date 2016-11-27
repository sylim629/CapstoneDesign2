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

	public String getMoney(String date) {
		String totalMoney = "0원";
		for (int i = 0; i < moneyList.size(); i++) {
			if (moneyList.get(i).getDate().equals(date)) {
				if (totalMoney.equals("0원")) {
					totalMoney = moneyList.get(i).getMoney();
				} else {
					String tokens[] = moneyList.get(i).getMoney().split(",");
					int moreMoney = Integer.parseInt(tokens[0] + tokens[1].substring(0, tokens[1].length() - 1));
					tokens = totalMoney.split(",");
					int tempTotalMoney = Integer.parseInt(tokens[0] + tokens[1].substring(0, tokens[1].length() - 1));
					tempTotalMoney += moreMoney;
					totalMoney = NumberFormat.getNumberInstance(Locale.US).format(tempTotalMoney) + "원";
				}
			}
		}
		return totalMoney;
	}
}
