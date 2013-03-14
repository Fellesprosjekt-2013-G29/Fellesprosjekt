package server;

import javax.net.ssl.SSLSocket;

import structs.Alert;
import structs.Request;
import structs.Response;

/**
 * ServerMethods
 * 
 * The class containing the logic behind the client handling and the simulator. 
 * The class contains only static methods that are standalone.
 * @author Tor Håkon Bonsaksen
 *
 */
public class ServerMethods 
{
	/**
	 * Handles all client requests based on the request field in the Request object.
	 * The response contains an 'error' if there are some problem of some sort that 
	 * hinders the server from returning the expected data.
	 * 
	 * @param request - Containing the relevant information regarding the request.
	 * @return Response - Containg relevant information to be returned to the client.
	 */
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
		        case Request.ADD_APPOINTMENT:  
		        	testSessionManaging(session);
		            break;
		        case Request.GET_USERS_APPOINTMENTS:  
//		        	getSheepByStatus(request, response, dc, 1);
		            break;
		        case Request.GET_USERS_NOTIFICATIONS:  
//		        	getSheepByStatus(request, response, dc, 2);
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
		        case Request.DELETE_APPOINTMENT:  
		        	testLogin(request, response, connection);
		            break;
		        case Request.ADD_APPOINTMENT:  
		        	System.out.println("derp");
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
