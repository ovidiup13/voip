package tcp.clienthandler;

import java.io.IOException;
import java.net.Socket;

import buffers.ClientRequest.Request;
import buffers.ServerResponse.Response;
import database.ConnectToDb;
import tcp.messagehandler.ResponseWriter;

public class ClientHandler implements Runnable {

	private Socket client;
	ResponseWriter responseWriter;
	private ConnectToDb db;

	public ClientHandler(Socket client) {
		this.client = client;
		responseWriter = new ResponseWriter();
		db =  new ConnectToDb();
		db.makeConnection();
	}

	@Override
	public void run() {
		readRequest();
	}

	public void readRequest() {
		Request request = null;

		try {
			request = Request.parseDelimitedFrom(client.getInputStream());
		} catch (IOException e) {
			System.err.println("ClientHandler: cannot read request");
			return;
		}

		Request.ReqType type = request.getRqType();

		if(type.equals(Request.ReqType.REG)){
			//add client to database
			System.out.println("username is: " + request.getReg().getUsername());
			System.out.println("password is: " + request.getReg().getPassword());
			if(db.register(request.getReg().getUsername(), request.getReg().getPassword()))
				//if ok, send confirmation response
				sendResponse(true, "registration successful");
			else{
				sendResponse(false, "reqistration unsuccessful");
			}
		}
		else if(type.equals(Request.ReqType.LIN)){
			System.out.println("username is: " + request.getReg().getUsername());
			System.out.println("password is: " + request.getReg().getPassword());
			if(db.logIn(request.getReg().getUsername(), request.getReg().getPassword()))
				//if ok, send confirmation response
				sendResponse(true, "Login successful");
			else{
				sendResponse(false, "Login unsuccessful");
			}

		}
		else if(type.equals(Request.ReqType.LOUT)){
			//remove the ClientID from the OnlineHashTable
				//if ok, send confirmation response
				sendResponse(true, "LogOut successful");}
			else{
				sendResponse(false, "LogOut unsuccessful");
			}
		
		 if(type.equals(Request.ReqType.CALL)){
			if (db.callCheckAvailable(request.getReg().getUsername())){
				sendResponse(true,"You can call the user");
				//get the ClientIP from the onlineHashTable, and send it as responce
				
				//make some check if p2p connection is establish then
				
				//chenge user status to in-call
				db.updateUserStatus(request.getReg().getUsername(), "4");
				}
			else{
				sendResponse(false, "Call unsuccessful");
				}
			
		 }
	}
	

	private void sendResponse(boolean ok, String message) {
		Response response = responseWriter.createActionResponse(ok, message);
		try {
			response.writeDelimitedTo(client.getOutputStream());
		} catch (IOException e) {
			System.err.println("Server: could not send response");
		}
	}
}
