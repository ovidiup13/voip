package tcp.clienthandler;

import buffers.ClientRequest.Request;
import buffers.ServerResponse.Response;
import database.ConnectToDb;
import database.IPAddressMap;
import writers.ResponseWriter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientHandler implements Runnable {

	private static int callID = 0;
	
	private Socket client;
	ResponseWriter responseWriter;
	private ConnectToDb db;
	private IPAddressMap ClientIPMap;

	//will need to pass parameter but will conflict
	public ClientHandler(Socket client, IPAddressMap ClientIPMap) {
		System.out.println("Server: Client found...");
		this.client = client;
		this.ClientIPMap = ClientIPMap;
		responseWriter = new ResponseWriter();
		db =  new ConnectToDb();
	}

	@Override
	public void run() {
		readRequest();
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readRequest() {
		Request request = null;
		
		while(true) {
			try {
				request = Request.parseDelimitedFrom(client.getInputStream());
			} catch (IOException e) {
				System.err.println("ClientHandler: cannot read request");
				return;
			}
			
			if (request == null) break;
			
			Request.ReqType type = request.getRqType();

			if (type.equals(Request.ReqType.REG)) {
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
			} else if (type.equals(Request.ReqType.LIN)) {
				String username = request.getLin().getUsername();
				String password = request.getLin().getPassword();

				db.makeConnection();
				if (db.logIn(username, password)) {
					//add the user's IP the the map of active users
					ClientIPMap.addIP(username, client);

					//modify user's status in db to idle
					db.updateUserStatus(username, "5");

					System.out.println("ClientIP" + ClientIPMap.getIP(username));

					//if ok, send confirmation response
					sendResponse(true, "Login successful");
				} else {
					sendResponse(false, "Login unsuccessful");
				}

				db.closeEverything();
			} else if (type.equals(Request.ReqType.LOUT)) {
				//remove the ClientID from the OnlineHashTable
				//if ok, send confirmation response
				sendResponse(true, "LogOut successful");
				break;
			} else if (type.equals(Request.ReqType.CALL)) {
				String callee = request.getUsername();
				System.out.println(callee);
				//if (db.callCheckAvailable(request.getUsername()))
				if (ClientIPMap.isOnline(callee)) {
					//(true, "You can call the user");

					//get the ClientIP from the onlineHashTable, and send it as responce
					Socket connectionToCallee = ClientIPMap.getIP(callee);

					//send response to client2
					sendCallResponse(connectionToCallee, callID);

					//send response to client1
					//sendCallResponse(client, callID);
					
					//call iD must be unique for every call

					//chenge user status to in-call

					//db.updateUserStatus(request.getReg().getUsername(), "4");
				} else {
					sendResponse(false, "Call unsuccessful");
				}
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
	
	private void sendCallResponse(Socket connection, int callID){
		String IPAddress = ((InetSocketAddress)connection.getRemoteSocketAddress()).getHostName();
		Response response = responseWriter.createCallResponse(IPAddress, callID);
		try {
			response.writeDelimitedTo(client.getOutputStream());
		} catch (IOException e) {
			System.err.println("Server: could not send response.");
		}
	}
}
