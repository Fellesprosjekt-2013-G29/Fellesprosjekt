package structs;

import java.io.Serializable;
import java.util.HashMap;

/**
 * The object containing information to be sent from the client 
 * to the server.
 * Contains a field representing the action to be taken and a hashmap 
 * containing additional information where it's needed.
 * @author Tor-Håkon Bonsaksen
 *
 */
public class Request implements Serializable {
	private static final long serialVersionUID = -7036039508370540155L;

	public static final int LOGIN = 0;
	public static final int ATTACH_SOCKET = 1;
	public static final int GET_USERS_NOTIFICATIONS = 10;
	public static final int GET_USERS_APPOINTMENTS = 11;
	public static final int ADD_APPOINTMENT = 12;
	public static final int GET_UPDATE_ALL = 13;
	public static final int UPDATE_APPOINTMENT = 14;
	public static final int UPDATE_INVITE = 15; //For alert
	public static final int DELETE_APPOINTMENT = 16;
	public static final int GET_USERS = 20;
	public static final int GET_ROOMS = 21;
	
	private int request;

	private HashMap<String, Object> items = new HashMap<String, Object>();
	
	public Request()
	{
		
	}
	
	public Request(int request)
	{
		this.request = request;
	}

	public void setRequest(int request) {
		this.request = request;
	}

	public void addItem(String key, Object value) {
		items.put(key, value);
	}

	public Object getItem(String key) {
		return items.get(key);
	}

	public int getRequest() {
		return request;
	}
	
	public boolean hasKey(String key)
	{
		return items.containsKey(key);
	}

}
