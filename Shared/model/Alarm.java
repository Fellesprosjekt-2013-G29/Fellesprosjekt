package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Alarm implements Serializable{
	private int id;
	private Timestamp time;
	private User user;
	private Event event;
	
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}

}
