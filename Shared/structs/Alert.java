package structs;

import java.io.Serializable;
import java.util.HashMap;

public class Alert implements Serializable
{
	public static final int MEETING_INVITATION = 0;
	public static final int MEETING_CANCELATION = 1;
	
	private int alertType;

	private HashMap<String, Object> items = new HashMap<String, Object>();

	public void setAlertType(int alertType) 
	{
		this.alertType = alertType;
	}

	public void addItem(String key, Object value) 
	{
		items.put(key, value);
	}

	public Object getItem(String key) 
	{
		return items.get(key);
	}

	public int getAlertType() 
	{
		return alertType;
	}

}
