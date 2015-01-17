package main;

import tcp.clienthandler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
	
	private static final int port = 9991;

	public static void main(String[] args) {
		boolean listening = true;

		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while(listening){
				new ClientHandler(serverSocket.accept()).run();
			}
		} catch (IOException e) {
			System.err.println("Server: could not init socket");
		}
	}

}
