package main;

import tcp.clienthandler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;

import database.IPAddressMap;

public class MainServer {
	
	private static final int port = 9992;

	public static void main(String[] args) {
		boolean listening = true;

		try {
			System.out.println("Starting server...");
			ServerSocket serverSocket = new ServerSocket(port);
			while(listening){
				System.out.println("Listening for connections...");
				//init the IPMap
				IPAddressMap ClientMap = new IPAddressMap();
				new ClientHandler(serverSocket.accept(), ClientMap).run();
			}
		} catch (IOException e) {
			System.err.println("Server: could not init socket");
		}
	}

}
