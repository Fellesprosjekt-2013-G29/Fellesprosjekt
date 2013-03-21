package server;

import java.util.ArrayList;

import javax.net.ssl.SSLSocket;

import model.User;

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
			if(session.getKey().equals(key))
				return session;
		}
		return null;
	}

	public void removeSession(Session session)
	{
		sessions.remove(session);
	}
	
	public ArrayList<Session> getSessions()
	{
		return sessions;
	}
	
	public Session findSession(User user)
	{
		for(Session session : sessions)
		{
			if(session.getUser().getUserId() == user.getUserId())
				return session;
		}
		return null;
	}
}
