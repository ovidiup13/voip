package main;

import tcp.sockethandler.SocketHandler;

public class MainClient {
	
	private static final int port = 9991;

	public static void main(String[] args) {
		SocketHandler client = new SocketHandler("localhost", port);
		// trying to establish connection to the server
		if(client.startConnection()){
			// asking server for time
			/*if(client.sendRegisterRequest("username2", "password"))
				//get response from server
				client.getResponse();*/
			
			//send a log in request
			client.sendLogInRequest("username", "default");
			System.out.println("Message for LOG IN: ");
			client.getResponse();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//send call request
			client.sendCallRequest("username");
			System.out.println("Message for CALL: ");
			client.getResponse();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//send log out request
			client.sendLogOutRequest(true);

			//close connection
			if(!client.closeConnection())
				System.err.println("Client: Connection error, could not close connection");
			else
				System.out.println("Connection closed.");
		}
		else
			System.err.println("Client: An error occurred, could not connect to server");
		
	}

}
