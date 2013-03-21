package model;

import gui.EventView;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import client.Program;

public class CalendarModel {

	private Program program;
	
	private User additionalUser;
	
	private Date currentDate;
	private int year;
	private int week;
	
	private ArrayList<EventView> events;
	private ArrayList<EventView> additionalEvents;
	
	public CalendarModel(Program parent) {
		this.program = parent;
		additionalUser = new User();
		init();
	}
	
	public void init() {

		currentDate = new Date();
		year = Integer.parseInt((new SimpleDateFormat("yyyy")).format(currentDate));
		week = Integer.parseInt((new SimpleDateFormat("w")).format(currentDate));
		
		events = new ArrayList<EventView>();
		additionalEvents = new ArrayList<EventView>();
		
//		createTestEvents();
		fetchEvents();
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public User getAdditionalUser() {
		return additionalUser;
	}

	public void setAdditionalUser(User additionalUser) {
		this.additionalUser = additionalUser;
		additionalEvents.clear();
		if(additionalUser != null) {
			for(Event e : program.getConnectionManager().getEvents(additionalUser)) {
				additionalEvents.add(new EventView(e));
			}
		}
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public ArrayList<EventView> getEvents() {
		ArrayList<EventView> temp = new ArrayList<EventView>();
		temp.addAll(events);
		temp.addAll(additionalEvents);
		return temp;
	}

	public void setEvents(ArrayList<EventView> events) {
		this.events = events;
	}
	
	public void addEvent(Event event) {
		events.add(new EventView(event));
	}

	public void changeEvent(EventView event, Event model) {
		events.get(events.indexOf(event)).setModel(model);
	}
	
	public void deleteEvent(EventView event) {
		events.remove(event);
	}
	
	public void fetchEvents() {
		for(Event e : program.getConnectionManager().getEvents(program.getUser())) {
			events.add(new EventView(e));
		}
	}
	
	public void createTestEvents() {

		User user = new User();
		user.setName("Tor");
		
		Event event = new Event("2013-03-19 09:00:00", "2013-03-19 13:00:00");
		event.setDescription("Testmøte som alle må delta på");
		event.setRoom(new Room(5, 5, "Testrom", 10));
		event.setTitle("Testmøte");
		event.setCreatedBy(program.getUser());
		EventView meeting1 = new EventView(event);
		events.add(meeting1);
		
		event = new Event("2013-03-23 08:00:00", "2013-03-23 10:00:00");
		event.setDescription("Ingen må gå inn på dette rommet!");
		event.setRoom(new Room(93, 93, "Testrom", 105));
		event.setTitle("Viktig!");
		event.setCreatedBy(user);
		EventView meeting2 = new EventView(event);
		events.add(meeting2);

		event = new Event("2013-03-23 07:00:00", "2013-03-23 09:30:00");
		event.setDescription("Lawl");
		event.setRoom(new Room(93, 93, "Testrom", 105));
		event.setTitle("Nope!");
		event.setCreatedBy(program.getUser());
		EventView meeting3 = new EventView(event);
		meeting3.setColor(Color.RED);
		events.add(meeting3);

		event = new Event("2013-03-22 10:00:00", "2013-03-22 11:00:00");
		event.setDescription("Lawl");
		event.setRoom(new Room(93, 93, "Testrom", 105));
		event.setTitle("Nope!");
		event.setCreatedBy(program.getUser());
		EventView meeting4 = new EventView(event);
		meeting3.setColor(Color.RED);
		events.add(meeting4);
		
//		for(int i = 0; i < 8; i++) {
//			event = new Event("2013-03-23 08:00:00", "2013-03-23 10:00:00");
//			event.setDescription("Lawl");
//			event.setRoom(new Room(93, "Testrom", 105));
//			event.setTitle("Nope!");
//			EventView meeting4 = new EventView(event);
//			meeting4.setColor(Color.BLUE);
//			events.add(meeting4);
//		}
		
//		EventView meeting3 = new EventView();
//		events.add(meeting3);
//		
//		EventView meeting4 = new EventView();
//		events.add(meeting4);
	}
}
