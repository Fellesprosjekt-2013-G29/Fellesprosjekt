package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Room {
	private String name;
	private int roomSize;
	private String location;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRoomSize() {
		return roomSize;
	}

	public void setRoomSize(int roomSize) {
		this.roomSize = roomSize;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public static List<Room>AvailableRooms(Date tid){
		//TODO: Implement me!
		
		// hent alle ledige rom ved gitt tidspunkt fra databasen, og returner liste med rom.
		return new ArrayList<Room>();
	}
}
