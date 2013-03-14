package server;

import java.io.ObjectOutputStream;
import java.net.SocketException;

import javax.net.ssl.SSLSocket;

import structs.Alert;
import structs.Response;

public class Session extends Connection
{
	private SSLSocket outboundSocket;
	private String user;
	private SessionManager sessionManager;
	private String key;
	
	public Session(SSLSocket innboundSocket, SessionManager sessionManager)
	{
		super(innboundSocket, sessionManager);
		this.sessionManager = sessionManager;
	}
	
	public void run()
	{			
		try
		{          
            while(running)
            {
            	System.out.println("Session waiting for object");
            	Response response = ServerMethods.handleRequest(reciveRequest(), this);
            	sendResponse(response);
            }
            
            closeConnection();
            
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean sendAlert(Alert alert)
	{
		try
		{
			objectOutputStream = new ObjectOutputStream(outboundSocket.getOutputStream());
			objectOutputStream.writeObject(alert);
			objectOutputStream.flush();
			return true;
		}
		catch(SocketException e)
		{
			e.printStackTrace();
			return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public void setOutboundSocket(SSLSocket socket)
	{
		this.outboundSocket = socket;
	}
	
	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{ 
		this.key = key;
	}
	
	public void setUser(String user)
	{
		this.user = user;
	}
	
	public String getUser()
	{
		return user;
	}
	
	public void addToList()
	{
		sessionManager.addSession(this);
	}
}
