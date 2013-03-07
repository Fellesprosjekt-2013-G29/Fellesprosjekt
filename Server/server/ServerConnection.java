package server;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

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
	
	public void run()
	{
		try
		{
		
			SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
	        SSLServerSocket sslserversocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(PORT);
	
	        String[] newcipher = new String[1];
	        newcipher[0]="TLS_DH_anon_WITH_AES_128_CBC_SHA";
	        
	        int id = 0;
	        System.out.println("Client handler started...");
	        while(true)
	        {
	        	SSLSocket sslsocket;
				sslsocket = (SSLSocket) sslserversocket.accept();
	
	        	sslsocket.setEnabledCipherSuites(newcipher);
	        	Session ssc = new Session(sslsocket,id++);
	        	ssc.start();
	        }    
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
