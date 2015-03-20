package voip;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.*;
import org.sqlite.SQLiteConfig;

import database.ConnectToDb;
import database.ResultPair;


/**
 * Due to the fact that the database have concurrency mechanism only on one 
 * connection , the result of the performed methods is observed visually on a GUI 
 * helper program - SQLite Manager (Mozila Firegox extension).
 *
 */
public class DatabaseTests {
		private ConnectToDb db;
		private ResultPair pair;
		private ResultPair pair2;
		@Before
	    public void setUp() throws Exception{
	        db = new ConnectToDb(); 
	        db.makeConnection();
	        insertTestData();
	    }
		
		
		@Test //the user Already exists
	    public void testRegister() throws SQLException {
				  assertFalse(db.register("username15", "password"));
			}
		
		/**
		 * result: successful By observing the database visually
		 * the user is correctly added to the database, and the method returs as 
		 * expected
		 */
		//@Test //run this separately 
	    public void testRegister2() throws SQLException {
				  assertTrue(db.register("username99", "password"));
			}
		
		

		
		@Test
	    public void testLogIn() throws SQLException {

			boolean success = false;
			if(db.isRegistered("username")){
				  success = db.logIn("username", "password");
				  assertTrue(success);
				  }
			else{ //cant log in for unregisted user
				success= db.logIn("username", "password");
				assertFalse(success);
			}	
		}
		@Test
	    public void testDeletePendingRequest() {
 
			
			 boolean pass = true;
			
			db.deletePendingRequest("user60","user70");
			
			 
			ArrayList<String> friends = db.getRelationshipsFor("user70");
			
			for(int i=0; i < friends.size(); i++){
				if ((i%3)==0){
					if(friends.get(i).equals("user60"));
						pass= false;	
				}
			}
			assertTrue(pass);
			
			

		}
		
		@Test
	    public void testgetFriendshipsFor() {
			//prepare and clean for the test 
			
			 boolean fr1 = false;
			 boolean fr2 = false;
			 boolean fr3 = false;
			 boolean fr4 = false;
		 
			ArrayList<String> friends = db.getRelationshipsFor("user60");
			
			for(int i=0; i < friends.size(); i++){
				if ((i%3)==0){
					if(friends.get(i).equals("user70"));
						fr1= true;	
					if(friends.get(i).equals("user64"));
						fr2= true;
					if(friends.get(i).equals("username"));
						fr3= true;
					if(friends.get(i).equals("user75"));
						fr4= true;	
				}
			}
			assertTrue(fr1 &&fr2 && fr3 &&fr4 );
			
			

		}
		
		@Test
	    public void tesAddFriend() {

			assertTrue(pair.getSuccessful() && pair2.getSuccessful());
			
			

		}
		
		@Test
	    public void testdeleteFrindship() {

			boolean pass=true;
			db.deleteFriendship("user60", "user70");
		
			//get first users's Friends
			ArrayList<String> friends = db.getRelationshipsFor("user60");
			for(int i=0; i < friends.size(); i++){
				if ((i%3)==0){
					if(friends.get(i).equals("user70"));
						pass= false;	
				}
			}	
			//get second user's Friends
			ArrayList<String> friends2 = db.getRelationshipsFor("user70");
			for(int i=0; i < friends2.size(); i++){
				if ((i%3)==0){
					if(friends.get(i).equals("user60"));
						pass= false;	
				}
			}
			assertTrue(pass);
			

		}
		
		@Test
	    public void testupdateFrindship() {

			boolean pass=false;
			 
			ArrayList<String> friends = db.getRelationshipsFor("user60");
			
			for(int i=0; i < friends.size(); i++){
				if ((i%3)==0 && i< friends.size()-1){
					if(friends.get(i).equals("user70"));
						if(friends.get(i+1).equals("2"))
							pass=true;
				
				}}
			assertTrue(pass);
			

		}
		//@Test //result : 11 sec
		public void howFast100UsersCanRegister(){
			long startTime = System.currentTimeMillis();
			for(int i =100 ; i< 200 ; i++){
				db.register(String.valueOf(i), "password");
			}
			long estimatedTime = System.currentTimeMillis() - startTime;
			System.out.println("Time in sec to register 100 users: " +estimatedTime / 1000);
		}
		
		//@Test // 200 Users :23 seconds ;  300 :34 ; 500:59 ;700:85; 1000: 121
		public void howFast200UsersCanRegister(){
			long startTime = System.currentTimeMillis();
				for(int i =100 ; i< 500 ; i++){
					db.register(String.valueOf(i), "password");
				}
			long estimatedTime = System.currentTimeMillis() - startTime;
			System.out.println("Time in sec to register 100 users: " +estimatedTime / 1000);
				}
		
		
		//@Test // ( OLD : 50 NEW RELATIONSHIPS: 317) NEW:  50 NEW RELATIONS :23
		//100 NEW RELATIONS :46
		public void howFastisUpdateFriends50Entires(){
			long startTime = System.currentTimeMillis();
				for(int i =100 ; i< 200 ; i++){
					db.addFriend(String.valueOf(i), "100");
					db.addFriend("100", String.valueOf(i));
				}
			long estimatedTime = System.currentTimeMillis() - startTime;
			System.out.println("Time in sec to register 100 users: " +estimatedTime / 1000);
				}
		
		private void  insertTestData(){
			db.register("username", "password");
			db.register("username15", "password");	
			db.register("user60", "password");
			db.register("user70", "password");	
			db.register("user64", "password");
			db.register("user75", "password");	
			pair =db.addFriend("user60", "user70");
			pair2 =db.addFriend("user60", "user64");
			db.addFriend("user60", "username");
			db.addFriend("user60", "user75");
			db.addFriend("user70", "user60");
			db.logIn("user60", "password");
			db.logIn("user70", "password");	
			db.logIn("user64", "password");
			db.logIn("user75", "password");
			db.logIn("username", "password");
			
		}

	
}
