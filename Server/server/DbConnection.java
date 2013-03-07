package server;


import java.io.FileWriter;
import java.sql.*;//jconnector
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JTable;

import structs.Sheep;
import net.proteanit.sql.DbUtils; //rs2xml
/**
 * DBConnection lets you connect to a SQL Database
 * @author gardmikael
 *
 */
public class DbConnection {

		private Connection connection = null;
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
        
        /**
         * Connects to the database requested in the constructor 
         * @return Returns true if the connection is successful
         */
		public boolean connect (){
			try{
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				connection = DriverManager.getConnection(url, user, password);
				statement = connection.createStatement();
				if(connection != null){
					return true;
				}
			} catch (Exception e) {
				System.out.println("Connection failed: " + e.getMessage());
			}
			return false; 
		}
		/**
		 * Sets the connection
		 * @param connection The connection you want to set
		 */
        public void setConnection(Connection connection){
            this.connection = connection;
        }
        /**
         * Gets this connections
         * @return Returns this connection
         */
        public Connection getConnection(){
            return this.connection;
        }
      
        /**
         * Retrives the stored hashed value from the database.
         * @param email The email from the user of whom you like to retrieve the salt 
         * @return Returns a byte array
         * @throws Exception 
         */
        public byte[] getStoredHash(String email, String collumnName)throws Exception{
        	String sql = "SELECT * FROM User WHERE Mail = '" + email +"'";
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
        		System.out.println("Epic fail: " + e.getMessage());
        	}
        }
        /**
         * Retrives a row in the database based on a given index 
         * @param sheepId A unique index of the sheep you wish to retrive
         * @return Returns a sheep object
         * @throws Exception 
         */
        public Sheep getSheep(int sheepId) throws Exception{
        	String sql = "SELECT * FROM Sheep WHERE SheepID =" + sheepId;
        	ResultSet resultSet = statement.executeQuery(sql);
        	resultSet.next();
			int weight = resultSet.getInt("Weight");
			int age = resultSet.getInt("Age");
			int status = resultSet.getInt("Status");
			String position = resultSet.getString("Location");
			String gender = resultSet.getString("Gender");
			String comment = resultSet.getString("Comment");
			return new Sheep(sheepId, weight, age, status, position, gender, comment);	
        }
        
        /**
         * Retrives a list with all the sheep from a given owner
         * @param owner A unique index of the user from whom you wish the retrive the sheep from
         * @return Returns an array of all the sheep belonging to the given owner 
         * @throws Exception  
         */
        public Sheep[] getAllSheepFromOwner(String email) throws Exception{
			String sql = "SELECT * FROM Sheep WHERE User_UserID = " + getIdFromEmail(email);
			ResultSet resultSet = statement.executeQuery(sql);
			resultSet.last();
			Sheep[] sheeps = new Sheep[resultSet.getRow()];
			resultSet.beforeFirst();
			int sheepCount = 0;
			while(resultSet.next()){
				int id = resultSet.getInt("SheepID");
				int weight = resultSet.getInt("Weight");
				int age = resultSet.getInt("Age");
				int status = resultSet.getInt("Status");
				String position = resultSet.getString("Location");
				String gender = resultSet.getString("Gender");
				String comment = resultSet.getString("Comment");

				sheeps[sheepCount] = new Sheep(id, weight, age, status, position, gender, comment);
				sheepCount++;
			}
			return sheeps;
		}
        
		/**
		 * Updates a row in the database based on a given index which represents the sheep you wish to update
		 * @param sheepId A unique index of the sheep you wish to update 
		 * @param column The name of the column you wish to update
		 * @param value The new value you wish to set
		 * @throws Exception
		 */
        public void updateRowByID(int sheepId, String column, String value) throws Exception{
        	String sql = "UPDATE Sheep SET " + column + " = '" + value + "' WHERE SheepID = " + sheepId;
        	statement.executeUpdate(sql);	
        }
	
        /**
         * Deletes a sheep from the database 
         * @param sheepID A unique index of the sheep you wish to delete 
         */
        public void deleteSheep(int sheepID) throws Exception{
	       String sql = "DELETE FROM Sheep WHERE SheepID = " + sheepID; 
	       statement.executeUpdate(sql);
	    }
        
        private int getIdFromEmail(String email) throws Exception{
 	       String sql = "SELECT * FROM User WHERE Mail = '" + email + "'"; 
 	       ResultSet resultSet = statement.executeQuery(sql);
 	       resultSet.next();
 	       return resultSet.getInt("UserID");
        }
        
        /**
         * Gets all the sheep based on a given status from a given user
         * @param status The status of the sheep you wish to retrieve
         * @param email The email of the user from whom you wish to retrieve the sheep
         * @return
         * @throws Exception
         */
        public Sheep[] getSheepByStatus(int status, String email) throws Exception{
        	String sql = "SELECT * FROM Sheep WHERE Status = " + status + " AND User_UserID = "
        			+ getIdFromEmail(email);
        	ResultSet resultSet = statement.executeQuery(sql);
        	resultSet.last();
			Sheep[] sheeps = new Sheep[resultSet.getRow()];
			resultSet.beforeFirst();
			int sheepCount = 0;
			while(resultSet.next()){
				int id = resultSet.getInt("SheepID");
				int weight = resultSet.getInt("Weight");
				int age = resultSet.getInt("Age");
				String position = resultSet.getString("Location");
				String gender = resultSet.getString("Gender");
				String comment = resultSet.getString("Comment");

				sheeps[sheepCount] = new Sheep(id, weight, age, status, position, gender, comment);
				sheepCount++;
			}
        	return sheeps;
        }

        /**
         * Adds a sheep to the database
         * @param comment The comment you want to attach the sheep
         * @param age The age you wish to set for the sheep
         * @param weight The weight you wish to set for the sheep
         * @param location The location you wish to set for the sheep
         * @param status The status you wish to set for the sheep
         * @param msg 
         * @param user The index of the user who is owner of the sheep
         * @throws SQLException
         */
        public void addSheep (String gender, String comment, int age, int weight, String location, int status, int msg, String user) throws Exception{
        	statement = connection.createStatement();
        	comment = "'" + comment + "'";
        	String sql = "INSERT INTO Sheep (Gender, Comment, Age, Weight, Location, Status, Message_messageID, User_UserID) VALUES ('" +
        			gender + "', " + comment + ", " + age + ", " + weight + ", '" + 
        			location + "', " + status + "," + msg + ", " + getIdFromEmail(user) +")";
        	statement.executeUpdate(sql);	
        }
        
        /**
         * Updates a sheep by a given ID
         * @param sheepId A unique index of the sheep you wish to update
         * @param column The name of the column you wish to update
         * @param value The new value you wish to set
         * @throws Exception
         */
        public void updateSheepByID(int id, String gender, String comment, int age, int weight, 
        		int status)throws Exception
        {
        	String sql = "UPDATE Sheep " +
        			"SET Gender='" + gender + "'," +
        			"Comment='" + comment + "'," +
        			"Age=" + age + "," +
        			"Status=" + status + "," +
        			"Weight=" + weight +
        			" WHERE SheepID= " + id;
        	statement.executeUpdate(sql);

        }
       
        /**
         * 
         * @param sheepId the ID of the sheep you want to update
         * @param column the column you want to update on the given sheep
         * @param value the value you want to update
         * @throws Exception
         */
        public void updateSheepCollumnByID(int sheepId, String column, String value)throws Exception{

            String sql = "UPDATE Sheep SET "+column+"='"+value+"'"+"WHERE SheepID ='"+sheepId+"'";

            statement.executeUpdate(sql);

        }

        /**
         * Edits a given field of a user in the database
         * @param email The email of the user of whom you wish to update 
         * @param column The name of the column you wish to update
         * @param value The new value you wish to set 
         * @throws Exception
         */
        public void editUser(String email, String column, String value) throws Exception{
        	statement = connection.createStatement();
        	String sql = "UPDATE User SET "+column+"='"+value+"'"+"WHERE Mail ='"+email+"'";
        	statement.executeUpdate(sql);
        }
        /**
         * 
         * @param username The username of the user you want to edit
         * @param name the name of the user you want to edit
         * @param phonenumber the phonenumber of the given user
         * @param farm the farm of the given user
         * @param address the address of the given user 
         * @param contactPhone the phonenumber of the contact the user has set
         * @param contactMail the mail address of the contact the user has set
         * @throws Exception
         */
        public void editUser(String username, String name, int phonenumber, String farm,
        		String address, int contactPhone, String contactMail) throws Exception{
        	String sql = "UPDATE User " +
        			"SET Name='" + name + "'," +
        			"Phonenumber=" + phonenumber + "," +
        			"Mail='" + username + "'," +
        			"Farm='" + farm + "'," +
        			"Adress='" + address + "'," +
        			"ContactPhone=" + contactPhone + "," +
        			"ContactMail='" + contactMail + "'" +
        			" WHERE UserId= " + getIdFromEmail(username);
        	statement.executeUpdate(sql);
        }

        /**
         * 
         * @param sheepID The ID of the sheep you want to find the owner of
         * @return Returns the owner of the given sheep
         * @throws Exception
         */
        public int getOwner(int sheepID) throws Exception
        {
        	String sql = "SELECT * FROM Sheep WHERE SheepID =" + sheepID;
        	ResultSet resultSet = statement.executeQuery(sql);
        	resultSet.next();
        	
			return resultSet.getInt("User_UserID");
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
        public void createUser (String name, String email, String farm,
        		int phonenumber, byte [] salt, byte [] password)  throws Exception{
        	String sql = "INSERT INTO User (Name, Phonenumber, Mail, Farm, Salt, Password)"
        			+ " VALUES ('" + name + "', " + phonenumber + ", '" + email + "', '" + farm
        			+ "', ?, ?)";

        	PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
        	ps.setBytes(1, salt);
        	ps.setBytes(2, password);
        	ps.executeUpdate();

        }

        /**
         * Returns the content on a given column that belongs to a specified user
         * @param column The name of the column you want to get
         * @param username The username of the user you want to retrieve the info from
         * @return Returns the content in the given column
         * @throws Exception
         */
        public String getColumnFromUserTable(String column, String username) throws Exception{
        	String sql = "SELECT " + column + " FROM User WHERE Mail = '" + username + "'";
        	ResultSet resultSet = statement.executeQuery(sql);
        	resultSet.next();
        	return resultSet.getString(column);
        }
        /**
         * 
         * @param column The name of the column you want to get
         * @param userID The ID of the user you want to retrieve info from
         * @return Returns the content in the given column
         * @throws Exception
         */
        public String getColumnFromUserTable(String column, int userID) throws Exception{
        	String sql = "SELECT " + column + " FROM User WHERE UserID = '" + userID + "'";
        	ResultSet resultSet = statement.executeQuery(sql);
        	resultSet.next();
        	return resultSet.getString(column);
        }

        //update method
        
        /**
         * Updates the position of all sheep in the database.
         * @throws Exception
         */
        public void update()throws Exception
        {
        	System.out.println("Updating coordinates...");
        	Statement statement = connection.createStatement();
        	String sql = "SELECT * FROM Sheep";
        	ResultSet resultSet = statement.executeQuery(sql);
        	while(resultSet.next())
        		updatePosition(resultSet.getInt("SheepID"));
        }

        /**
         * Updates the position of a desiered sheep
         * @param sheepID The unique ID of the sheep you wish to update
         * @return Returns the new position of the sheep
         * @throws Exception
         */
        private void updatePosition(int sheepID) throws Exception{
        	Statement statement = connection.createStatement();
            String sql = "SELECT * FROM Sheep WHERE SheepID= " + sheepID;
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
           
            //DecimalFormat decimalFormat = new DecimalFormat("#0.000000");

            String [] positions = resultSet.getString("Location").split(" ");
            double positionY = Double.parseDouble(positions[0]);
            double positionX = Double.parseDouble(positions[1]);
           
            String newPosition = updateDouble(positionY) + " " + updateDouble(positionX);
            newPosition = newPosition.replace(",", ".");
            
            writeFile("Sheep"+sheepID, newPosition);
            sql = "UPDATE Sheep SET Location='" + newPosition +"' WHERE SheepID = " + sheepID;
            statement.executeUpdate(sql);
        }

        private void writeFile(String filename, String position){
        	try{
        		String filepath = "C:\\Sheepwatch\\log\\" + filename;                   
        		FileWriter fileWriter = new FileWriter(filepath,true);
        		fileWriter.write(position + "\n");
        		fileWriter.close();
        	}catch(Exception e){
        		System.err.println("IOException: " + e.getMessage());
        	}
        }
        
        private double updateDouble(double position){
            int rand = random(0,2);
            DecimalFormat df = new DecimalFormat("#0.000000");
            String number = "0.000";
            double vector;
            number += random(3,10);
            number += random(3,10);
            vector = Double.parseDouble(number);
            if(rand == 0)
            	position += vector;
            else                 
            	position -= vector;
            
            String newValue = df.format(position);
            newValue = newValue.replace(",", ".");
            return Double.parseDouble(newValue);
    }
        
        private static int random(int min, int max) {
            return min+ (int)( Math.random() * (max-min) );
        }
}
