package client;

import java.sql.Timestamp;
import java.util.ArrayList;

import model.Event;
import model.Invitation;
import model.InvitationAnswer;
import model.Room;
import model.User;

import structs.Alert;
import structs.Request;
import structs.Response;

public class ClientConnection_Test 
{
	private final static String HOST = "localhost";
	private final static int PORT = 4447;
	
	public static void main(String[] args) throws Exception
	{
		System.out.println("client started...");
		ClientConnection cc = new ClientConnection(HOST, PORT);
		ClientConnection cc2 = new ClientConnection(HOST, PORT);
		boolean connection = cc.openConnection();
		
		String key = login(cc, connection);
		boolean connection2 = cc2.openConnection();
		attachSocket(cc2, connection2, key);
		
		
		Timestamp start = Timestamp.valueOf("2013-03-28 16:00:00");
		Timestamp end = Timestamp.valueOf("2013-03-28 18:00:00");
		
		ArrayList<User> users = getUsers(cc, connection);
	
		
		addAppointment(cc, start, end, users);
		
		
		//getRooms(cc, start, end);
		
		//getApointments(cc, null);
		
		
		
		
		cc.closeConnection();
		cc2.closeConnection();
	}
	
	public static String login(ClientConnection cc, boolean connection) 
	{
		if(connection)
		{
			Request request = new Request();
			request.setRequest(Request.LOGIN);
			request.addItem("username", "bjarne@gmail.com");
			request.addItem("password", "derp");
			cc.sendObject(request);
			Response response = cc.reciveResponse();
			if(response.errorExist())
				System.out.println("error " + response.getItem("error"));
			else
				System.out.println("result:" + " " + response.getItem("result"));
			
			return (String)response.getItem("key");
		}
		return null;
	}
	
	public static void attachSocket(ClientConnection cc, boolean connection, String key) 
	{
		Request request = new Request();
		request.setRequest(Request.ATTACH_SOCKET);
		request.addItem("key", key);
		cc.sendObject(request);
		Response response = cc.reciveResponse();
		System.out.println(response.getItem("result"));
	}

	public static void testSocket1(ClientConnection cc, boolean connection)
	{
		Request request = new Request();
		request.setRequest(Request.ADD_APPOINTMENT);
		cc.sendObject(request);
		Response response = cc.reciveResponse();
		System.out.println(response.getItem("result"));
	}
	
	public static void testServerPushing(ClientConnection cc, boolean connection)
	{
		Alert alert;
		
		while (true)
		{
			alert = cc.reciveAlert();
			System.out.println("Type: " + alert.getAlertType());
			if(alert.getAlertType() == 4)
				break;
		}
		System.out.println("Done");
	}

	public static ArrayList<User> getUsers(ClientConnection cc, boolean connection)
	{
		Request request = new Request(Request.GET_USERS);
		cc.sendObject(request);
		Response response = cc.reciveResponse();
		if(response.errorExist())
			System.out.println(response.getItem("error"));
		else
		{
			ArrayList<User> users = (ArrayList<User>) response.getItem("users");
			return users;
		}
		return null;
	}
	
	public static void getRooms(ClientConnection cc, Timestamp start, Timestamp end)
	{
		Request request = new Request(Request.GET_ROOMS);
		request.addItem("start", start);
		request.addItem("end", end);
		cc.sendObject(request);
		Response response = cc.reciveResponse();
		if(response.errorExist())
			System.out.println(response.getItem("error"));
		else
		{
			System.out.println("rooms:");
			ArrayList<Room> rooms = (ArrayList<Room>) response.getItem("rooms");
			for(Room room : rooms)
				System.out.println(room.getId() + " - " + room.getRoomNumber() + " - " + room.getLocation() + " - " + room.getRoomSize());
		}
	}
	
	public static void getNotifications(ClientConnection cc)
	{
		Request request = new Request(Request.GET_USERS_NOTIFICATIONS);
		cc.sendObject(request);
		Response response = cc.reciveResponse();
		if(response.errorExist())
			System.out.println(response.getItem("error"));
		else
			//TODO
			System.out.println("Uncomplete");
	}

	public static void getApointments(ClientConnection cc, User user)
	{
		Request request = new Request(Request.GET_USERS_APPOINTMENTS);
		request.addItem("user", user);
		cc.sendObject(request);
		Response response = cc.reciveResponse();
		if(response.errorExist())
			System.out.println(response.getItem("error"));
		else
		{
			ArrayList<Event> events = (ArrayList<Event>) response.getItem("events");
			for(Event event : events)
				System.out.println(event.getTitle());
			
			System.out.println("Invitations:");
			
			ArrayList<Invitation> invitations = (ArrayList<Invitation>) response.getItem("invitations");
			for(Invitation invitation : invitations)
				System.out.println(invitation.getTo().getEmail());
		}
			
	}
	
	public static void addAppointment(ClientConnection cc, Timestamp start, Timestamp end, ArrayList<User> users)
	{
		Event event = new Event();
		event.setDescription("beskrivelse");
		event.setTitle("Møte1");
		event.setStart(start);
		event.setEnd(end);
		
		Request request = new Request(Request.ADD_APPOINTMENT);
		request.addItem("event", event);
		cc.sendObject(request);
		Response response = cc.reciveResponse();
		if(response.errorExist())
			System.out.println(response.getItem("error"));
		else
		{
			Event newEvent = (Event) response.getItem("event");
			addInvitation(cc, users, newEvent);
			System.out.println(response.getItem("result"));
		}
	}
	
	public static void addInvitation(ClientConnection cc, ArrayList<User> users, Event event)
	{
		Request request = new Request(Request.ADD_INVITE);
		ArrayList<Invitation> list = new ArrayList<Invitation>();
		
		for(User user : users)
		{
			Invitation inv = new Invitation();
			inv.setEvent(event);
			inv.setStatus(InvitationAnswer.NA);
			inv.setTo(user);
			list.add(inv);
		}
		request.addItem("invitations", list);
		cc.sendObject(request);
		Response response = cc.reciveResponse();
		if(response.errorExist())
			System.out.println(response.getItem("error"));
		else
		{
			System.out.println(response.getItem("result"));
		}
	}

	public static void createUser(ClientConnection cc)
	{
		Request request = new Request(Request.CREATE_USER);
		request.addItem("name", "Bjarne hansen");
		request.addItem("username", "hege@gmail.com");
		request.addItem("password", "derp");
		cc.sendObject(request);
		Response response = cc.reciveResponse();
		if(response.errorExist())
			System.out.println(response.getItem("error"));
		else
			System.out.println(response.getItem("result"));
	}

	
}

