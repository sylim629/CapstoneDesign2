package manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import manager.ScheduleManager;
import model.MoneyInfo;
import model.Schedule;
import manager.LoginManager;
import manager.ServerManager;

public class ServerManager {
	static private ServerManager sharedInstance = new ServerManager();

	static public ServerManager sharedInstance() {
		return sharedInstance;
	}

	public ServerManager() {

	}

	public void loadServer() {
		StringBuffer buffer = connectToServer("loadSchedule",
				"&session_key=" + LoginManager.sharedInstance().getSessionKey());
		try {
			JSONParser parser = new JSONParser();
			JSONObject data = (JSONObject) parser.parse(buffer.toString());
			if (Long.parseLong((String) data.get("code")) == 200) {
				data = (JSONObject) data.get("data");
				JSONArray schedules = (JSONArray) data.get("schedule");
				for (int i = 0; i < schedules.size(); i++) {
					JSONObject obj = (JSONObject) schedules.get(i);

					Schedule s = new Schedule();
					s.setServerID((String) obj.get("id"));
					s.setSubject((String) obj.get("title"));
					s.setContent((String) obj.get("content"));
					s.setStartDate(new Date(Long.parseLong((String) obj.get("startdate"))));
					s.setEndDate(new Date(Long.parseLong((String) obj.get("enddate"))));
					JSONArray taggedFriendsJson = (JSONArray) obj.get("tagged");
					ArrayList<String> taggedFriends = new ArrayList<>();
					for (int j = 0; j < taggedFriendsJson.size(); j++) {
						taggedFriends.add((String) ((JSONObject) taggedFriendsJson.get(j)).get("user_id"));
					}
					s.setTaggedFriends(taggedFriends);
					ScheduleManager.sharedInstance().addSchedule(s);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void loadServer_moneyOnly(String phoneNum) {
		StringBuffer buffer = connectToServer("loadMoney",
				"&session_key=" + LoginManager.sharedInstance().getSessionKey() + "&phone_num=" + phoneNum);
		try {
			JSONParser parser = new JSONParser();
			JSONObject data = (JSONObject) parser.parse(buffer.toString());
			if (Long.parseLong((String) data.get("code")) == 200) {
				data = (JSONObject) data.get("data");
				JSONArray moneyJson = (JSONArray) data.get("money");
				ArrayList<MoneyInfo> moneyList = new ArrayList<>();
				for (int i = 0; i < moneyJson.size(); i++) {
					JSONObject obj = (JSONObject) moneyJson.get(i);
					String date = (String) obj.get("date_spent");
					String money = (String) obj.get("money_spent");
					moneyList.add(new MoneyInfo(date, money));
				}
				MoneyManager.sharedInstance().setMoneyList(moneyList);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void saveServer() {
		ArrayList<Schedule> scheduleList = ScheduleManager.sharedInstance().getScheduleList();
		Schedule s = ScheduleManager.sharedInstance().getScheduleAtServerID("-1");

		String taggedFriends = "";
		for (int i = 0; i < s.getTaggedFriends().size(); i++) {
			taggedFriends += s.getTaggedFriends().get(i) + "|";
		}

		StringBuffer buffer = connectToServer("saveSchedule",
				"session_key=" + LoginManager.sharedInstance().getSessionKey() + "&title=" + s.getSubject()
						+ "&content=" + s.getContent() + "&startdate=" + s.getStartDate().getTime() + "&enddate="
						+ s.getEndDate().getTime() + "&tagged=" + taggedFriends);

		try {
			JSONParser parser = new JSONParser();
			JSONObject data = (JSONObject) parser.parse(buffer.toString());
			if (Long.parseLong((String) data.get("code")) == 200) {
				data = (JSONObject) data.get("subject");
				scheduleList.get(scheduleList.size() - 1).setServerID("" + data.get("id"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public void modifyServer(int scheduleID) {
		Schedule s = ScheduleManager.sharedInstance().getScheduleAtServerID(String.valueOf(scheduleID));
		String taggedFriends = "";
		for (int i = 0; i < s.getTaggedFriends().size(); i++) {
			taggedFriends += s.getTaggedFriends().get(i) + "|";
		}

		@SuppressWarnings("unused")
		StringBuffer buffer = connectToServer("modifySchedule",
				"session_key=" + LoginManager.sharedInstance().getSessionKey() + "&id=" + s.getServerID() + "&title="
						+ s.getSubject() + "&content=" + s.getContent() + "&startdate=" + s.getStartDate().getTime()
						+ "&enddate=" + s.getEndDate().getTime() + "&tagged=" + taggedFriends);
	}

	public void deleteServer(int scheduleID) {
		@SuppressWarnings("unused")
		StringBuffer buffer = connectToServer("deleteSchedule",
				"id=" + scheduleID + "&session_key=" + LoginManager.sharedInstance().getSessionKey());
	}

	public StringBuffer connectToServer(String index, String contentToWrite) {
		String url = "http://leannelim0629.cafe24.com/" + index;
		StringBuffer buffer = new StringBuffer();

		try {
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setDoOutput(true);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("charset", "utf-8");

			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(contentToWrite);
			wr.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = rd.readLine()) != null) {
				buffer.append(line);
			}
			wr.close();
			rd.close();
			conn.disconnect();

			System.out.println(buffer);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return buffer;
	}
}
