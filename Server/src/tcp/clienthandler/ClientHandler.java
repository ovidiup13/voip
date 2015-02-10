package tcp.clienthandler;

import buffers.ClientRequest.Request;
import buffers.ServerResponse.Response;
import database.Client;
import database.ClientStatus;
import database.ConnectToDb;
import database.IPAddressMap;
import interfaces.ResponseSender;
import writers.ResponseWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable, ResponseSender {

	private static int callID = 0;
	
	private Client client;
	private Socket clientSocket;
	private ResponseWriter responseWriter;
	private InputStream input;
	private OutputStream output;
	private ConnectToDb db;
	private IPAddressMap addressMap;

	//will need to pass parameter but will conflict
	public ClientHandler(Socket clientSocket, IPAddressMap addressMap, ConnectToDb db) {
		System.out.println("Server: Client found...");
		
		this.clientSocket = clientSocket;
		this.client = new Client(clientSocket);
		this.addressMap = addressMap;
		this.db = db;
		
		//create request writer
		responseWriter = new ResponseWriter();
		
		//get input and output stream of socket
		try {
			input = clientSocket.getInputStream();
			output = clientSocket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
					case STS:  { readStatusRequest(request); break; }
                    case CALLRES: { readCallResponse(request); break; }
                    case FLIST: { readFriendListRequest(request); break; }
                    case ADDF: { readAddFriendRequest(request); break; }
                    case DELF: { readDeleteFriendRequest(request); break; }
                    case ECALL: {}//to be implemented
				}
			}
	}

    private void readRegisterRequest(Request request){
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
	}
	
	private void readLogInRequest(Request request){
		String username = request.getLin().getUsername();
		String password = request.getLin().getPassword();

		if (db.logIn(username, password)) {
			//add the user's IP the the map of active users
			client.setUsername(username);
			client.setStatus(ClientStatus.IDLE);
			addressMap.addClient(client);

			//modify user's status in db to idle
			db.updateUserStatus(username, "5");

			System.out.println("ClientIP" + addressMap.getClient(username).getHostName());

			//if ok, send confirmation response
			sendResponse(true, "Login successful");
		} else {
			sendResponse(false, "Login unsuccessful");
		}

	
	}
	
	private void readCallRequest(Request request){
		String callee = request.getUsername();
		System.out.println(callee);
		
		if (addressMap.isOnline(callee)) {
			Client calleeClient = addressMap.getClient(callee);

			if (calleeClient.getStatus() == ClientStatus.IDLE && client.getStatus() == ClientStatus.IDLE) {
				calleeClient.setClientCalled(client);
				calleeClient.setStatus(ClientStatus.WAITING);
				client.setStatus(ClientStatus.WAITING);
				client.setClientCalled(calleeClient);
				
				sendCallInquiry(calleeClient); //both clients now in deadlock until a call response is achieved or a timeout is hit
			}
			
			sendCallResponse(client, calleeClient, callID++);
			
		} else {
			sendResponse(false, "Call unsuccessful");
		}
	}

    //read status request
    private void readStatusRequest(Request request) {
        Client userData = addressMap.getClient(request.getUsername());
        //get the status of the user
        ClientStatus status = userData.getStatus();

        Response response;
        //if user is not idle - user is unavailable
        response = responseWriter.createStatusResponse(status == ClientStatus.IDLE);
        
        //send response to client
        try {
            response.writeDelimitedTo(output);
        } catch (IOException e) {
            System.err.println("cannot send status response");
        }
    }

    //read friend request
    private void readAddFriendRequest(Request request) {
        db.addFriend(client.getUsername(), request.getUsername());
        //need to know if it was successful or not !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        
        Response response = responseWriter.createActionResponse(true, "some message");

        try {
            response.writeDelimitedTo(output);
        } catch (IOException e) {
            System.err.println("could not send confirmation for friend request");
        }
    }

    //read friend list request
    private void readFriendListRequest(Request request) {
        ArrayList<String> friends = db.getFriendsFor(client.getUsername());
        
        Response response = responseWriter.createFriendListResponse(friends);

        try {
            response.writeDelimitedTo(output);
        } catch (IOException e) {
            System.err.println("Cannot send friend list");
        }
    }
        
    //read call response
    private void readCallResponse(Request request) {
    	if (client.getStatus() != ClientStatus.WAITING) return; //can't respond when we aren't waiting on another client
    	Client target = client.getClientCalled();
    	
    	//if they're waiting on us, we can accept the call
    	if (target.getStatus() == ClientStatus.WAITING && target.getClientCalled() == client) {
    		//link both clients up
    		sendCallResponse(target, client, callID);
    		sendCallResponse(client, target, callID++);
    		target.setStatus(ClientStatus.IN_CALL);
    		client.setStatus(ClientStatus.IN_CALL);
    	} else {
    		sendEndCallResponse(true);
    		//failsafe, it is instantly declined for the caller.
    	}
        request.getConfirmation();
    }

    //read delete friend request
    private void readDeleteFriendRequest(Request request) {
        //need function to delete friend relationship !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //to be implemented
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
	public void sendCallResponse(Client target, Client other, int callID){
		String IPAddress = other.getHostName();
		Response response = responseWriter.createCallResponse(IPAddress, callID);
		try {
			response.writeDelimitedTo(target.getSocket().getOutputStream());
		} catch (IOException e) {
			System.err.println("Server: could not send response.");
		}
	}

    @Override
    public void sendCallInquiry(Client target) {
    	Response response = responseWriter.createCallInquiry(client.getUsername());
		try {
			response.writeDelimitedTo(target.getSocket().getOutputStream());
		} catch (IOException e) {
			System.err.println("Server: could not send response");
		}
    }

    @Override
	public void sendEndCallResponse(boolean ok) {
		//to be implemented
	}
}
