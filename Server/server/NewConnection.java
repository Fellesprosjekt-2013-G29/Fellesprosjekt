package server;

import javax.net.ssl.SSLSocket;

import structs.Response;

public class NewConnection extends Connection
{
	public NewConnection(SSLSocket socket, SessionManager sessionManager) 
	{
		super(socket, sessionManager);
	}

	public void run()
	{			
		try
		{          
            while(running)
            {
            	Response response = ServerMethods.handleNewConnection(reciveRequest(), this);
            	sendResponse(response);
            	running = false;
            }
            System.out.println("New connection terminated");
            
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
