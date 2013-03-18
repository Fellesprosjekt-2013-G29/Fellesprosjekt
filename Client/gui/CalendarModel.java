package gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Event;

public class CalendarModel {

	private Date currentDate;
	private int year;
	private int week;
	
	private ArrayList<EventView> events;
	
	public CalendarModel() {

		init();
	}
	
	public void init() {

		currentDate = new Date();
		year = Integer.parseInt((new SimpleDateFormat("yyyy")).format(currentDate));
		week = Integer.parseInt((new SimpleDateFormat("w")).format(currentDate));
		
		events = new ArrayList<EventView>();
		
		createTestEvents();
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
		return events;
	}

	public void setEvents(ArrayList<EventView> events) {
		this.events = events;
	}
	
	public void addEvent(Event event) {
		events.add(new EventView(event));
	}
	
	public void createTestEvents() {

		EventView meeting1 = new EventView(new Event("2013-02-07, 16:00", "2013-02-07, 18:00"));
		events.add(meeting1);
		
		EventView meeting2 = new EventView(new Event("2013-02-03, 08:00", "2013-02-03, 10:00"));
		events.add(meeting2);

		EventView meeting3 = new EventView(new Event("2013-03-19, 09:00", "2013-03-19, 13:00"));
		events.add(meeting3);
		
		EventView meeting4 = new EventView(new Event("2013-03-23, 08:00", "2013-03-23, 10:00"));
		events.add(meeting4);
	}
}
