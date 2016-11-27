package model;

public class MoneyInfo {
	private String date;
	private String money;
	
	public MoneyInfo(String date, String money) {
		this.date = date;
		this.money = money;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}
}
