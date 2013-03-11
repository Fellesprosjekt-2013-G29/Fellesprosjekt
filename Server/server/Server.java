 package server;
 
public class Server 
{
	public static void main(String[] args)
	{	
		System.out.println("starting server...");
		ServerConnection sc = new ServerConnection();
		sc.start();
	}

}
