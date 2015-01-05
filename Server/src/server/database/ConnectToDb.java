package server.database;

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
		System.out.println("Connecting to db Driver");
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println(" Connection to db driver Successful");
		
		
		//1.Get a connection to the database	URL, Username, Password	
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/TP3Hdb","root","zzz");
		
		
		//2.Select statement 
	//	statement  = connection.createStatement();
		
		
		//register("User4","password");
		//System.out.println(logIn("User4","password"));
		//3.Execute SQL query
		//ResultSet myRs = statement.executeQuery("select * from Users");
		
		//4. Process the result set
	//	while(myRs.next()){
		//	System.out.println(myRs.getString("lastLogin"));
	//	}
		
		
	}catch(ClassNotFoundException error){
		System.out.println("Error: "+ error.getMessage());
	}
	catch(SQLException error){
		System.out.println("Error: " + error.getMessage());
	}finally{
		System.out.println("The connection was closed, foreveerrrr");
		closeEverything(resultSet,statement,connection);
	}
	
	}
	 
	 public void register( String name,String password) throws SQLException {
		 if(!isRegistered(name)){
			 preparedStatement= connection.prepareStatement("INSERT INTO Users(username,password) VALUES (?,?)");
			 preparedStatement.setString(1,name);
			 preparedStatement.setString(2,password);
			 preparedStatement.execute();
		 }else{
		 System.out.println("The user already Exists !");}
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
	 
	 public boolean logIn(String username, String password) throws SQLException{
		 preparedStatement = connection.prepareStatement("SELECT count(*) FROM Users WHERE username= ? AND password= ?");
		 preparedStatement.setString(1, username);
		 preparedStatement.setString(2, password);
		 resultSet = preparedStatement.executeQuery();
		 if(resultSet.next()) {
			 int count = resultSet.getInt(1);
			 if(count == 1){
				 //login authorization successful, set the lastLogin field to the current time
				 preparedStatement = connection.prepareStatement("UPDATE Users SET lastLogin = NOW() WHERE username= ?");
				 preparedStatement.setString(1, username);
				 preparedStatement.execute();
				 return true;
				 }
		 }
		 return false;
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