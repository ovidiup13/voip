package database;

import java.security.MessageDigest;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.sqlite.SQLiteConfig;

public class ConnectToDb {
	private Connection connection = null;
	private PreparedStatement registerStm,isRegisteredStm, logInStm,getLastLoginStm,addFriendStm,removeFriendStm;	
	private PreparedStatement checkFriendRequestExistsStm, getRelationshipsStm,findFriendshipsStm,updateLastLoginStm;
	private ResultSet resultSet = null;
	private PreparedStatement updateFriendsStm;

	private static final String DB_URL = "jdbc:sqlite:Server/src/database/TP3Hdb.db";  
	private static final String DRIVER = "org.sqlite.JDBC";  

	public enum TypeAction {REQUEST_FR, RESPONSE_FR,PENDING_DEL , FRIENDSHIP_DEL};




	/**
	 * Connection method : connect to the db driver
	 * 
	 * @return void
	 * @throws SQLException , ClassNotFoundException
	 * This implementation  does not turn on foreign keys!
	 * **/
	public void makeConnection()  { 
		try { 
			Class.forName(DRIVER);     
			SQLiteConfig config = new SQLiteConfig();  
			config.enforceForeignKeys(true);  
			connection = DriverManager.getConnection(DB_URL);  
			
			initPrepareStatements();
			
		}catch(ClassNotFoundException error){
			System.out.println("Error: "+ error.getMessage());
		}catch(SQLException error){
			System.out.println("Error in connecting to db: " + error.getMessage());
		}
	}
	
	
	private void initPrepareStatements(){
		try {
			registerStm= connection.prepareStatement("INSERT INTO Users(username,password) VALUES (?,?)");
		} catch (SQLException e) {
			System.out.println("Register statement failed to compile:");
			e.printStackTrace();
		}
		
		try {
			isRegisteredStm = connection.prepareStatement("SELECT count(*) FROM Users WHERE username= ?");
		} catch (SQLException e) {
			System.out.println("isRegistered statement failed to compile:");
			e.printStackTrace();
		}
		
		try {
			logInStm = connection.prepareStatement("SELECT count(*) FROM Users WHERE username= ? AND password= ?");
		} catch (SQLException e) {
			System.out.println("LogIn statement failed to compile:");
			e.printStackTrace();
		}
		
		try {
			getLastLoginStm = connection.prepareStatement("SELECT lastLogin FROM Users WHERE username= ?");
		} catch (SQLException e) {
			System.out.println("getLastLogin statement failed to compile:");
			e.printStackTrace();
		}
		
		try {
			String query_addFriend= "INSERT INTO RelationshipType (username,username2,relationship) VALUES (?,?,?)";
			addFriendStm = connection.prepareStatement(query_addFriend);
		} catch (SQLException e) {
			System.out.println("addFriend statement failed to compile:");
			e.printStackTrace();
		}
		String query_removeFriend= "DELETE FROM RelationshipType WHERE username= ? AND username2 = ?";
		try {
			removeFriendStm = connection.prepareStatement(query_removeFriend);
		} catch (SQLException e) {
			System.out.println("removeFriend statement failed to compile:");
			e.printStackTrace();
		}
		try {
			checkFriendRequestExistsStm = connection.prepareStatement("SELECT count(*) FROM RelationshipType WHERE username= ? AND username2 = ?");
		} catch (SQLException e) {
			System.out.println("checkFriendRequestExists statement failed to compile:");
			e.printStackTrace();
		}
		try {
			String query_getFriendsFor = "SELECT username, relationship FROM RelationshipType WHERE (username2 = ?  AND ( relationship= ? OR relationship = ?))";
			getRelationshipsStm = connection.prepareStatement(query_getFriendsFor);
		} catch (SQLException e) {
			System.out.println("getRelationships statement failed to compile:");
			e.printStackTrace();
		}
		
		try {
			String query_findFriendships = "SELECT  f1.* from RelationshipType f1 inner join RelationshipType f2 on f1.username = f2.username2 and f1.username2 = f2.username WHERE ( f1.username= ? OR f1.username= ?) AND ( f2.username= ? OR f2.username= ?);";
			findFriendshipsStm = connection.prepareStatement(query_findFriendships);
		} catch (SQLException e) {
			System.out.println("findFriendships statement failed to compile:");
			e.printStackTrace();
		}
		try {
			String query_updateFriends = "UPDATE RelationshipType SET relationship = ? WHERE (username= ? AND username2= ?)";
			updateFriendsStm = connection.prepareStatement(query_updateFriends);
		} catch (SQLException e) {
			System.out.println("updateFriends statement failed to compile:");
			e.printStackTrace();
		}
		try {
			updateLastLoginStm = connection.prepareStatement("UPDATE Users SET lastLogin = ? WHERE username= ?");
		} catch (SQLException e) {
			System.out.println("updateLastLogin statement failed to compile:");
			e.printStackTrace();
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
				registerStm.setString(1,name);
				registerStm.setString(2, sha256(password));
				registerStm.execute();
				return true;
			}
		} catch (SQLException error) {
			System.out.println("Error in registation: " + error.getMessage());
			error.printStackTrace();
		}
		System.out.println("The user already Exists !");
		return false;
	}


	public boolean isRegistered( String username) throws SQLException{
		//3.Execute SQL query
		isRegisteredStm.setString(1, username);
		resultSet =  isRegisteredStm.executeQuery();
		if(resultSet.next()) {
			int count = resultSet.getInt(1);
			if(count == 0) return false;
		}
		closeResultSet(resultSet);
		return true;
	}
	/**
	 * LogIn method
	 * @param username, password
	 *@return  true if user name and password match
	 *    false if password does not match or user name does not exists
	 *    
	 * 
	 * **/
	public boolean logIn(String username, String password) {
		try {
			logInStm.setString(1, username);
			logInStm.setString(2,  sha256(password));
			resultSet = logInStm.executeQuery();
			if(resultSet.next()) { // rs.next() is false if nothing is found
				int count = resultSet.getInt(1);
				if(count == 1){
					//login authorization successful 
					//set the lastLogin field to the current time 
					java.util.Date date= new java.util.Date();
					Timestamp time = new Timestamp(date.getTime());
					updateLastLoginStm.setString(1, time.toString());
					updateLastLoginStm.setString(2, username);
					updateLastLoginStm.execute();
					System.out.println("log in for user :"+username+"succesfull");
					return true;
				}
			}
		}
		catch (SQLException e) {
			System.out.println("Error in db while registration: " + e.getMessage());
			e.printStackTrace();
		}finally{
			closeResultSet(resultSet);
		}
		
		return false;
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
			getLastLoginStm.setString(1, username);
			resultSet = getLastLoginStm.executeQuery();
			if(resultSet.next()) {
				Timestamp date;
				date = resultSet.getTimestamp("lastLogin");
				SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
				return sdf.format(date);
			}
		}catch (SQLException e) {
			System.out.println("Error: "+ e.getMessage());
			e.printStackTrace();
		}finally{
			closeResultSet(resultSet);
		}
		return null;
	}

	/**
	 * Method for adding friends to the db
	 * @param fromUser, ToUser
	 * @must the username you want to add as a friend must exist in users table  and check for consistency if the
	 * pending request is not already send as well if you are not trying to add yourself as a friend 
	 * 1.first when user1 request friendship to user2
	 * 2.second when user2 request friendship to user1
	 * if both rows exist the two users become friends
	 * @Glosary sets relation status to : 1 ( Pending Friend Request)
	 * 			calls updateFrinds(if both rows exist set status to 2 (Confirm Friend Request)
	 * @return ResultPair(boolean,enum) (successful: true or false; type: RESPONSE_FR,REQUEST_FR)
	 * **/
	public ResultPair addFriend(String fromUser, String ToUser){
		try{
			if(!checkFriendRequestExists(fromUser, ToUser) &&  isRegistered(ToUser) && !fromUser.equals(ToUser)){
				addFriendStm.setString(1, fromUser);
				addFriendStm.setString(2, ToUser);
				addFriendStm.setInt(3,1);
				addFriendStm.execute();	
				if(checkFriendRequestExists(ToUser, fromUser)){
					if (updateFriends(fromUser,ToUser))
						return new ResultPair(true,TypeAction.RESPONSE_FR);				
				}
				
				return new ResultPair(true,TypeAction.REQUEST_FR);	

			}
		}catch (SQLException e) {
			System.out.println("Error in addFriends: "+ e.getMessage());
			e.printStackTrace();}
		
		return new ResultPair(false,TypeAction.REQUEST_FR);	//Type is not important here, choose random


	}

	/**
	 * Method for deletion a friendship from the db
	 * 
	 * @param username1 , username2
	 * If a deletion request is sent from whoever user to delete its' friend
	 * the deletion will occur in both ways and in neither friend's list the opposite user name will appear 
	 * That means the user you want to delete will not have to  give his/her confirmation. 
	 * 
	 * @return ResultPair(boolean,enum) ( successful:true or false, Type:PENDING_DEL,FRIENDSHIP_DEL)
	 * 	relation must exist , user name must be real
	 * **/
	public ResultPair deleteFriendship(String username, String username2){
		
		boolean areFriends = checkFriendRequestExists(username2,username);
		
		try{
			if(checkFriendRequestExists(username, username2) ){
				removeFriendStm.setString(1, username);
				removeFriendStm.setString(2, username2);
				removeFriendStm.execute();
			}
			if( areFriends){
				removeFriendStm.setString(1, username2);
				removeFriendStm.setString(2, username);
				removeFriendStm.execute();
				return new ResultPair(true,TypeAction.FRIENDSHIP_DEL);
			}
			return new ResultPair(true,TypeAction.PENDING_DEL);
		
		}catch (SQLException e) {
			System.out.println("Error in deleteFriendship: "+ e.getMessage());
			e.printStackTrace();
			}
		
		return new ResultPair(false,TypeAction.FRIENDSHIP_DEL); //Type irrelevant here
	}


	/**
	 * 
	 * 
	 * Helper method to check if the record exists in the db
	 * 
	 * **/
	private boolean checkFriendRequestExists(String username, String username2){
		try{
			checkFriendRequestExistsStm.setString(1, username);
			checkFriendRequestExistsStm.setString(2, username2);
			resultSet =  checkFriendRequestExistsStm.executeQuery();
			if(resultSet.next()) {
				int count = resultSet.getInt(1);
				if(count == 0) return false;
			}	
		}catch (SQLException e) {
			System.out.println("Error in checkifExistsFriends: "+ e.getMessage());
			e.printStackTrace();
		}finally{
			closeResultSet(resultSet);
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
	 * FUTURE WORK: Optimize this method: right now it is selecting all pairs and it is updating
	 * all existing pairs 
	 * @param toUser 
	 * @param fromUser 
	 * 
	 **/
	private boolean updateFriends(String fromUser, String toUser){
		try{
			findFriendshipsStm.setString(1, fromUser);	
			findFriendshipsStm.setString(2, toUser);
			findFriendshipsStm.setString(3, fromUser);	
			findFriendshipsStm.setString(4, toUser);
			resultSet =  findFriendshipsStm.executeQuery();
	
			while(resultSet.next()) {
				System.out.println("HEREEEE");
				updateFriendsStm.setInt(1,2);
				updateFriendsStm.setString(2, resultSet.getString("username"));	
				updateFriendsStm.setString(3, resultSet.getString("username2"));
				updateFriendsStm.execute();	
			}
			return true;

		}catch(SQLException e){
			System.out.println("Error in updateFriends: "+ e.getMessage());
			e.printStackTrace();
		}finally{
			closeResultSet(resultSet);
		}
		return false;
	}



	/**
	 * get ArrayList of the friends(confirmed) and pending(requested) request for a given user as well as the 
	 * LastLogin field 
	 * 
	 * status: 1 for pending (send , but not accepted)
	 * 			2 for accepted (friendships)
	 * 
	 **/
	public ArrayList<String> getRelationshipsFor(String user1){
		ArrayList<String> array = new ArrayList<String>();
		try{
			getRelationshipsStm.setString(1, user1);
			getRelationshipsStm.setInt(2, 1);
			getRelationshipsStm.setInt(3, 2);
			ResultSet result = getRelationshipsStm.executeQuery();

			while(result.next()){
				array.add(result.getString("username"));
				array.add(result.getString("relationship"));
				array.add(getLastLogin(result.getString("username")));

			}
		}catch(SQLException e){
			System.out.println("Error in getFriends: "+ e.getMessage());
			e.printStackTrace();
		}
		return array;
	}
	
	
	public boolean deletePendingRequest(String fromUser, String toUser){
		try{
			if(checkFriendRequestExists(fromUser, toUser)){
				removeFriendStm.setString(1, fromUser);
				removeFriendStm.setString(2, toUser);
				removeFriendStm.execute();
				return true;
			}
		}catch (SQLException e) {
			System.out.println("Error in deletePendingRequest: (db) "+ e.getMessage());
			e.printStackTrace();}
		return false;
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

	protected void closeResultSet( ResultSet resource ) {
		 try {

		 resource.close();
		
		} catch( Exception error ) {
		 System.out.println("Error in closingResultSet: "+ error.getMessage());
		 error.printStackTrace();
		 }
		 }
	//except statements 
	public void closeEverything() {
		if (resultSet != null) {
			try {
				resultSet.close();
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
