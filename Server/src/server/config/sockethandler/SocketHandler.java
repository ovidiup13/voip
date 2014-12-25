package server.config.sockethandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import server.config.clienthandler.ClientHandler;

public class SocketHandler {

	private ServerSocket serverSocket;
	private int port;

	public SocketHandler(int port) {
		this.port = port;
	}

	public void start() throws IOException {
		System.out.println("Server: Initialising server...");
		serverSocket = new ServerSocket(port);
		System.out.println("Server: Server started.");

		while (true) {

			System.out.println("Server: Awaiting client...");
			Socket client = serverSocket.accept();

			System.out.println("Server: Client found, sending message...");
			new Thread(new ClientHandler(client)).start();
		}
	}

}
