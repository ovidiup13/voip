package client.main;

import java.io.IOException;
import java.net.UnknownHostException;

import client.config.Client;

public class InitClient {
	
	private static final int port = 9990;

	public static void main(String[] args) {
		Thread clientThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Client client = new Client("localhost", port);
				try {
					// trying to establish connection to the server
					client.connect();
					// asking server for time
					client.askForTime();
					// waiting to read response from server
					client.readResponse();

				} catch (UnknownHostException e) {
					System.err
							.println("Host unknown. Cannot establish connection");
				} catch (IOException e) {
					System.err
							.println("Cannot establish connection. Server may not be up."
									+ e.getMessage());
				}

			}

		});
		
		clientThread.start();

	}

}
