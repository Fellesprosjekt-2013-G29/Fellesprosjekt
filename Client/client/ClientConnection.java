package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import structs.Request;
import structs.Response;

public class ClientConnection 
{	
	private static final int CONNECTION_TIMEOUT = 3000;
	
	private String host;
	private int port;
	ObjectOutputStream objectOutputStream;
	ObjectInputStream objectInputStream;
	
	private SSLSocket sslsocket;
	
	public ClientConnection(String host, int port)
	{	
		this.host = host;
		this.port = port;
	}
	
	public boolean openConnection()
	{
		try
		{		
			SocketAddress socketaddress = new InetSocketAddress(host, port);		
			SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			sslsocket = (SSLSocket) sslsocketfactory.createSocket();
			sslsocket.connect(socketaddress, CONNECTION_TIMEOUT);
			
            sslsocket.setEnabledCipherSuites(sslsocket.getSupportedCipherSuites());
            
            System.out.println("Client - Conection open");
            return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean sendObject(Request request)
	{
		try 
		{
			objectOutputStream = new ObjectOutputStream(sslsocket.getOutputStream());
			objectOutputStream.writeObject(request);
			objectOutputStream.flush();
			return true;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public Response reciveResponse()
	{
		try
		{
			objectInputStream = new ObjectInputStream(sslsocket.getInputStream());
	        return (Response) objectInputStream.readObject();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public int reciveNotification()
	{
		try
		{
			objectInputStream = new ObjectInputStream(sslsocket.getInputStream());
	        return (Integer) objectInputStream.readObject();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return -1;
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
			if(sslsocket != null)
				sslsocket.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
