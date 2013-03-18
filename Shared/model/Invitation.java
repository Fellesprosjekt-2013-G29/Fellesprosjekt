package model;

import hoved.Event;
import hoved.InvitationAnswer;
import hoved.Person;

public class Invitation {
	private Person from;
	private Person to;
	private Event event;
	private InvitationAnswer answer;
	
	public void setFrom(Person from) {
		this.from = from;
	}
	public Person getFrom() {
		return from;
	}
	public void setTo(Person to) {
		this.to = to;
	}
	public Person getTo() {
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
