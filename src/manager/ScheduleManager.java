package manager;

import java.util.Date;
import java.util.ArrayList;

import model.Schedule;

public class ScheduleManager {
	private static ScheduleManager singleton = new ScheduleManager();

	private ArrayList<Schedule> scheduleList;
	private Callbacks scheduleCallback;

	static public ScheduleManager sharedInstance() {
		return singleton;
	}

	private ScheduleManager() {
		scheduleList = new ArrayList<Schedule>();
		scheduleCallback = null;
	}

	public Schedule getScheduleAtServerID(String id) {
		Schedule schedule = null;
		for (Schedule s : scheduleList) {
			if (s.getServerID().equals(id)) {
				schedule = s.copy();
				break;
			}
		}
		return schedule;
	}

	public Schedule getSchedule(int index) {
		Schedule schedule = null;

		if (index >= 0 && index < scheduleList.size()) {
			schedule = scheduleList.get(index).copy();
		}

		return schedule;
	}

	public ArrayList<Schedule> getScheduleList() {
		return scheduleList;
	}

	public ArrayList<Schedule> getSchedules(Date startDate, Date endDate) {
		ArrayList<Schedule> returnList = new ArrayList<Schedule>();

		for (Schedule s : scheduleList) {
			if (s.checkInsetDate(startDate, endDate))
				returnList.add(s.copy());
		}

		for (int i = 0; i < scheduleList.size() - 1; i++) {
			for (int j = i + 1; j < scheduleList.size(); j++) {
				if (scheduleList.get(i).getStartDate().compareTo(scheduleList.get(j).getStartDate()) > 0) {
					Schedule s = scheduleList.get(i);
					scheduleList.set(i, scheduleList.get(j));
					scheduleList.set(j, s);
				}
			}
		}

		return returnList;
	}

	public Schedule addSchedule(Schedule schedule) {
		if (schedule == null)
			return null;

		Schedule newSchedule = new Schedule();
		newSchedule.setServerID(schedule.getServerID());
		newSchedule.setSubject(schedule.getSubject());
		newSchedule.setStartDate(schedule.getStartDate());
		newSchedule.setEndDate(schedule.getEndDate());
		newSchedule.setContent(schedule.getContent());
		newSchedule.setTaggedFriends(schedule.getTaggedFriends());

		scheduleList.add(newSchedule);

		if (scheduleCallback != null)
			scheduleCallback.onAddedSchedule(newSchedule);

		return newSchedule.copy();
	}

	public void updateSchedule(Schedule schedule, int serverID) {
		if (schedule == null)
			return;

		for (int i = 0; i < scheduleList.size(); i++) {
			if (scheduleList.get(i).getServerID().equals(schedule.getServerID())) {
				scheduleList.get(i).setContent(schedule.getContent());
				scheduleList.get(i).setEndDate(schedule.getEndDate());
				scheduleList.get(i).setStartDate(schedule.getStartDate());
				scheduleList.get(i).setSubject(schedule.getSubject());
				scheduleList.get(i).setTaggedFriends(schedule.getTaggedFriends());
				break;
			}
		}

		if (scheduleCallback != null)
			scheduleCallback.onUpdatedSchedule(schedule);
	}

	public boolean deleteSchedule(Schedule schedule) {
		boolean isDeleted = false;

		for (int i = 0; i < scheduleList.size(); i++) {
			if (scheduleList.get(i).getServerID().equals(schedule.getServerID())) {
				scheduleList.remove(i);
				isDeleted = true;
				break;
			}
		}

		if (isDeleted) {
			if (scheduleCallback != null)
				scheduleCallback.onDeletedSchedule(schedule);
		}

		return isDeleted;
	}

	public void setEventListener(Callbacks callback) {
		scheduleCallback = callback;
	}

	public Callbacks getEventListener() {
		return scheduleCallback;
	}

	public interface Callbacks {
		public void onAddedSchedule(Schedule schedule);

		public void onUpdatedSchedule(Schedule schedule);

		public void onDeletedSchedule(Schedule schedule);
	}
}