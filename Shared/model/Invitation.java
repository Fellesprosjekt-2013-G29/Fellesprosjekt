package model;

import hoved.Event;
import hoved.InvitationAnswer;
import hoved.Person;

public class Invitation {
	private User from;
	private User to;
	private Event event;
	private InvitationAnswer answer;
	
	public void setFrom(User from) {
		this.from = from;
	}
	public User getFrom() {
		return from;
	}
	public void setTo(User to) {
		this.to = to;
	}
	public User getTo() {
		return to;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public Event getEvent() {
		return event;
	}
	public void setInvitationAnswer(InvitationAnswer answer) {
		this.answer = answer;
	}
	public InvitationAnswer getInvitationAnswer() {
		return answer;
	}
}
