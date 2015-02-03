package database;

import java.sql.*;

public class ConnectToDb {
	private Connection connection = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;	
	private ResultSet resultSet = null;




	public void makeConnection(){


		try
		{
			//Utilize the driver


			//Class.forName("com.mysql.jdbc.Driver");
			Class.forName("org.sqlite.JDBC");

			//System.out.println(" Connection to db driver Successful");


			//1.Get a connection to the database	URL, Username, Password	
			//connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/TP3Hdb","root","zzz");
			connection =  DriverManager.getConnection("jdbc:sqlite:Server/src/database/TP3Hdb.db");
//			Statement stmt = connection.createStatement();
//			stmt.executeUpdate("PRAGMA foreign_keys = ON");
//			System.out.println("Opened database successfully");


		//	register("User11","password");
//			register("User12","password");
//			register("User13","password");
//			register("User14","password");
//			register("User15","password");
//			register("User16","password");
				
			
//			addFriend("User16","User15");
//			addFriend("User15","User16");
//			
//			addFriend("User15","User13");
//			addFriend("User13","User15");
//			addFriend("User13","User15");
//			addFriend("User13","User15");
//			addFriend("User13","User15");
//			
			addFriend("User15","Viktor");
				
			getFriendsFor("User15");
;


		}catch(ClassNotFoundException error){
			System.out.println("Error: "+ error.getMessage());
		}
		catch(SQLException error){
			System.out.println("Error: " + error.getMessage());
		}
		//		finally{
		//		System.out.println("The connection was closed, foreveerrrr");
		//		closeEverything(resultSet,statement,connection);
		//	}

	}
	/**
	 * Register method: register the user
	 * @param name, password
	 * @return true if user does not exists, false if exists
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
			try {
				preparedStatement.close();
				resultSet.close();
			} catch (SQLException e) {
				System.out.println("Error in db registration: " + e.getMessage());
				e.printStackTrace();
			}
			
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
			try {
				preparedStatement.close();
				resultSet.close();
			} catch (SQLException e) {
				System.out.println("Error in db registration: " + e.getMessage());
				e.printStackTrace();
			}
			
		}
		return false;
	}
	/**
	 * looks up in DB if the user exist:
 		if user does not exists : return false
		if user has status of “off/call” (is either offline-0 or in a call-4) return false;
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
			try {
				preparedStatement.close();
				resultSet.close();
			} catch (SQLException e) {
				System.out.println("Error in db registration: " + e.getMessage());
				e.printStackTrace();
			}
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
	public Timestamp getLastLogin(String username) {
		try{
			preparedStatement = connection.prepareStatement("SELECT lastLogin FROM Users WHERE username= ?");
			preparedStatement.setString(1, username);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				Timestamp date;
				return date = resultSet.getTimestamp("lastLogin");
			}
		}catch (SQLException e) {
			System.out.println("Error: "+ e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Method for adding friends to the db
	 * @param fromUser, ToUser
	 * must use this method twice :  
	 * 1.first when user1 request friendship to user2
	 * 2.second when user2 request friendship to user1
	 * if both rows exit the two users become friends
	 * @Glosary sets relation status to : 1 ( Pending Friend Request)
	 * 			calls updateFrinds
	 * 			
	 * @important first ask for the acceptance of the requested user then use this 
	 * method to modify db.
	 * @throws excepts if users does not exist 
	 *@return 
	 * **/
	public void addFriend(String fromUser, String ToUser){
		try{
			if(!checkFriendRequestExists(fromUser, ToUser)){
				System.out.println("I am here");
				String query_addFriend= "INSERT INTO RelationshipType (username,username2,relationship) VALUES (?,?,?)";
				preparedStatement = connection.prepareStatement(query_addFriend);
				preparedStatement.setString(1, fromUser);
				preparedStatement.setString(2, ToUser);
				preparedStatement.setInt(3,1);
				preparedStatement.execute();
				updateFriends(fromUser,ToUser);
			}
		}catch (SQLException e) {
			System.out.println("Error in addFriends: "+ e.getMessage());
			e.printStackTrace();}
	
		
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
				System.out.println(count);
				if(count == 0) return false;
			}
			
	}catch (SQLException e) {
		System.out.println("Error in checkifExistsFriends: "+ e.getMessage());
		e.printStackTrace();}
		return true;

		
	}
	/**
	 * Check if both users have made successful fried request
	 * if yes set relationship status to 2-Confirm Friend Request
	 * 
	 * NOT WORKING PROPERLY MUST FIX 
	 * TO DO
	**/
	private void updateFriends(String user1, String user2){
		try{
			String query_updateFriends = "UPDATE RelationshipType SET relationship = ? WHERE (username= ? OR username2= ?) AND (username= ? OR username2= ?)";
			preparedStatement = connection.prepareStatement(query_updateFriends);
			preparedStatement.setInt(1,2);
			preparedStatement.setString(2, user1);	
			preparedStatement.setString(3, user1);
			preparedStatement.setString(4, user2);
			preparedStatement.setString(5, user2);
			preparedStatement.execute();

		}catch(SQLException e){
			System.out.println("Error in updateFriends: "+ e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void getFriendsFor(String user1){
		try{
			String query_getFriendsFor = "SELECT username2 FROM RelationshipType WHERE (username= ?  AND relationship= ?)";
			preparedStatement = connection.prepareStatement(query_getFriendsFor);
			preparedStatement.setString(1, user1);
			preparedStatement.setInt(2, 2);
			ResultSet result = preparedStatement.executeQuery();
			while(result.next()){
				System.out.println(result.getString("username2"));
				
			
				}
			

		}catch(SQLException e){
			System.out.println("Error in getFriends: "+ e.getMessage());
			e.printStackTrace();
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