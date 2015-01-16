package main;
import java.io.IOException;

import tcp.sockethandler.SocketHandler;

public class InitServer {
	
	private static final int port = 9991;

	public static void main(String[] args) {

		Thread serverThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SocketHandler server = new SocketHandler(port);
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
