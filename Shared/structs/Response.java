package structs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The object containing information to be returned from the server to the client.
 * Contains a field for a single sheep, an array for multiple sheep and a hashmap for aditional information.
 * 
 * @author Tor-Håkon Bonsaksen
 *
 */
public class Response implements Serializable {

	private HashMap<String, Object> items = new HashMap<String, Object>();

	public void addItem(String key, Object item) {
		items.put(key, item);
	}

	public Object getItem(String key) {
		return items.get(key);
	}
	
	public boolean errorExist()
	{
		return items.containsKey("error");
	}
}
