package tcp.sockethandler;

import buffers.ClientRequest.Request;
import buffers.ServerResponse.Response;
import writers.RequestWriter;

import java.io.IOException;
import java.net.Socket;

import p2p.SimpleVoIPCall;

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

	public boolean startConnection() {
		try {
			socketClient = new Socket(hostname, port);
			return true;
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to open socket");
		}
		return false;
	}
	
	public boolean sendRegisterRequest(String username, String password) {
		System.out.println("Client: Creating and sending register request...");
		Request request = requestWriter.createRegisterReq(username, password);
		try {
			request.writeDelimitedTo(socketClient.getOutputStream());
			System.out.println("Client: Sent register request...");
			return true;
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to send register request");
		}
		return false;
	}
	
	public void sendLogInRequest(String username, String password) {
		Request request = requestWriter.createLogInReq(username, password);
		try {
			request.writeDelimitedTo(socketClient.getOutputStream());
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to send login request");
		}
	}
	
	public void sendLogOutRequest(boolean confirm) {
		Request request = requestWriter.createLogOutReq(confirm);
		try {
			request.writeDelimitedTo(socketClient.getOutputStream());
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to send logout request");
		}
	}
	
	public void sendCallRequest(String username){
		Request request = requestWriter.createCallReq(username);
		try {
			request.writeDelimitedTo(socketClient.getOutputStream());
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to send call request");
		}
	}

	public void getResponse() {
		Response response = null;
		
		try {
			response = Response.parseDelimitedFrom(socketClient.getInputStream());
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to open input stream");
		}

		Response.ResType type = response.getResType();
		if(type.equals(Response.ResType.ACT)){
			System.out.println("Result: " + response.getReqResult().getOk());
			System.out.println("Message: " + response.getReqResult().getCause());
		}
		else if(type.equals(Response.ResType.CALL)){
			System.out.println("IP Address of callee: " + response.getCallResponse().getIpAddress());
			System.out.println("Call ID: " + response.getCallResponse().getCallID());
			
	        SimpleVoIPCall play = new SimpleVoIPCall();
	        play.start(
	        		response.getCallResponse().getIpAddress(),
	        		12345, 
	        		response.getCallResponse().getCallID()
	        );
		}
	}

	public boolean closeConnection() {
		try {
			socketClient.close();
			return true;
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to close socket");
		}
		return false;
	}
}
