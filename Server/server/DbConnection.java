package server;


import java.sql.*;//jconnector
import java.util.ArrayList;
import model.*;

public class DbConnection {

		private java.sql.Connection connection = null;
		private Statement statement = null;
		private PreparedStatement preparedStatement = null;     
        private String url, user, password;
		
        /**
		 * DbConnection constructor
		 * @param url The url for the desired
		 * @param user Username for the database
		 * @param password Password for the database
		 */
        public DbConnection(String url, String user, String password){
        	this.url = url;
        	this.user = user;
        	this.password = password;
        	
        }
        

		public boolean connect ()
		{
			try
			{
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				connection = DriverManager.getConnection(url, user, password);
				statement = connection.createStatement();
				if(connection != null)
					return true;
			} 
			catch (Exception e) 
			{
				System.out.println("Connection failed: " + e.getMessage());
			}
			return false; 
		}

        public void setConnection(java.sql.Connection connection){
            this.connection = connection;
        }

        public java.sql.Connection getConnection(){
            return this.connection;
        }
      
        
        
        public byte[] getStoredHash(String email, String collumnName)throws Exception
        {
        	String sql = "SELECT * FROM User WHERE email = '" + email +"'";
        	ResultSet resultSet = statement.executeQuery(sql);
        	resultSet.next();
        	byte[] hash = resultSet.getBytes(collumnName);
        	return hash;
        }
        
        /**
         * Closes the connection to the database
         */
        public void closeConnection(){
        	try{
        		if (connection != null) 
        			connection.close();
        	} catch (SQLException e){
        		System.out.println(e.getMessage());
        	}
        }

        
        private int getIdFromEmail(String email) throws Exception
        {
 	       String sql = "SELECT * FROM User WHERE Mail = '" + email + "'"; 
 	       ResultSet resultSet = statement.executeQuery(sql);
 	       resultSet.next();
 	       return resultSet.getInt("UserID");
        }

        /**
         * 
         * @param name The name of the user
         * @param email The email of the user
         * @param farm	The farm of the user
         * @param phonenumber	The phonenumber of the user
         * @param salt The salt that is used on the hashed password
         * @param password The password of the user
         * @throws Exception
         */
        public void createUser (User u, byte[] password, byte [] salt)  throws Exception
        {
        	String sql = "INSERT INTO User (email, name, password, pw_hash)  VALUES (?, ?, ?, ?)";

        	PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
        	
        	ps.setString(1, u.getEmail());
        	ps.setString(2, u.getName());
        	ps.setBytes(3, password);
        	ps.setBytes(4, salt);
        	ps.executeUpdate();

        }
        
        
        //
        //
        //  Collections from database
        //
        //
        
        
//        public ArrayList<Alarm> getAlarms(User u){
//        	ArrayList<Invitation> result = new ArrayList<Invitation>();
//        	
//        	String query = String.format("SELECT id from Invitation i where user_id = %s", u.getUserId());
//        	ResultSet res = statement.executeQuery(query);
//        	while( res.next() ){
//        		result.add(getInvitation(res.getInt("user_id")));
//        	}
////        	return result;
//        }
//        
        
        public ArrayList<Event> getCancellations(User u){
        	return null;
        }
        
        
        public ArrayList<Room> getAvailableRooms(Timestamp start, Timestamp end) throws SQLException{
        	String query = String.format("select * from Room r where r.id not in ( select roomid from Appointment a where (a.start not between %s and %s)and (a.end not between %s and %s))",
        			start.toString(), 
        			end.toString(),
        			start.toString(),
        			end.toString());
        	ArrayList<Room> result = new ArrayList<Room>();
        	
        	ResultSet res = statement.executeQuery(query);
        	while (res.next()){
        		int roomNumber = res.getInt("roomnr");
        		int roomSize = res.getInt("size");
        		String location = res.getString("location");
        		Room room = new Room(roomNumber, location, roomSize);
        		result.add(room);
        	}
        	return result;
        }
        
        public ArrayList<Invitation> getInvites(User u) throws SQLException{
        	ArrayList<Invitation> result = new ArrayList<Invitation>();
        	
        	String query = String.format("SELECT id from Invitation i where user_id = %s", u.getUserId());
        	ResultSet res = statement.executeQuery(query);
        	while( res.next() ){
        		result.add(getInvitation(res.getInt("user_id")));
        	}
        	return result;
        }
        

