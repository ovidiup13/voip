package voip;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import tcp.clienthandler.ClientHandler;
import tcp.sockethandler.SocketHandler;
import database.Client;
import database.ConnectToDb;
import database.IPAddressMap;
import main.ClientThread;

import org.junit.*;


/**
 * Run Each test separetly , so there is not BindException Thrown
 *
 */
public class ServerTests {
	ServerSocket serverSocket ;
	ConnectToDb db;
	IPAddressMap ClientMap;
	SocketHandler client;
	int port = 9994;
	ClientThread clientThread;
	ArrayList<SocketHandler> Clients;
	
	@Before
    public void setUp() throws Exception{
		serverSocket = new ServerSocket(port);
		db = new ConnectToDb();
		db.makeConnection();
		ClientMap = new IPAddressMap();
		Clients = new ArrayList<SocketHandler>(500);
		//set up the server on separate thread;
		Thread thread = new Thread(){
		    public void run(){
		      System.out.println("Thread Running");
		      try {
		  		
		    	  while(true){
		  			new Thread(new ClientHandler(serverSocket.accept(), ClientMap, db)).start();
		  		}
		    }catch (IOException e) {
				System.out.println("Error in serverSocket");
				e.printStackTrace();
			}
		  }};
		thread.start();

    }
	
	/**
	 * 
	 */
	@Before
	public void create500Client(){
		for(int i=0; i<500;i++){

			client = new SocketHandler("localhost", port);
			
			try {
				
				if (client.startConnection()) {
				//	System.out.println("Connection established!");
					clientThread = new ClientThread(client);
					clientThread.start();
					Clients.add(client);
					}
		} 	catch (IOException e) {
				System.out.println("Errror in creating 500 Clients");
				e.printStackTrace();
			}
		
		}
	}
	/**
	 * Method to send 500 register requests from 500 client
	 * The request are processed up to the server side, without 
	 * actually registering them
	 * Intentionally the requests are with the same name
	 * so that the db will simply reject them
	 * Result: all request are processed with no server crash
	 */
	@Test
	public void register500Clients(){
		for(int i=0; i<Clients.size();i++){
			SocketHandler client = Clients.get(i);
			 client.sendRegisterRequest("user", "password");
		}
	}
	
	

	
	/**
	 * Result: the server correctly processes 500 client call requests(no crash)
	 */
	@Test
	public void make500CallRequests(){
		for(int i=100; i<Clients.size()+100;i++){
			SocketHandler client = Clients.get(i-100);
			 client.sendCallRequest(String.valueOf(i));

		}
	}
	/**
	 * Method to handle 500 Call Requests and after that 500 call Responses
	 * and after that 500 End Call Request.
	 * 
	 * Result:  The server behaves as expected without crashing
	 */
	@Test
	public void make500CallProsedures(){
		for(int i=100; i<Clients.size()+100;i++){
			SocketHandler client = Clients.get(i-100);
			 client.sendCallRequest(String.valueOf(i));

		}
		for(int i=100; i<Clients.size()+100;i++){
			SocketHandler client = Clients.get(i-100);
			 client.sendCallResponse(true);

		}
		for(int i=100; i<Clients.size()+100;i++){
			SocketHandler client = Clients.get(i-100);
			 client.sendEndCallRequest();
			 }

		}
		
		/**
		 * This routine is correctly processed by the server , but the database throws
		 * ResultSet already requested. Also there is sometimes exceptions in 
		 * Thead : Exception in thread "Thread-284" java.lang.NullPointerException
		 */
		@Test
		public void make500FriendRequests(){
			for(int i=100; i<Clients.size()+100;i++){
				SocketHandler client = Clients.get(i-100);
				client.sendAddFriendRequest(String.valueOf(i+1));


			}
	}
	
		
		
	
	
}
