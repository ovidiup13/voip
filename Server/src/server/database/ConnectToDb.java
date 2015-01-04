package server.database;

import java.sql.*;
import java.lang.ClassNotFoundException;

public class ConnectToDb {
	 private Connection connection = null;
	 private Statement statement = null;
	 private PreparedStatement preparedStatement = null;	
	
	
	 public void makeConnection(){
		
		
		try
		{
		//Utilize the driver
		System.out.println("Connecting to Driver");
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println(" Connection to driver Successful");
		
		
		//1.Get a connection to the database	URL, Username, Password
		
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/TP3Hdb","root","zzz");
		
		
		//2.Select statement 
		statement  = connection.createStatement();
		
		
		register("User3","password");

		
		//3.Execute SQL query
		ResultSet myRs = statement.executeQuery("select * from Users");
		
		//4. Process the result set
		while(myRs.next()){
			System.out.println(myRs.getString("username"));
		}
		
		
	}catch(ClassNotFoundException error){
		System.out.println("Error: "+ error.getMessage());
	}
	catch(SQLException error){
		System.out.println("Error: " + error.getMessage());
	}
	
	}
	 
	 private void register( String name,String password) throws SQLException {
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
		 final ResultSet resultSet =  preparedStatement.executeQuery();
		 if(resultSet.next()) {
		     int count = resultSet.getInt(1);
			 if(count == 0) return false;
		 }
		 return true;
	
	
	 }


}