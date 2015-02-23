package database;

import java.awt.List;
import java.security.MessageDigest;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.sqlite.SQLiteConfig;

public class ConnectToDb {
	private Connection connection = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;	
	private ResultSet resultSet = null;

	private static final String DB_URL = "jdbc:sqlite:Server/src/database/TP3Hdb.db";  
	private static final String DRIVER = "org.sqlite.JDBC";  




	public void makeConnection(){

		
		getConnection();
		System.out.println("Encoding for password is "+ sha256("VIktor"));

		//		System.out.println("DELATION  "+ deleteFriendship("User15","User13"));
		//			addFriend("User15","User16");
		//			
		//			addFriend("User35","Viktor");
		//		logIn("User4","password");
		//
		//			addFriend("username","User4");
		//			addFriend("username2","User3");
		//			addFriend("User11","User4");
	
	}


	private void getConnection()  { 
		try { 
			Class.forName(DRIVER);     
			SQLiteConfig config = new SQLiteConfig();  
			config.enforceForeignKeys(true);  
			connection = DriverManager.getConnection(DB_URL);  
		}catch(ClassNotFoundException error){
			System.out.println("Error: "+ error.getMessage());
		}catch(SQLException error){
			System.out.println("Error in connecting to db: " + error.getMessage());
		}
	}
	/**
	 * Register method: register the user
	 * @param name, password
	 * @return true if user does not exists, false if exists
	 * @throws SQLException 
	 * **/
	public boolean register( String name,String password)  {
		try {
			if(!isRegistered(name)){
				preparedStatement= connection.prepareStatement("INSERT INTO Users(username,password) VALUES (?,?)");
				preparedStatement.setString(1,name);
				preparedStatement.setString(2,password);
				preparedStatement.execute();
				return true;
			}
		} catch (SQLException error) {
			System.out.println("Error in registation: " + error.getMessage());
			error.printStackTrace();
		}finally{
			closeStatement(preparedStatement);
		}
		System.out.println("The user already Exists !");
		return false;
	}


