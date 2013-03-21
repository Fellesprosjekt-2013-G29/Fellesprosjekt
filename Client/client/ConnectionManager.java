package client;

import java.sql.Timestamp;
import java.util.ArrayList;

import model.Event;
import model.Invitation;
import model.Room;
import model.User;
import structs.Request;
import structs.Response;

public class ConnectionManager {

	private Program program;
	private ClientConnection outboundConnection;
	private ClientConnectionListener inboundConnection;

	private String key;

	public ConnectionManager(Program program, String host, int port) {
		this.program = program;

		outboundConnection = new ClientConnection(host, port);
		inboundConnection = new ClientConnectionListener(host, port, this);
	}

	public String login(String username, String password) {

		if (outboundConnection.openConnection()) {
			String result;
			Request request = new Request();
			request.setRequest(Request.LOGIN);
			request.addItem("username", username);
			request.addItem("password", password);
			outboundConnection.sendObject(request);
			Response response = outboundConnection.reciveResponse();
			if (response.errorExist()) {
				System.out.println("error " + response.getItem("error"));
				return "Feil brukernavn eller passord";
			} else {
				result = (String) response.getItem("result");
				if (result.equals("loginok")) {
					key = (String) response.getItem("key");
					program.setUser((User) response.getItem("user"));
					return "Login ok";
				}
			}
		}
		System.out.println("connection failed");
		return "Klarer ikke koble til serveren";
	}

	public void attachSocket() {
		if (inboundConnection.openConnection()) {
			Request request = new Request();
			request.setRequest(Request.ATTACH_SOCKET);
			request.addItem("key", key);
			inboundConnection.sendObject(request);
			Response response = inboundConnection.reciveResponse();
			System.out.println(response.getItem("result"));
			inboundConnection.run();
		}
	}

	public ArrayList<Event> getEvents(User user) {
		Request request = new Request(Request.GET_USERS_APPOINTMENTS);
		request.addItem("user", user);
		outboundConnection.sendObject(request);
		Response response = outboundConnection.reciveResponse();
		if (response.errorExist())
			System.out.println(response.getItem("error"));
		else {
			ArrayList<Event> events = (ArrayList<Event>) response
					.getItem("ownedevents");
			for (Event event : events)
				System.out.println(event.getTitle());

			System.out.println("Invitated events:");

			events.addAll((ArrayList<Event>) response.getItem("invitedevents"));
			// events = (ArrayList<Event>) response.getItem("invitedevents");
			// for (Event event : events)
			// System.out.println(event.getTitle());
			return events;
		}
		return null;
	}

	public ArrayList<User> getUsers() {
		Request request = new Request(Request.GET_USERS);
		outboundConnection.sendObject(request);
		Response response = outboundConnection.reciveResponse();
		if (response.errorExist()) {
			System.out.println(response.getItem("error"));
		} else {
			return (ArrayList<User>) response.getItem("users");
		}
		return null;
	}

	public ArrayList<Room> getRooms(Timestamp start, Timestamp end) {
		Request request = new Request(Request.GET_ROOMS);
		request.addItem("start", start);
		request.addItem("end", end);
		outboundConnection.sendObject(request);
		Response response = outboundConnection.reciveResponse();
		if (response.errorExist())
			System.out.println(response.getItem("error"));
		else {
			System.out.println("rooms:");
			ArrayList<Room> rooms = (ArrayList<Room>) response.getItem("rooms");
			return rooms;
		}
		return null;
	}

	public void handleNotifications(int type) {
		System.out.println("Recieived notification");
		switch (type) {
		case 1:
			System.out.println("type 1");
			// get uppdates
			break;

		default:
			break;
		}
	}

	public ArrayList<Invitation> getNotifications() {
		Request request = new Request(Request.GET_USERS_NOTIFICATIONS);
		outboundConnection.sendObject(request);
		Response response = outboundConnection.reciveResponse();
		if (response.errorExist()) {
			System.out.println(response.getItem("error"));
		} else {
			// TODO
			ArrayList<Invitation> notifications = (ArrayList<Invitation>) response
					.getItem("invitation");
			return notifications;
		}
		return null;
	}

	public Event addEvent(Event event) {
		Request request = new Request(Request.ADD_APPOINTMENT);
		request.addItem("event", event);
		outboundConnection.sendObject(request);
		Response response = outboundConnection.reciveResponse();
		if (response.errorExist())
			System.out.println(response.getItem("error"));
		else {
			Event newEvent = (Event) response.getItem("event");
			addInvitation(event.getParticipants(), newEvent);
			System.out.println(response.getItem("result"));
			return newEvent;
		}
		return null;
	}

	public void updateEvent(Event event, ArrayList<String> changes) {
		Request request = new Request(Request.UPDATE_APPOINTMENT);

		int eventID = event.getEventId();
		int room;
		Timestamp start;
		Timestamp end;
		String description;
		ArrayList<User> participants;

		request.addItem("id", eventID);

		System.out.println(changes);

		for (String s : changes) {
			switch (s) {
			case "room":
				request.addItem("room", event.getRoom());
				break;
			case "start":
				System.out.println(event.getStart());
				request.addItem("start", event.getStart());
				break;
			case "end":
				request.addItem("end", event.getEnd());
				break;
			case "description":
				request.addItem("description", event.getDescription());
				break;
			case "participants":
				participants = new ArrayList<User>();
				for (Invitation i : event.getParticipants()) {
					participants.add(i.getTo());
				}
				request.addItem("participants", participants);
				break;
			}
		}

		outboundConnection.sendObject(request);

		Response response = outboundConnection.reciveResponse();
		if (response.errorExist())
			System.out.println(response.getItem("error"));
		else {
			System.out.println(response.getItem("result"));
		}

	}

	public void addInvitation(ArrayList<Invitation> users, Event event) {
		Request request = new Request(Request.ADD_INVITE);
		for (Invitation i : users) {
			i.setEvent(event);
		}
		request.addItem("invitations", users);
		outboundConnection.sendObject(request);
		Response response = outboundConnection.reciveResponse();
		if (response.errorExist())
			System.out.println(response.getItem("error"));
		else {
			System.out.println(response.getItem("result"));
		}
	}

	public boolean updateInvitation(Invitation invite) {
		Request request = new Request(Request.UPDATE_APPOINTMENT);

		request.addItem("invitation", invite);
		outboundConnection.sendObject(request);

		Response response = outboundConnection.reciveResponse();

		if (response.errorExist()) {
			System.out.println(response.getItem("error"));
			return false;
		} else {
			System.out.println(response.getItem("result"));
			return true;
		}
	}

	public boolean deleteEvent(Event event) {
		Request request = new Request(Request.DELETE_APPOINTMENT);

		request.addItem("id", event.getEventId());
		outboundConnection.sendObject(request);

		Response response = outboundConnection.reciveResponse();

		if (response.errorExist()) {
			System.out.println(response.getItem("error"));
			return false;
		} else {
			System.out.println(response.getItem("result"));
			return true;
		}
	}
}
