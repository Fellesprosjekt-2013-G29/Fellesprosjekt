package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

import javax.net.ssl.SSLSocket;

import structs.Request;
import structs.Response;

public abstract class Connection extends Thread
{
	private SSLSocket socket;
	private SessionManager sessionManager;
	boolean running = true;
	
	ObjectInputStream objectInputStream;
	ObjectOutputStream objectOutputStream;
	
	public Connection(SSLSocket socket, SessionManager sessionManager)
	{
		this.socket = socket;
		this.sessionManager = sessionManager;
	}

	public Request reciveRequest()
	{
		try
		{
			objectInputStream = new ObjectInputStream(socket.getInputStream());
	        return (Request) objectInputStream.readObject();
		}
		catch(Exception e)
		{
			running = false;
			return null;
		}
	}

	public boolean sendResponse(Response response)
	{
		try
		{
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectOutputStream.writeObject(response);
			objectOutputStream.flush();
			return true;
		}
		catch(SocketException e)
		{
			return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}	
	}

	public void closeConnection()
	{
		try
		{
			if(objectInputStream != null)
				objectInputStream.close();
			if(objectOutputStream != null)
				objectOutputStream.close();
			if(socket != null)
				socket.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public SSLSocket getSocket()
	{
		return socket;
	}
	
	public SessionManager getSessionManager()
	{
		return sessionManager;
	}
}
