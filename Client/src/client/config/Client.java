package client.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	private String hostname;
	private int port;
	Socket socketClient;

	public Client(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	public void connect() throws UnknownHostException, IOException {
		System.out.println("Client: Attempting to connect to server...");
		socketClient = new Socket(hostname, port);
		System.out.println("Client: Connection established.");
	}

	public void readResponse() throws IOException {
		String userInput;
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				socketClient.getInputStream()));

		System.out.println("Response from server: ");

		while ((userInput = stdIn.readLine()) != null) {
			System.out.println(userInput);
		}
	}

	public void askForTime() throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				socketClient.getOutputStream()));
		writer.write("TIME?");
		writer.newLine();
		writer.flush();
	}

}
