package server;

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
	public static Response handleRequest(Request request)
	{
		Response response = new Response();
		DbConnection dc = null;
				//new DbConnection("jdbc:mysql://mysql.stud.ntnu.no/chrlu_prosjekt1", "chrlu_prosjekt1", "general1");
		
		if(request == null)
		{
			response.addItem("error", "No request");
		}
		else if(!dc.connect())
		{
			response.addItem("error", "Unable to connect to the database");
		}
		else
		{					
			switch (request.getRequest()) 
			{
		        case Request.LOGIN:  
		            login(request, response, dc);
		            break;
		        case Request.ADD_APPOINTMENT:  
		        	//getAllSheep(request, response, dc);
		            break;
		        case Request.GET_UPDATE:  
//		        	getSheepByStatus(request, response, dc, 0);
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
		dc.closeConnection();
		return response;		
	}
	
	/**
	 * This method is called when the client has requestet a login.
	 * The Request will then contain a username and a password that will be used to 
	 * authenticate the user.
	 * 
	 * If the authentication gets through the Response is loaded with the key "result" with the value "loginOK"
	 * and returned
	 * 
	 * @param request - The Request object containing the relevant information
	 * @param response - The Response object to be loaded with the response
	 * @param dc - The DbConnection object containing an open connection with the database
	 */
	private static void login(Request request, Response response, DbConnection dc)
	{
		String username = (String) request.getItem("username");
		String password = (String) request.getItem("password");
		
		try
		{
			byte[] salt = dc.getStoredHash(username, "Salt");
			byte[] hashedPassword = dc.getStoredHash(username, "Password");
			
			boolean correct = PasswordEncryption.checkPassword(password, hashedPassword, salt);
			if(correct)	
				response.addItem("result", "loginOK");
			else
				response.addItem("result", "loginFailed");
		}
		catch(Exception e)
		{
			response.addItem("error", e.toString());
		}
	}
}
