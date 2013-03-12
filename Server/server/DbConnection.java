package server;


import java.sql.*;//jconnector

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
        public void createUser (String name, String email, String farm,
        		int phonenumber, byte [] salt, byte [] password)  throws Exception
        {
        	String sql = "INSERT INTO User (Name, Phonenumber, Mail, Farm, Salt, Password)"
        			+ " VALUES ('" + name + "', " + phonenumber + ", '" + email + "', '" + farm
        			+ "', ?, ?)";

        	PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
        	ps.setBytes(1, salt);
        	ps.setBytes(2, password);
        	ps.executeUpdate();

        }

}