	private boolean isRegistered( String username) throws SQLException{
		//3.Execute SQL query
		preparedStatement = connection.prepareStatement("SELECT count(*) FROM Users WHERE username= ?");
		preparedStatement.setString(1, username);
		resultSet =  preparedStatement.executeQuery();
		if(resultSet.next()) {
			int count = resultSet.getInt(1);
			if(count == 0) return false;
		}
		return true;
	}
	/**
	 * LogIn method
	 * @param username, password
	 *@return  true if user name and password match
	 *    false if password does not match or user name does not exists
	 *    
	 * if successful update last login and set status to 1 (online)
	 * Glosary: Status (enum;0 for offline 1 for online, 2 for away, 3 for  busy, 4 for in a call ) DEFAUL null
	 * 
	 * NOTE: status checking not implemented, should be restricted in GUI (sign in should be 
	 * disabled if logged in )
	 * **/
	public boolean logIn(String username, String password) {
		try {
			preparedStatement = connection.prepareStatement("SELECT count(*) FROM Users WHERE username= ? AND password= ?");

			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) { // rs.next() is false if nothing is found
				int count = resultSet.getInt(1);
				if(count == 1){
					//login authorization successful 
					//set the lastLogin field to the current time and status to 1 (online), in query add+1
					java.util.Date date= new java.util.Date();
					Timestamp time = new Timestamp(date.getTime());
					preparedStatement = connection.prepareStatement("UPDATE Users SET lastLogin = ?, status= 1 WHERE username= ?");
					preparedStatement.setString(1, time.toString());
					preparedStatement.setString(2, username);
					preparedStatement.execute();
					preparedStatement.close();
					System.out.println("log in for user :"+username+"succesfull");
					return true;
				}
			}
		}
		catch (SQLException e) {
			System.out.println("Error in db while registration: " + e.getMessage());
			e.printStackTrace();
		}finally{

			closeStatement(preparedStatement);
		}
		return false;
	}
	/**
	 * looks up in DB if the user exist:
 		if user does not exists : return false
		if user has status of â€œoff/callâ€� (is either offline-0 or in a call-4) return false;
		if user has status of 1 for online, 2 for away, 3 for  busy,  return true;
	 * 
	 * **/
	public boolean callCheckAvailable (String username) {
		try{
			if(isRegistered(username)){
				String query_getUserStatus = "SELECT status FROM Users WHERE username= ?";
				preparedStatement = connection.prepareStatement(query_getUserStatus);
				preparedStatement.setString(1, username);
				resultSet =  preparedStatement.executeQuery();

				//if user in call or offline return false
				if(resultSet.next()) { // rs.next() is false if nothing is found
					String status = resultSet.getString(1); 
					if(status.equals("0") || status.equals("4")) return false;
				}
				return true;
			}
		}catch(SQLException e){
			System.out.println("Error: "+ e.getMessage());
			e.printStackTrace();
		}finally{
			closeStatement(preparedStatement);		
		}
		System.out.println("User with such name does not exists!");
		return false; 
	}

	/**method to update the user status for a given user name (use for log out functionality etc. when LogIN 
	 * status is changed by the LogIn function)
	 * @param username and status
	 * @return true if  status in range and user name exists , false otherwise 
	 * Glosary: status: 1 for online, 2 for away, 3 for  busy, 4 for incall, 5 for idle; 
	 * 
	 * **/
	public boolean updateUserStatus(String username,String status){
		try {
			if(status.matches("[0-5]") && isRegistered(username) ){
				String query_updateUserStatus= "UPDATE Users SET status = ? WHERE username = ?";
				preparedStatement = connection.prepareStatement(query_updateUserStatus);
				preparedStatement.setString(2, username);
				preparedStatement.setString(1, status);
				preparedStatement.execute();
				return true;
			}
		} catch (SQLException e) {
			System.out.println("Error: "+ e.getMessage());
			e.printStackTrace();
		}finally{
			closeStatement(preparedStatement);
		}
		System.out.println("InvalidStatus");
		return false;
	}


	public void delete(String username) {
		try{
			preparedStatement = connection.prepareStatement("DELETE FROM Users WHERE username= ?");
			preparedStatement.setString(1, username);
			preparedStatement.execute();
		}catch(SQLException e){
			System.out.println("Error: "+ e.getMessage());
			e.printStackTrace();
		}finally{
			closeStatement(preparedStatement);
		}
	}
	/**
	 * Method to get lastLogin date for a user
	 * return Timestamp use:
	 * java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * sdf.format(date)
	 * to drop out the milisecond and use accordingly
	 * 
	 *@return the date or null if registered, but still haven't logged in 
	 * **/
	private String getLastLogin(String username) {
		try{
			preparedStatement = connection.prepareStatement("SELECT lastLogin FROM Users WHERE username= ?");
			preparedStatement.setString(1, username);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				Timestamp date;
				date = resultSet.getTimestamp("lastLogin");
				SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				return sdf.format(date);
			}
		}catch (SQLException e) {
			System.out.println("Error: "+ e.getMessage());
			e.printStackTrace();
		}finally{
			closeStatement(preparedStatement);
		}
		return null;
	}

	/**
	 * Method for adding friends to the db
	 * @param fromUser, ToUser
	 * @must the username you want to add as a friend must exist and check for consistency if the
	 * pending request is not already send
	 * 1.first when user1 request friendship to user2
	 * 2.second when user2 request friendship to user1
	 * if both rows exist the two users become friends
	 * @Glosary sets relation status to : 1 ( Pending Friend Request)
	 * 			calls updateFrinds(if both rows exist set status to 2 (Confirm Friend Request)
	 * **/
	public boolean addFriend(String fromUser, String ToUser){
		try{
			if(!checkFriendRequestExists(fromUser, ToUser) &&  isRegistered(ToUser)){
				String query_addFriend= "INSERT INTO RelationshipType (username,username2,relationship) VALUES (?,?,?)";
				preparedStatement = connection.prepareStatement(query_addFriend);
				preparedStatement.setString(1, fromUser);
				preparedStatement.setString(2, ToUser);
				preparedStatement.setInt(3,1);
				preparedStatement.execute();	
				if (updateFriends()){
					return true;
				}

			}
		}catch (SQLException e) {
			System.out.println("Error in addFriends: "+ e.getMessage());
			e.printStackTrace();}
		finally{
			closeStatement(preparedStatement);
		}
		return false;


	}

	/**
	 * Method for deletion a friendship from the db
	 * 
	 * @param username1 , username2
	 * If a deletion request is sent from whoever user to delete its' friend
	 * the deletion will occur in both ways and in neither friend's list the opposite user name will appear 
	 * That means the user you want to delete will not have to  give his/her confirmation. 
	 * 
	 * @return true if successful ,false if not.
	 * relation must exist (USERS MUST BE FRIENDS ), user name must be real
	 * **/
	public boolean deleteFriendship(String username, String username2){
		boolean successful = false;
		try{
			boolean friends = checkFriendRequestExists(username2,username);
			System.out.println("Friends: "+ friends);
			
			if(checkFriendRequestExists(username, username2)&& friends ){
				String query_removeFriend= "DELETE FROM RelationshipType WHERE username= ? AND username2 = ?";
				preparedStatement = connection.prepareStatement(query_removeFriend);
				preparedStatement.setString(1, username);
				preparedStatement.setString(2, username2);
				preparedStatement.execute();
				closeStatement(preparedStatement);
				successful = true;
				}
				
			if( friends){
				String query2_removeFriend= "DELETE FROM RelationshipType WHERE username= ? AND username2 = ?";
				preparedStatement = connection.prepareStatement(query2_removeFriend);
				preparedStatement.setString(1, username2);
				preparedStatement.setString(2, username);
				preparedStatement.execute();
				closeStatement(preparedStatement);
				successful = true;
				}

		}catch (SQLException e) {
			closeStatement(preparedStatement);
			System.out.println("Error in addFriends: "+ e.getMessage());
			e.printStackTrace();}
		finally{
			
		}
		return successful;
	}



	/**
	 * 
	 * 
	 * Helper method to check if the record exists in the db
	 * 
	 * **/
	private boolean checkFriendRequestExists(String username, String username2){
		try{
			//3.Execute SQL query
			preparedStatement = connection.prepareStatement("SELECT count(*) FROM RelationshipType WHERE username= ? AND username2 = ?");
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, username2);
			resultSet =  preparedStatement.executeQuery();
			if(resultSet.next()) {
				int count = resultSet.getInt(1);
				if(count == 0) return false;
			}	
		}catch (SQLException e) {
			System.out.println("Error in checkifExistsFriends: "+ e.getMessage());
			e.printStackTrace();
		}
		finally{
			closeStatement(preparedStatement);
		}
		return true;
	}

	/**
	 * Check if both users have made successful fried request
	 * that is : if (user1, user2) and (user2,user1) both exist
	 * if yes set relationship status to 2-Confirm Friend Request
	 * boolean to check if update was successful
	 * 
	 * Internal working : 1.Finds the relationships pairs
	 * 					  2.For each relationship pair modify status if must
	 * 
	 * When a Statement object is closed, its current ResultSet object, if one exists, is also closed.
	 * 
	 **/
	private boolean updateFriends(){
		try{
			String query_findFriendships = "SELECT  f1.* from RelationshipType f1 inner join RelationshipType f2 on f1.username = f2.username2 and f1.username2 = f2.username;";
			preparedStatement = connection.prepareStatement(query_findFriendships);
			resultSet =  preparedStatement.executeQuery();
			//	closeStatement(preparedStatement);
			while(resultSet.next()) {
				String query_updateFriends = "UPDATE RelationshipType SET relationship = ? WHERE (username= ? AND username2= ?)";
				preparedStatement = connection.prepareStatement(query_updateFriends);
				preparedStatement.setInt(1,2);
				preparedStatement.setString(2, resultSet.getString("username"));	
				preparedStatement.setString(3, resultSet.getString("username2"));
				preparedStatement.execute();				
			}
			return true;

		}catch(SQLException e){
			System.out.println("Error in updateFriends: "+ e.getMessage());
			e.printStackTrace();
		}finally{
			closeStatement(preparedStatement);
		}
		return false;
	}



	/**
	 * get ArrayList of the friends(confirmed) and pending(requested) request for a given user as well as the 
	 * LastLogin field 
	 * 
	 * status: 1 for pending (send , but not accepted)
	 * 			2 for accepted (friendships)
	 * USE: 
	 * 	for(int i = 0 ; i<list.size(); i++){
				if ((i%3)==0)
					System.out.println("USER: "+ list.get(i));
				if ((i%3)==1)
					System.out.println("Status: "+ list.get(i));
				if ((i%3)==2)
					System.out.println("LastLogin: "+ list.get(i));
			}
	 * 
	 **/
	public ArrayList<String> getRelationshipsFor(String user1){
		ArrayList<String> array = new ArrayList<String>();
		try{
			String query_getFriendsFor = "SELECT username, relationship FROM RelationshipType WHERE (username2 = ?  AND ( relationship= ? OR relationship = ?))";
			preparedStatement = connection.prepareStatement(query_getFriendsFor);
			preparedStatement.setString(1, user1);
			preparedStatement.setInt(2, 1);
			preparedStatement.setInt(3, 2);
			ResultSet result = preparedStatement.executeQuery();

			while(result.next()){
				array.add(result.getString("username"));
				array.add(result.getString("relationship"));
				array.add(getLastLogin(result.getString("username")));

			}
		}catch(SQLException e){
			System.out.println("Error in getFriends: "+ e.getMessage());
			e.printStackTrace();
		}finally{
			closeStatement(preparedStatement);
		}
		return array;
	}
	
	
	public boolean deletePendingRequest(String fromUser, String toUser){
		try{
			if(checkFriendRequestExists(fromUser, toUser)){
				String query_removeFriend= "DELETE FROM RelationshipType WHERE username= ? AND username2 = ?";
				preparedStatement = connection.prepareStatement(query_removeFriend);
				preparedStatement.setString(1, fromUser);
				preparedStatement.setString(2, toUser);
				preparedStatement.execute();
				closeStatement(preparedStatement);
				return true;


			}
		}catch (SQLException e) {
			System.out.println("Error in deletePendingRequest: (db) "+ e.getMessage());
			e.printStackTrace();}
		finally{
			closeStatement(preparedStatement);
		}
		return false;
	}


	protected void closeStatement( Statement resource ) {
		try {
			if (resource != null) {
				resource.close();
			}
		} catch( Exception error ) {
			System.out.println("Error in updateFriends: "+ error.getMessage());
			error.printStackTrace();
		}
	}
	/**
	 * basically convert the string into bytes 
	 * (e.g. using text.getBytes("UTF-8")) and then hash the bytes. 
	 * To represent that in a string converts the array of bytes into a 
	 * String representing the hexadecimal values of each byte in order
	 * 
	 * @param base
	 * @return
	 */
	public static String sha256(String base) {
		try{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(base.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}

	// you need to close all three to make sure
	public void closeEverything() {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
			}
		}
	}

}
