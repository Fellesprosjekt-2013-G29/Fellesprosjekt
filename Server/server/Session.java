package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

import javax.net.ssl.SSLSocket;

import structs.Request;
import structs.Response;

/**
 * Session
 * 
 * The Session class will contain each client sessions information and allow the client and server
 * to have an open connection.
 * 
 * @author Tor Håkon Bonsaksen
 *
 */
public class Session extends Thread
{
	private SSLSocket sslsocket;
	private int sessionID;
	boolean running = true;
	
	ObjectInputStream objectInputStream;
	ObjectOutputStream objectOutputStream;
	
	/**
	 * Constructor for the Session class
	 * 
	 * @param sslsocket - The socket that holds the connection
	 * @param sessionID - The ID of the session
	 */
	public Session(SSLSocket sslsocket, int sessionID)
	{
		this.sslsocket = sslsocket;
		this.sessionID = sessionID;
	}

	
	/**
	 * Starts the session
	 * Starts a loop that waits for requests from the client
	 * 
	 */
	public void run()
	{		
		
		
		try
		{
			System.out.println("Server - Connection Open...");
            System.out.println("Session ID: " + sessionID);
            
            while(running)
            {
            	Response response = ServerMethods.handleRequest(reciveObject());
            	sendObject(response);
            }
            
            System.out.println("Session: " + sessionID + " terminated");
            
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Waits for an object from the server or for the connection to be closed.
	 * When the connection is closed an exception is thrown and the boolean value of 'running'
	 * is set to false.
	 * @return Request - The request object sent from the client or NULL if the connection has been closed.
	 */
	public Request reciveObject()
	{
		try
		{
			System.out.println("Sesion: [" + sessionID + "] - Waiting for object...");
			objectInputStream = new ObjectInputStream(sslsocket.getInputStream());
	        return (Request) objectInputStream.readObject();
		}
		catch(Exception e)
		{
			running = false;
			return null;
		}
	}

	/**
	 * Returns an object to the client after an request has been received.
	 * @param response - The object to be returned to the client.
	 * @return boolean - True - the transfer completed, False - the transfer failed.
	 */
	public boolean sendObject(Response response)
	{
		try
		{
			objectOutputStream = new ObjectOutputStream(sslsocket.getOutputStream());
			objectOutputStream.writeObject(response);
			objectOutputStream.flush();
			return true;
		}
		catch(SocketException e)
		{return false;}//Host disconected, dont do anything
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}	
	}

	/**
	 * Cleanly closes all open streams and the socket.
	 */
	public void closeConnection()
	{
		try
		{
			if(objectInputStream != null)
				objectInputStream.close();
			if(objectOutputStream != null)
				objectOutputStream.close();
			if(sslsocket != null)
				sslsocket.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
