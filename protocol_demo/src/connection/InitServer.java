package connection;

import java.io.IOException;

import server.Server;

public class InitServer {
	
	private static final int port = 9990;

	public static void main(String[] args) {

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
		try {
			serverThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}
