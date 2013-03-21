package client;

public class ClientConnectionListener extends Thread
{
	private ClientConnection cc;
	ConnectionManager cm;

	public ClientConnectionListener(String host, int port, ConnectionManager cm) 
	{
		cc = new ClientConnection(host, port);
		this.cm = cm;
	}

	@Override
	public void run() 
	{
		while(true)
		{
			cm.handleNotifications(cc.reciveNotification());
		}
		
	}
	
	public ClientConnection getClientConnection()
	{
		return cc;
	}

}
