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
		newSchedule.setIndex(scheduleList.size());
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

	public void updateSchedule(Schedule schedule, int index) {
		if (schedule == null)
			return;

		scheduleList.get(index).setContent(schedule.getContent());
		scheduleList.get(index).setEndDate(schedule.getEndDate());
		scheduleList.get(index).setStartDate(schedule.getStartDate());
		scheduleList.get(index).setSubject(schedule.getSubject());
		scheduleList.get(index).setTaggedFriends(schedule.getTaggedFriends());

		if (scheduleCallback != null)
			scheduleCallback.onUpdatedSchedule(schedule);
	}

	public boolean deleteSchedule(Schedule schedule) {
		boolean isDeleted = false;
		int index = schedule.getIndex();

		if (index >= 0 && index < scheduleList.size()) {
			scheduleList.remove(index);
			isDeleted = true;
		}
		if (isDeleted) {
			if (scheduleCallback != null)
				scheduleCallback.onDeletedSchedule(schedule);
		}
		
		return isDeleted;
	}

	public void deleteAllSchedules() {
		scheduleList = new ArrayList<>();
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