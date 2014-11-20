package connection;
import java.io.IOException;
import java.net.UnknownHostException;

import client.Client;
import server.Server;

public class ThreadConnect {
	
	private static final int port = 9990;
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Thread serverThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Server server = new Server(port);
					server.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});

		serverThread.start();
		
		/*try {
			serverThread.join();
			serverThread.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

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
