package model;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;


public class Event {

	private int eventId;
	private User createdBy;
	private Timestamp start;
	private Timestamp end;
	private String description;
	private Room room;
	private ArrayList<Invitation> participants;
	private String title;
	
	public Event() {
		
	}
	
	public Event(String start, String end) {
		setStart(Timestamp.valueOf(start));
		setEnd(Timestamp.valueOf(end));
		
	}
	
	public User getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}
	public Timestamp getStart() {
		return start;
	}
	public void setStart(Timestamp start) {
		this.start = start;
	}
	
	public Timestamp getEnd() {
		return end;
	}
	
	public void setEnd(Timestamp end) {
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setEventId(int id){
		this.eventId = id;
	}
	
	public int getEventId(){
		return this.eventId;
	}
}
