package database;

import java.sql.*;
import java.lang.ClassNotFoundException;

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

			System.out.println(" Connection to db driver Successful");


			//1.Get a connection to the database	URL, Username, Password	
			//connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/TP3Hdb","root","zzz");
			connection =  DriverManager.getConnection("jdbc:sqlite:Server/src/server/database/TP3Hdb.db");
			System.out.println("Opened database successfully");



			//		
			//		register("User10","password");
			//		//System.out.println(logIn("User10","pass"));
			//	//	logIn("User3","password");
			//		System.out.println(logIn("User10","password"));
			//		System.out.println("Call : "+ callCheckAvailable("User5"));
			//		System.out.println("Update User status successfull? "+updateUserStatus("Viktor","1"));
			//		System.out.println("Is Login Succesfull "+logIn("User7","password"));
			//		
			//		//2.Select statement 
			//		statement  = connection.createStatement();
			//		//3.Execute SQL query
			//		ResultSet myRs = statement.executeQuery("select * from Users");
			//		
			//		//4. Process the result set
			//		while(myRs.next()){
			//			System.out.println(myRs.getString("status"));
			//		}

			//java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			//System.out.println("Get lastLogin for: "+ sdf.format(getLastLogin("User4")));


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
			System.out.println("Error: " + error.getMessage());
			error.printStackTrace();
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
	 * @param usurname, password
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
					System.out.println("log in for user :"+username+"succesfull");
					return true;
				}
			}
		}
		catch (SQLException e) {
			System.out.println("Error in db while registration: " + e.getMessage());
			e.printStackTrace();
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
		}
		System.out.println("User with such name does not exists!");
		return false; 
	}

	/**method to update the user status for a given user name (use for log out functionality etc. when LogIN 
	 * status is changed by the LogIn function)
	 * @param username and status
	 * @return true if  status in range and user name exists , false otherwise 
	 * Glosary: status: 1 for online, 2 for away, 3 for  busy, 4 for incall; 
	 * 
	 * **/
	public boolean updateUserStatus(String username,String status){
		try {
			if(status.matches("[0-4]") && isRegistered(username) ){
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



	// you need to close all three to make sure
	public static void closeEverything(ResultSet rs, Statement stmt,
			Connection con) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
			}
		}
	}

}