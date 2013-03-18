package server;

import model.Event;
import structs.Alert;
import structs.Request;
import structs.Response;

public class ServerMethods 
{

	public static Response handleRequest(Request request, Session session)
	{
		Response response = new Response();
		DbConnection dc = null;
				//new DbConnection("jdbc:mysql://mysql.stud.ntnu.no/chrlu_prosjekt1", "chrlu_prosjekt1", "general1");
		
		if(request == null)
		{
			response.addItem("error", "No request");
		}
//		else if(!dc.connect())
//		{
//			response.addItem("error", "Unable to connect to the database");
//		}
		else
		{					
			switch (request.getRequest()) 
			{
		        case Request.GET_USERS_APPOINTMENTS:  
		        	getUsersAppointment(request, response, dc, session);
		            break;
		        case Request.GET_USERS_NOTIFICATIONS:  
		        	getUsersNotifications(request, response, dc, session);
		            break;
		        case Request.ADD_APPOINTMENT:  
		        	addAppointment(request, response, dc, session);
		            break;
		        case Request.GET_UPDATE_ALL:  
		        	//TODO
		            break;
		        case Request.UPDATE_APPOINTMENT:  
		        	updateAppointment(request, response, dc);
		            break;
		        case Request.UPDATE_INVITE:  
		        	updateInvite(request, response, dc, session);
		            break;
		        case Request.DELETE_APPOINTMENT:  
		        	deleteAppointment(request, response, dc, session);
		            break;
		        case Request.GET_USERS:  
		        	getUsers(response, dc);
		            break;
		        case Request.GET_ROOMS:  
		        	updateInvite(request, response, dc, session);
		            break;
		        default:
		        	response.addItem("error", "Unknown request");
		            
			}
		}	
//		dc.closeConnection();
		return response;		
	}
	
	public static Response handleNewConnection(Request request, NewConnection connection)
	{
		Response response = new Response();
		DbConnection dc = null;
				//new DbConnection("jdbc:mysql://mysql.stud.ntnu.no/chrlu_prosjekt1", "chrlu_prosjekt1", "general1");
		
		if(request == null)
		{
			response.addItem("error", "No request");
		}
//		else if(!dc.connect())
//		{
//			response.addItem("error", "Unable to connect to the database");
//		}
		else
		{					
			switch (request.getRequest()) 
			{
				case Request.LOGIN:  
					login(request, response, connection);
					break;
		        case Request.ATTACH_SOCKET:  
		        	attachSocket(request, response, connection);
		            break;
		        default:
		        	response.addItem("error", "Invalid request");
		            
			}
		}	
		//dc.closeConnection();
		return response;		
	}
	
	public static boolean login(Request request, Response response, NewConnection connection)
	{
		//TODO fix
		DbConnection dc = new DbConnection(null, null, null);
		
		String username = (String) request.getItem("username");
		String password = (String) request.getItem("password");
		
		try
		{
			byte[] salt = dc.getStoredHash(username, "Salt");
			byte[] hashedPassword = dc.getStoredHash(username, "Password");
			
			if(PasswordEncryption.checkPassword(password, hashedPassword, salt))	
			{
				Session session = new Session(connection.getSocket(), connection.getSessionManager());
				String key = PasswordEncryption.createSalt().toString();
				session.setKey(key);
				session.setUser(username);
				session.addToList();
				session.start();
				response.addItem("key", key);
				response.addItem("result", "loginOK");
				return true;
			}
			else
				response.addItem("result", "loginFailed");
		}
		catch(Exception e)
		{
			response.addItem("error", e.toString());
		}
		return false;
	}

	public static void attachSocket(Request request, Response response, NewConnection connection)
	{
		String key = (String) request.getItem("key");
		System.out.println(key.toString());
		Session session = connection.getSessionManager().getSession(key);
		if(session != null)
		{
			session.setOutboundSocket(connection.getSocket());
			response.addItem("result", "Socket added");
		}
		else
			response.addItem("result", "No Active session found");
	}

	private static void getUsersAppointment(Request request, Response response, DbConnection dc, Session session)
	{
		try
		{
			String[] users = (String[]) request.getItem("users");
			
			if(users != null)
			{
				response.addItem("appointments", dc.getUsersApointment(users));
			}
			else
			{
				response.addItem("error", "Invalid input");
			}
		}
		catch(Exception e)
		{
			response.addItem("error", e.toString());
		}
	}

	private static void getUsersNotifications(Request request, Response response, DbConnection dc, Session session)
	{	
		try
		{
			response.addItem("notifications", dc.getUsersNotifications(session.getUser()));
		}
		catch(Exception e)
		{
			response.addItem("error", e.toString());
		}
	}
	
	private static void addAppointment(Request request, Response response, DbConnection dc, Session session)
	{
		try
		{
			Event event = (Event) request.getItem("event");
			
			dc.addAppointment(event);
			response.addItem("result", "OK");
			//TODO trigger notifications
		}
		catch(Exception e)
		{
			response.addItem("error", e.toString());
		}
	}
	
	private static void updateAppointment(Request request, Response response, DbConnection dc)
	{
		try
		{
			if(request.hasKey("start"))
				dc.updateAppointment("start", request.getItem("start"));
			if(request.hasKey("end"))
				dc.updateAppointment("end", request.getItem("end"));
			if(request.hasKey("description"))
				dc.updateAppointment("description", request.getItem("description"));
			if(request.hasKey("room"))
				dc.updateAppointment("room", request.getItem("room"));
			if(request.hasKey("participants"))
				dc.updateAppointment("participants", request.getItem("participants"));
			if(request.hasKey("title"))
				dc.updateAppointment("title", request.getItem("title"));
			if(request.hasKey("room"))
				dc.updateAppointment("room", request.getItem("room"));
			
			//TODO trigger notifications
		}
		catch(Exception e)
		{
			response.addItem("error", e.toString());
		}
	}
	
	private static void updateInvite(Request request, Response response, DbConnection dc, Session session)
	{
		//TODO
	}
	
	private static void deleteAppointment(Request request, Response response, DbConnection dc, Session session)
	{
		try
		{
			dc.deleteAppointment(request.getItem("appointmentid"));
			//TODO trigger notifications
		}
		catch(Exception e)
		{
			response.addItem("error", e.toString());
		}
	}
	
	private static void getUsers(Response response, DbConnection dc)
	{
		try
		{
			response.addItem("users", dc.getUsers());
		}
		catch(Exception e)
		{
			response.addItem("error", e.toString());
		}
	}
	
	private static void getRooms(Request request, Response response, DbConnection dc)
	{
		try
		{
			response.addItem("rooms", dc.getRooms(request.getItem("start"), request.getItem("end")));
		}
		catch(Exception e)
		{
			response.addItem("error", e.toString());
		}
	}
	
	
	
	
	public static void testLogin(Request request, Response response, NewConnection connection)
	{
		String username = (String) request.getItem("username");
		Session session = new Session(connection.getSocket(), connection.getSessionManager());
		String key = PasswordEncryption.createSalt().toString();
		session.setKey(key);
		session.setUser(username);
		session.addToList();
		session.start();
		System.out.println(key.toString());
		response.addItem("key", key);
		response.addItem("result", "loginOK");
	}
	
	public static void testSessionManaging(Session session)
	{
		Alert alert = new Alert();
		alert.setAlertType(Alert.MEETING_INVITATION);
		session.sendAlert(alert);
		alert.setAlertType(Alert.MEETING_CANCELATION);
		session.sendAlert(alert);
		alert.setAlertType(4);
		session.sendAlert(alert);
	}
}
