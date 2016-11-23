package model;

import java.util.ArrayList;
import java.util.Date;

public class Schedule {
	private String serverID;
	private int index;
	private String subject;
	private String content;
	private Date startDate;
	private Date endDate;
	private ArrayList<String> taggedFriends;
//	private String moneySpent;

	public Schedule() {
		serverID = String.valueOf(-1);
		index = -1;
		subject = "";
		content = "";
		startDate = new Date(0);
		endDate = new Date(0);
		taggedFriends = new ArrayList<String>();
//		moneySpent = "0원";
	}

	public Schedule(Schedule schedule) {
		this.serverID = schedule.serverID;
		this.subject = schedule.subject;
		this.content = schedule.content;
		this.startDate = schedule.startDate;
		this.endDate = schedule.endDate;
		this.taggedFriends = schedule.taggedFriends;
//		this.moneySpent = schedule.moneySpent;
	}

	public Schedule copy() {
		return new Schedule(this);
	}

	public String getServerID() {
		return this.serverID;
	}

	public int getIndex() {
		return this.index;
	}

	public String getSubject() {
		return this.subject;
	}

	public String getContent() {
		return this.content;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public ArrayList<String> getTaggedFriends() {
		return this.taggedFriends;
	}

	public void setServerID(String id) {
		this.serverID = id;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setStartDate(Date date) {
		this.startDate = date;
	}

	public void setEndDate(Date date) {
		this.endDate = date;
	}

	public void setTaggedFriends(ArrayList<String> friends) {
		this.taggedFriends = new ArrayList<String>();
		for (int i = 0; i < friends.size(); i++) {
			this.taggedFriends.add(friends.get(i));
		}
	}

	public boolean checkInsetDate(Date date) {
		if (startDate.getTime() <= date.getTime() && date.getTime() <= endDate.getTime())
			return true;
		return false;
	}

	public boolean checkInsetDate(Date date1, Date date2) {
		long maxDist = Math.abs(date1.getTime() - date2.getTime()) + Math.abs(startDate.getTime() - endDate.getTime());
		long dist = Math.max(Math.abs(date1.getTime() - endDate.getTime()),
				Math.abs(date2.getTime() - startDate.getTime()));

		if (dist <= maxDist)
			return true;
		return false;
	}

	@Override
	public String toString() {
		return new String("{ Schedule:{ subject:\"" + subject + "\", content:\"" + content + "\", startDate:\""
				+ startDate.toString() + "\", endDate:\"" + endDate.toString() + "\" } }");
	}

//	public String getMoneySpent() {
//		return moneySpent;
//	}

//	public void setMoneySpent(String moneySpent) {
//		this.moneySpent = moneySpent;
//	}
}
