package main;

import tcp.sockethandler.SocketHandler;

public class MainClient {
	
	private static final int port = 9991;

	public static void main(String[] args) {
		SocketHandler client = new SocketHandler("localhost", port);
		// trying to establish connection to the server
		client.startConnection();
		// asking server for time
		client.sendRegisterRequest();
		//get response from server
		System.out.println(client.getResponse());
		//close connection
		client.closeConnection();
	}

}
