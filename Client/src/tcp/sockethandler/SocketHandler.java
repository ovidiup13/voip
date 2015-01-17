package tcp.sockethandler;

import buffers.ClientRequest.Request;
import buffers.ServerResponse.Response;
import tcp.messagehandler.RequestWriter;
import java.io.*;
import java.net.Socket;

/**
 * @author Ovidiu Popoviciu
 * */

public class SocketHandler {

	private String hostname;
	private int port;
	Socket socketClient;
	RequestWriter requestWriter;

	public SocketHandler(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
		requestWriter  = new RequestWriter();
	}

	public void startConnection() {
		try {
			socketClient = new Socket(hostname, port);
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to open socket");
		}
	}
	
	public void sendRegisterRequest() {
		Request request = requestWriter.createRegisterReq("username", "default");
		try {
			request.writeDelimitedTo(socketClient.getOutputStream());
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to send register request");
		}
	}
	
	public void sendLogInRequest() {
		Request request = requestWriter.createLogInReq("username", "default");
		try {
			request.writeDelimitedTo(socketClient.getOutputStream());
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to send login request");
		}
	}
	
	public void sendLogOutRequest() {
		Request request = requestWriter.createLogOutReq("username");
		try {
			request.writeDelimitedTo(socketClient.getOutputStream());
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to send logout request");
		}
	}

	public boolean getResponse() {
		Response response = null;
		/*BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				socketClient.getInputStream()));*/
		
		//while((userInput = stdIn.readLine()) != null){
		try {
			response = Response.parseDelimitedFrom(socketClient.getInputStream());
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to open input stream");
		}
		//}
		return response.getResult().getOk();
	}

	public void closeConnection() {
		try {
			socketClient.close();
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to close socket");
		}
	}
}
