package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Invitation implements Serializable{
	private int id;
	private Timestamp created;
	private User to;
	private Event event;
	private Timestamp alarm;
	private InvitationAnswer status;
	
	public Invitation(){
		
	}
	
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public User getTo() {
		return to;
	}

		public void setTo(User to) {
		this.to = to;
	}
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	public Timestamp getAlarm() {
		return alarm;
	}
	public void setAlarm(Timestamp alarm) {
		this.alarm = alarm;
	}
	public InvitationAnswer getStatus() {
		return status;
	}
	public void setStatus(InvitationAnswer status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
