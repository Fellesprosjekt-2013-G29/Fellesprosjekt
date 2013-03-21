package client;

public class ClientConnectionListener extends ClientConnection implements Runnable
{
	private ConnectionManager cm;

	public ClientConnectionListener(String host, int port, ConnectionManager cm) 
	{
		super(host, port);
		this.cm = cm;
	}

	@Override
	public void run() 
	{
		while(true)
		{
			cm.handleNotifications(reciveNotification());
		}
		
	}

}
