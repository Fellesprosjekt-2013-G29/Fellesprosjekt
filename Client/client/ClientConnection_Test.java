package client;

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
		testSocket1(cc, connection);
		cc.closeConnection();
		cc2.closeConnection();
	}
	
	public static String login(ClientConnection cc, boolean connection) 
	{
		if(connection)
		{
			Request request = new Request();
			request.setRequest(Request.DELETE_APPOINTMENT);
			request.addItem("username", "herpderp@gmail.com");
			request.addItem("password", "passord");
			cc.sendObject(request);
			Response response = cc.reciveObject();
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
		Response response = cc.reciveObject();
		System.out.println(response.getItem("result"));
	}

	public static void testSocket1(ClientConnection cc, boolean connection)
	{
		Request request = new Request();
		request.setRequest(Request.ADD_APPOINTMENT);
		cc.sendObject(request);
		Response response = cc.reciveObject();
		System.out.println(response.getItem("result"));
	}
}

