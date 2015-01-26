package main;

import tcp.clienthandler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;

import database.IPAddressMap;

public class MainServer {
	
	private static final int port = 9991;

	public static void main(String[] args) {
		boolean listening = true;

		try {
			System.out.println("Starting server...");
			ServerSocket serverSocket = new ServerSocket(port);
			//init the IPMap
			IPAddressMap ClientMap = new IPAddressMap();
			while(listening){
				System.out.println("Listening for connections...");
				new ClientHandler(serverSocket.accept(), ClientMap).run();
			}
		} catch (IOException e) {
			System.err.println("Server: could not init socket");
		}
	}

}
