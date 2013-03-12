package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Room {
	private String name;
	private int roomSize;
	private String location;
	
	public static List<Room>AvailableRooms(Date tid){
		//TODO: Implement me!
		
		// hent alle ledige rom ved gitt tidspunkt fra databasen, og returner liste med rom.
		return new ArrayList<Room>();
	}
}