        public ArrayList<Invitation> getInvitationsByEvent(int eventId) throws SQLException{
        	ArrayList<Invitation> list = new ArrayList<Invitation>();
        	String query = String.format("SELECT id from Invitation where appointment_id = %s", eventId);
        	ResultSet res = statement.executeQuery(query);
        	while( res.next() ){
     		   list.add(getInvitation(res.getInt("id")));
     	   }
     	   return list;
        }
        
        
        public ArrayList<Event> getEventsCreatedByUser(User u) throws SQLException{
        	ArrayList<Event> list = new ArrayList<Event>();
        	String query = String.format("SELECT id from Appointment where owner = %s", u.getUserId());
        	ResultSet res = statement.executeQuery(query);
        	while( res.next() ){
     		   list.add(getEvent(res.getInt("id")));
     	   }
        	return list;
        }
        
        public ArrayList<User> getUsers(){
        	// TODO: Implement
        	return new ArrayList<User>();
        }
        
        
        //
        // Single entries from database
        //
        
       public Event getEvent(int eventId) throws SQLException{
    	   Event event = new Event();    	   
    	   String query = String.format("SLEECT * from Appointment a where id = %s", eventId);
    	   PreparedStatement stmt = connection.prepareStatement(query);
    	   stmt.setMaxRows(1);
    	   ResultSet res = stmt.executeQuery();
    	   
    	   event.setEventId(eventId);
    	   event.setCreatedBy(getUser(res.getInt("owner")));
    	   event.setDescription(res.getString("description"));
    	   event.setStart(Timestamp.valueOf(res.getString("start")));
    	   event.setEnd(Timestamp.valueOf(res.getString("end")));
    	   event.setTitle("name");
    	   event.setRoom(getRoom(res.getInt("roomid")));
    	   event.setParticipants(getInvitationsByEvent(eventId));
    	   return event;
       }
        
       public User getUser(int uid) throws SQLException{
    	   User u = new User();
    	   String query = String.format("SLEECT * from User where id = %s", uid);
    	   PreparedStatement stmt = connection.prepareStatement(query);
    	   stmt.setMaxRows(1);
    	   ResultSet res = stmt.executeQuery();
    	   
    	   u.setEmail(res.getString("email"));
    	   u.setName(res.getString("name"));
    	   u.setUserId(uid);
    	   return u;
       }
       
       public User getUser(String email) throws SQLException{
    	   User u = new User();
    	   String query = "SELECT * from User where email = ?";
    	   PreparedStatement stmt = connection.prepareStatement(query);
    	   stmt.setMaxRows(1);
    	   stmt.setString(1, email);
    	   ResultSet res = stmt.executeQuery();
    	   res.next();
    	   
    	   u.setEmail(res.getString("email"));
    	   u.setName(res.getString("name"));
    	   u.setUserId(res.getInt("id"));
    	   return u;
       }
       
       public Room getRoom(int rid) throws SQLException{
    	   String query = String.format("SLEECT * from Room where id = %s", rid);
    	   PreparedStatement stmt = connection.prepareStatement(query);
    	   stmt.setMaxRows(1);
    	   ResultSet res = stmt.executeQuery();
    	   
    	   int roomNumber = res.getInt("roomnr");
    	   int roomSize = res.getInt("size");
    	   String location = res.getString("location");
    	   Room room = new Room(roomNumber, location, roomSize);
    	   room.setId(rid);
    	   return room;
       }
       
       public int getCancellation(int id){
    	   return 0;
       }
       
       public Invitation getInvitation(int invitationId) throws SQLException{
    	   Invitation inv = new Invitation();
    	   String query = String.format("SLEECT * from Invitation where id = %s", invitationId);
    	   PreparedStatement stmt = connection.prepareStatement(query);
    	   stmt.setMaxRows(1);
    	   ResultSet res = stmt.executeQuery();
    	   
    	   inv.setId(invitationId);
    	   inv.setAlarm( Timestamp.valueOf(res.getString("alarm")));
    	   inv.setCreated(Timestamp.valueOf(res.getString("created")));
    	   inv.setStatus(InvitationAnswer.valueOf(res.getString("status")));
    	   inv.setTo(getUser(res.getInt("user_id")));
    	   inv.setEvent(getEvent(res.getInt("appointment_id")));
    	   
    	   return inv;
       }
       
       public Alarm getAlarm(int id){
    	   return null;
       }
       
       
       //
       // Creation methods
       //
       
       public void addAppointment(Event e) throws SQLException{}
       
       
       
       //
       // Update methods
       //
       
       public void updateAppointment(int eventId, String columnname, Object value)throws SQLException{
    	   
       }
       
       //
       // Deletion methods
       //
       
       public void deleteAppointment(int id){
    	   
       }

       
       
       
}








