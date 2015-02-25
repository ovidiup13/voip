package tcp.clienthandler;

import buffers.ClientRequest.Request;
import buffers.ServerResponse.Response;
import database.*;
import database.ConnectToDb.TypeAction;
import interfaces.ResponseSender;
import writers.ResponseWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
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

        try {
            clientSocket.setKeepAlive(true);
            clientSocket.setSoTimeout(10000);
        } catch (SocketException e) {
            System.err.println("Connection with client timed out. Client will be logged out.");
        }

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
        readRequest(); //loops until client wants to close the socket or a fatal error occurs
        synchronized (client) { //require exclusive access to our own object so that other threads don't modify the state or attempt to access a half removed client.
            //remove client first to avoid issues with half-ended clients.
            //TODO: may be a synchronisation problem here.
            if (client.getStatus() != ClientStatus.NOT_LOGGED_IN)
                addressMap.removeClient(client); //if they're logged in, remove their client from the hashtable as it is now invalid
            client.setStatus(ClientStatus.NOT_LOGGED_IN); //no other clients should see this, as they need to access clients from the addressMap, but it's here just in case
            
            sendEndCallResponse(); //if we are in a call with another user, end it
        }
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

        while (true) {
            try {
                request = Request.parseDelimitedFrom(input);
            } catch (IOException e) {
                System.err.println("ClientHandler: cannot read request");
                return;
            }

            if (request == null) break;
            Request.ReqType type = request.getRqType();

            synchronized (client) { //we will need exclusive access to our client object to do any of these.
                if (type.equals(Request.ReqType.LOUT)) {

                    //if ok, send confirmation response
                    sendLogOutResponse(true, "LogOut successful");
                    return;
                } else {
                    switch (type) {
                        case REG: {
                            readRegisterRequest(request);
                            break;
                        }
                        case LIN: {
                            readLogInRequest(request);
                            break;
                        }
                        case CALL: {
                            readCallRequest(request);
                            break;
                        }
                        case STS: {
                            readStatusRequest(request);
                            break;
                        }
                        case CALLRES: {
                            readCallResponse(request);
                            break;
                        }
                        case FLIST: {
                            readFriendListRequest(client);
                            break;
                        }
                        case ADDF: {
                            readAddFriendRequest(request);
                            break;
                        }
                        case DELF: {
                            readDeleteFriendRequest(request);
                            break;
                        }
                        case ECALL: {
                            sendEndCallResponse();
                            break;
                        }
                    }
                }
            }
        }
    }

    private void readRegisterRequest(Request request) {
        System.out.println("Server: Request is of type REG, processing...");
        System.out.println("Server: Adding user to database...");
        if (db.register(request.getReg().getUsername(), request.getReg().getPassword())) {
            //if ok, send confirmation response
            sendRegisterResponse(true, "registration successful - you can log in now");
            System.out.println("Server: Sent successful response.");
        } else {
            sendRegisterResponse(false, "registration unsuccessful - the user already exists");
            System.out.println("Server: Sent unsuccessful response.");
        }
    }

    private void readLogInRequest(Request request) {
        if (client.getStatus() != ClientStatus.NOT_LOGGED_IN) {
            sendLogInResponse(false, "Login unsuccessful, you are already logged in...");
            return;
        }
        String username = request.getLin().getUsername();
        String password = request.getLin().getPassword();

        if (db.logIn(username, password)) {

            if (addressMap.isOnline(username)) {
                //can't log in as user, they are already online.
                sendLogInResponse(false, "Login unsuccessful, user already online");
                return;
            } //this check may have to be atomic with adding the client.

            //add the user's IP the the map of active users
            client.setUsername(username);
            client.setStatus(ClientStatus.IDLE);
            addressMap.addClient(client); //do this at the end, so that
            
            
            //notify all user friends for status change
            sendFriendListStatusChange(client);

            System.out.println("ClientIP" + addressMap.getClient(username).getHostName());

            //if ok, send confirmation response
            sendLogInResponse(true, "Login successful");
        } else {
            sendLogInResponse(false, "Login unsuccessful, incorrect credentials");
        }
    }

    private void readCallRequest(Request request) {
        String callee = request.getUsername();
        System.out.println(callee);

        if (addressMap.isOnline(callee)) {
            Client calleeClient = addressMap.getClient(callee);

            synchronized (calleeClient) {
                if (calleeClient.getStatus() == ClientStatus.IDLE && client.getStatus() == ClientStatus.IDLE) {
                    calleeClient.setClientCalled(client);
                    calleeClient.setStatus(ClientStatus.WAITING);
                    
                    client.setStatus(ClientStatus.WAITING);
                    client.setClientCalled(calleeClient);
                    
                    //notify all user friends for status change
                    sendFriendListStatusChange(client);
                    sendFriendListStatusChange(calleeClient);


                    sendCallInquiry(calleeClient); //both clients now waiting until a call response is recieved or a timeout is hit
                    //TODO: the actual timeout
                    //yeah
                }
            }

        } else {
            sendUnsuccessfulCall(false, "Call unsuccessful");
        }
    }

    //read status request
    private void readStatusRequest(Request request) {
        
        Response response = Response.newBuilder().setResType(Response.ResType.STS).build();
        
        //send response to client
        try {
            response.writeDelimitedTo(output);
        } catch (IOException e) {
            System.err.println("cannot send status response");
        }
    }

    //read friend request
    private void readAddFriendRequest(Request request) {
        ResultPair result = db.addFriend(client.getUsername(), request.getUsername());
        if (result.getSuccessful()) {
            sendAddFriendResponse(true, "Adding friend successful");
            System.out.println("The type of the add friend is "+ result.getType());
            //send friend list back
            readFriendListRequest(client);

            if(addressMap.isOnline(request.getUsername())){
                readFriendListRequest(addressMap.getClient(request.getUsername()));
                if(result.getType()== TypeAction.RESPONSE_FR){
                	sendFriendRequestResponse(addressMap.getClient(request.getUsername()),client.getUsername(), true);
                }
             }
            
            System.out.println("Server: Sent successful response.");
        } else {
            sendAddFriendResponse(false, "Adding friend unsuccessful- Contact staff for support");
        }
    }

    //read friend list request
    private void readFriendListRequest(Client client) {
        //FORMAT: USER, STATUS, LASTLogin
        ArrayList<String> relationships = db.getRelationshipsFor(client.getUsername());
        
        //add online status
        for(int i = 0; i < relationships.size() - 2; i+= 3) {
            //add status on the 4th element
            String friend = relationships.get(i);
            int status;
            if(addressMap.isOnline(friend))
                status = addressMap.getClient(relationships.get(i)).getStatus().getNumVal();
            else status = -1;
            
            System.out.println(relationships.get(i) + " " + status);
            
            relationships.add(i+3, Integer.toString(status));
            i++;
        }

        Response response = responseWriter.createFriendListResponse(relationships);

        try {
            response.writeDelimitedTo(client.getSocket().getOutputStream());
        } catch (IOException e) {
            System.err.println("Cannot send friend list");
        }
    }
    
    
    private void sendFriendListStatusChange(Client client) {
      
        ArrayList<String> relationships = db.getRelationshipsFor(client.getUsername());
        
        
      //look through the array
        for(int i = 0; i < relationships.size()-2; i+= 3){
        	System.out.println(relationships.get(i));
        	String username = relationships.get(i);
        	if (addressMap.getClient(username) != null)
        		readFriendListRequest(addressMap.getClient(username));
        }
        }

    //read call response
    private void readCallResponse(Request request) {
        if (client.getStatus() != ClientStatus.WAITING) return; //can't respond when we aren't waiting on another client
        Client target = client.getClientCalled();

        //if they're waiting on us, we can accept the call
        synchronized (target) { //need to deal with other client objects atomically
            if (request.getConfirmation() && target.getStatus() == ClientStatus.WAITING && target.getClientCalled() == client) {
                //link both clients up
                sendCallResponse(target, client, callID);
                sendCallResponse(client, target, callID++);
                target.setStatus(ClientStatus.IN_CALL);
                client.setStatus(ClientStatus.IN_CALL);
                
                //notify all user friends for status change
                sendFriendListStatusChange(client);
                sendFriendListStatusChange(target);
                
            } else {
                sendEndCallResponse();
                //failsafe, it is instantly declined for the caller.
            }
        }
    }

    //read delete friend request
    private void readDeleteFriendRequest(Request request) {
    	ResultPair result = db.deleteFriendship(request.getUsername(),client.getUsername() );
        if (result.getSuccessful()) {
            sendDelFriendResponse(true, "Deleting friend successful");
            //update both client lists

            readFriendListRequest(client); //send friends list back as well
            if (addressMap.isOnline(request.getUsername())){
            	System.out.println("RESULT IS "+ result.getType());
            	readFriendListRequest(addressMap.getClient(request.getUsername()));
            	if(result.getType()== TypeAction.PENDING_DEL){
            		System.out.println("WE AREEEE HEREEE");
            		sendFriendRequestResponse(addressMap.getClient(request.getUsername()),client.getUsername(), false);
            		}
            }
            
            System.out.println("Server: Sent successful response.");
        } else {
            sendDelFriendResponse(false, "Deleting friend  unsuccessful - Contact staff for support");
            System.out.println("Server: Sent unsuccessful response.");
        }
    }


    @Override
    public void sendRegisterResponse(boolean ok, String message) {
        Response response = responseWriter.createRegistrationResponse(ok, message);
        try {
            response.writeDelimitedTo(output);
        } catch (IOException e) {
            System.err.println("Server: could not send response");
        }
    }

    @Override
    public void sendLogInResponse(boolean ok, String message) {
        Response response = responseWriter.createLogInResponse(ok, message);
        try {
            response.writeDelimitedTo(output);
        } catch (IOException e) {
            System.err.println("Server: could not send log in response");
        }
    }

    @Override
    public void sendLogOutResponse(boolean ok, String message) {
        Response response = responseWriter.createLogOutResponse(ok, message);
        try {
        	
            client.setStatus(ClientStatus.NOT_LOGGED_IN);
            //notify all user friends for status change
            sendFriendListStatusChange(client);
            
            addressMap.removeClient(client);

            response.writeDelimitedTo(output);
        } catch (IOException e) {
            System.err.println("Server: could not send log out response");
        }
    }

    @Override
    public void sendCallResponse(Client target, Client other, int callID) {
        String IPAddress = other.getHostName();
        Response response = responseWriter.createCallSuccessResponse(IPAddress, callID);
        try {
            response.writeDelimitedTo(target.getSocket().getOutputStream());
        } catch (IOException e) {
            System.err.println("Server: could not send response.");
        }
    }

    @Override
    public void sendUnsuccessfulCall(boolean ok, String message) {
        Response response = responseWriter.createCallUnSuccessResponse(ok, message);
        try {
            response.writeDelimitedTo(output);
        } catch (IOException e) {
            System.err.println("Server: could not send call unsuccessful response");
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
    public void sendEndCallResponse() {
        if (client.getStatus().getNumVal() < ClientStatus.IN_CALL.getNumVal())
            return; //not in a call or waiting on one, ignore
        Client clientCalled = client.getClientCalled();
        synchronized (clientCalled) { //need to deal with other client objects atomically
            if (clientCalled == null) return; //client called must not be null
            if (clientCalled.getStatus().getNumVal() < ClientStatus.IN_CALL.getNumVal())
                return; //other client must be in call
            if (clientCalled.getClientCalled() != client) return; //other client must be in a call with this client.
            //set the status of current client to idle
            client.setStatus(ClientStatus.IDLE);
            client.setClientCalled(null);
            


            clientCalled.setStatus(ClientStatus.IDLE);
            clientCalled.setClientCalled(null);
            
            //notify all user friends for status change
            sendFriendListStatusChange(client);
            sendFriendListStatusChange(clientCalled);

        }

        //send an endcall message to other client and set his status to idle
        Response response = responseWriter.createEndCallResponse(true);

        try {
            response.writeDelimitedTo(clientCalled.getSocket().getOutputStream());
        } catch (IOException e) {
            System.err.println("Could not send end call");
        }
    }

    @Override
    public void sendAddFriendResponse(boolean ok, String message) {
        Response response = responseWriter.createFriendRequestResponse(ok, message);
        try {
            response.writeDelimitedTo(output);
        } catch (IOException e) {
            System.err.println("Server: could not send add friend response");
        }
    }

    @Override
    public void sendDelFriendResponse(boolean ok, String message) {
        Response response = responseWriter.createDeleteFriendResponse(ok, message);
        try {
            response.writeDelimitedTo(output);
        } catch (IOException e) {
            System.err.println("Server: could not send delete friend response");
        }
    }

    @Override
    public void sendFriendRequestResponse(Client target, String username, boolean ok) {
        Response response = responseWriter.createFriendResponse(username, ok);
        try {
            response.writeDelimitedTo(target.getSocket().getOutputStream());
        } catch (IOException e) {
            System.err.println("Server: could send response for friend request");
        }
    }
}
