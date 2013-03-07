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
	public static final int GET_USERS_NOTIFICATIONS = 1;
	public static final int GET_USERS_APPOINTMENTS = 2;
	public static final int ADD_APPOINTMENT = 3;
	public static final int GET_UPDATE = 4;
	
	// this is a comment
	private int request;

	private HashMap<String, Object> items = new HashMap<String, Object>();

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

}
