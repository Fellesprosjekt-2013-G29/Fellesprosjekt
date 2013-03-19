package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Room implements Serializable{
	private int id;
	private int roomNumber;
	private int roomSize;
	private String location;
	
	public Room(int roomid, int roomNumber, String location, int roomSize)
	{
		this.id = roomid;
		this.roomNumber = roomNumber;
		this.location = location;
		this.roomSize = roomSize;
	}
	
	
	public int getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(int nr) {
		this.roomNumber = nr;
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


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
}
