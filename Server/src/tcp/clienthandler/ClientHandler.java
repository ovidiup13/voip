package tcp.clienthandler;

import buffers.ClientRequest.Request;
import buffers.ServerResponse.Response;
import database.ConnectToDb;
import database.IPAddressMap;
import interfaces.ResponseSender;
import writers.ResponseWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientHandler implements Runnable, ResponseSender {

	private static int callID = 0;
	
	private Socket clientSocket;
	private ResponseWriter responseWriter;
	private InputStream input;
	private OutputStream output;
	private ConnectToDb db;
	private IPAddressMap addressMap;

	//will need to pass parameter but will conflict
	public ClientHandler(Socket clientSocket, IPAddressMap addressMap) {
		System.out.println("Server: Client found...");
		
		this.clientSocket = clientSocket;
		this.addressMap = addressMap;
		
		//create request writer
		responseWriter = new ResponseWriter();
		
		//get input and output stream of socket
		try {
			input = clientSocket.getInputStream();
			output = clientSocket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//make connection to db handler
		db =  new ConnectToDb();
	}

	@Override
	public void run() {
		readRequest();
		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Server: Client logged out successfully.");
		System.out.println("Listening for other connections...");
	}

	public void readRequest() {
		Request request;
		
		while(true) {
			try {
				request = Request.parseDelimitedFrom(input);
			} catch (IOException e) {
				System.err.println("ClientHandler: cannot read request");
				return;
			}
			
			if (request == null) break;
			Request.ReqType type = request.getRqType();
			
			if (type.equals(Request.ReqType.LOUT)) {
				//remove the ClientID from the OnlineHashTable

				//if ok, send confirmation response
				sendResponse(true, "LogOut successful");
				return;
			} 
			else
				switch(type){
					case REG:  { readRegisterRequest(request); break; }
					case LIN:  { readLogInRequest(request); break; }
					case CALL: { readCallRequest(request); break; }
					case STS:  { break; } //to be implemented
				}
			}
	}
	
	private void readRegisterRequest(Request request){
		db.makeConnection();
		System.out.println("Server: Request is of type REG, processing...");
		System.out.println("Server: Adding user to database...");
		if (db.register(request.getReg().getUsername(), request.getReg().getPassword())) {
			//if ok, send confirmation response
			sendResponse(true, "registration successful - you can log in now");
			System.out.println("Server: Sent successful response.");
		} else {
			sendResponse(false, "registration unsuccessful - the user already exists");
			System.out.println("Server: Sent unsuccessful response.");
		}
		db.closeEverything();
	}
	
	private void readLogInRequest(Request request){
		String username = request.getLin().getUsername();
		String password = request.getLin().getPassword();

		db.makeConnection();
		if (db.logIn(username, password)) {
			//add the user's IP the the map of active users
			addressMap.addIP(username, clientSocket);

			//modify user's status in db to idle
			db.updateUserStatus(username, "5");

			System.out.println("ClientIP" + addressMap.getIP(username));

			//if ok, send confirmation response
			sendResponse(true, "Login successful");
		} else {
			sendResponse(false, "Login unsuccessful");
		}

		db.closeEverything();
	}
	
	private void readCallRequest(Request request){
		String callee = request.getUsername();
		System.out.println(callee);
		//if (db.callCheckAvailable(request.getUsername()))
		if (addressMap.isOnline(callee)) {
			//(true, "You can call the user");

			//get the ClientIP from the onlineHashTable, and send it as responce
			Socket connectionToCallee = addressMap.getIP(callee);

			//send response to client2
			sendCallResponse(connectionToCallee, callID++);

			//send response to client1

			//call iD must be unique for every call
			
			//chenge user status to in-call

			//db.updateUserStatus(request.getReg().getUsername(), "4");
		} else {
			sendResponse(false, "Call unsuccessful");
		}
	}

	@Override
	public void sendResponse(boolean ok, String message) {
		Response response = responseWriter.createActionResponse(ok, message);
		try {
			response.writeDelimitedTo(output);
		} catch (IOException e) {
			System.err.println("Server: could not send response");
		}
	}
	
	@Override
	public void sendCallResponse(Socket connection, int callID){
		String IPAddress = ((InetSocketAddress)connection.getRemoteSocketAddress()).getHostName();
		Response response = responseWriter.createCallResponse(IPAddress, callID);
		try {
			response.writeDelimitedTo(output);
		} catch (IOException e) {
			System.err.println("Server: could not send response.");
		}
	}

	@Override
	public void sendEndCallResponse(boolean ok) {
		//to be implemented
	}
}
