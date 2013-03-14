package model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Event {
	private Person createdBy;
	private DateFormat dateFormat;
	private Date start;
	private Date end;
	private String description;
	private Room room;
	private ArrayList<Invitation> participants;
	private boolean alarm;
	private int AlarmBefore;
	
	public Event() {
		
	}
	
	public Event(String start, String end) {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd, HH:mm");
		
		this.start = new Date();
		this.end = new Date();

		try {
			this.start = dateFormat.parse(start);
			this.end = dateFormat.parse(end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public Person getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Person createdBy) {
		this.createdBy = createdBy;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	public ArrayList<Invitation> getParticipants() {
		return participants;
	}
	public void setParticipants(ArrayList<Invitation> participants) {
		this.participants = participants;
	}
	public boolean isAlarm() {
		return alarm;
	}
	public void setAlarm(boolean alarm) {
		this.alarm = alarm;
	}
	public int getAlarmBefore() {
		return AlarmBefore;
	}
	public void setAlarmBefore(int alarmBefore) {
		AlarmBefore = alarmBefore;
	}
	
	
	
}
