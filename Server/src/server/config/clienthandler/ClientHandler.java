package server.config.clienthandler;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import protocol.config.buffers.ClientRequest.Request;

public class ClientHandler implements Runnable {

	private Socket client;

	public ClientHandler(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
			System.out.println("Client found and started thread "
					+ Thread.currentThread().getName());
			readResponse();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void readResponse() throws IOException, InterruptedException {

		Request clientRequest;

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				client.getInputStream()));

		//System.out.println(stdIn.readLine());

		
	}

	private void respond() throws IOException, InterruptedException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				client.getOutputStream()));
		writer.write("");
		writer.flush();
		writer.close();
	}
}
