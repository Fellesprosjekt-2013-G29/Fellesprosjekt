package client;

import java.sql.Timestamp;
import java.util.ArrayList;

import model.Event;
import model.Room;
import model.User;
import structs.Request;
import structs.Response;

public class ConnectionManager {

	private Program program;
	private ClientConnection outboundConnection;
	private ClientConnection inboundConnection;

	private String key;

	public ConnectionManager(Program program) {
		this.program = program;

		outboundConnection = new ClientConnection("localhost", 4447);
		inboundConnection = new ClientConnection("localhost", 4447);
	}

	public boolean login(String username, String password) {

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
				return false;
			} else {
				result = (String) response.getItem("result");
				if (result.equals("loginok")) {
					key = (String) response.getItem("key");
					program.setUser((User) response.getItem("user"));
					return true;
				}
			}
		}
		System.out.println("connection failed");
		return false;
	}

	public void attachSocket() {
		if (inboundConnection.openConnection()) {
			Request request = new Request();
			request.setRequest(Request.ATTACH_SOCKET);
			request.addItem("key", key);
			inboundConnection.sendObject(request);
			Response response = inboundConnection.reciveResponse();
			System.out.println(response.getItem("result"));
		}
	}

	public ArrayList<Event> getEvents(User user) {
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

	public void getNotifications() {

	}

	public void addEvent(Event event) {

	}

	public boolean deleteEvent(Event event) {
		return true;
	}
}
