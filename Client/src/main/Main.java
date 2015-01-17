package main;

import tcp.sockethandler.SocketHandler;

public class Main {
	
	private static final int port = 9990;

	public static void main(String[] args) {
		Thread clientThread = new Thread(new Runnable() {
			
		@Override
		public void run() {
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
		});
		
		clientThread.start();

	}

}
