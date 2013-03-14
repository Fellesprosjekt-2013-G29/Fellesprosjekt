package server;

import java.util.ArrayList;

import javax.net.ssl.SSLSocket;

public class SessionManager 
{
	private ArrayList<Session> sessions = new ArrayList<Session>();
	
	public void addSession(Session session)
	{
		sessions.add(session);
	}
	
	public Session getSession(String key)
	{
		for(Session session : sessions)
		{
			System.out.println("Session: " + session.getKey() + " - " + key + " = " + session.getKey().equals(key));
			if(session.getKey().equals(key))
				return session;
		}
		return null;
	}

	public void removeSession(Session session)
	{
		sessions.remove(session);
	}
}
