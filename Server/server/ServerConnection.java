package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import structs.Request;
import structs.Response;

/**
 * ServerConnection
 * 
 * The main server class handling client connections and starting sessions.
 * 
 * @author Tor Håkon Bonsaksen
 *
 */
public class ServerConnection extends Thread
{	
	public static final int PORT = 4447;
	
	private SessionManager sessionManager = new SessionManager();
	
	public void run()
	{
		try
		{
		
			SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
	        SSLServerSocket sslserversocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(PORT);
	        
	        System.out.println("Client handler started...");
	        while(true)
	        {
	        	SSLSocket sslsocket;
				sslsocket = (SSLSocket) sslserversocket.accept();
	        	sslsocket.setEnabledCipherSuites(sslserversocket.getSupportedCipherSuites());
	        
	        	NewConnection connection = new NewConnection(sslsocket, sessionManager);
	        	connection.start();
	        }    
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
