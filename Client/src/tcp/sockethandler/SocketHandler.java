package tcp.sockethandler;

import buffers.ClientRequest.Request;
import interfaces.RequestSender;
import writers.RequestWriter;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Ovidiu Popoviciu
 * */

public class SocketHandler implements RequestSender {

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
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to open socket");
            return false;
		}
		return true;
	}
	
	@Override
	public boolean sendRegisterRequest(String username, String password) {
		Request request = requestWriter.createRegisterReq(username, password);
		try {
			request.writeDelimitedTo(socketClient.getOutputStream());
			
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to send register request");
            return false;
		}
        return true;
	}
	
	@Override
	public boolean sendLogInRequest(String username, String password) {
		Request request = requestWriter.createLogInReq(username, password);
		try {
			request.writeDelimitedTo(socketClient.getOutputStream());
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to send login request");
            return false;
		}
        return true;
	}

    @Override
	public boolean sendLogOutRequest() {
		Request request = requestWriter.createLogOutReq();
		try {
			request.writeDelimitedTo(socketClient.getOutputStream());
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to send logout request");
            return false;
		}
        
        return true;
	}
	
	@Override
	public boolean sendCallRequest(String username){
		Request request = requestWriter.createCallReq(username);
		try {
			request.writeDelimitedTo(socketClient.getOutputStream());
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to send call request");
            return false;
		}
        return true;
	}

    @Override
    public boolean sendCallResponse(boolean ok) {
        Request request = requestWriter.createCallResponse(ok);
        try {
            request.writeDelimitedTo(socketClient.getOutputStream());
        } catch (IOException e) {
            System.err.println("cannot send call response");
            return false;
        }
        return true;
    }

    //add friend request sender
    @Override
    public boolean sendAddFriendRequest(String username) {
        Request request = requestWriter.createFriendRequest(username);
        try {
            request.writeDelimitedTo(socketClient.getOutputStream());
        } catch (IOException e) {
            System.err.println("cannot send friend request");
            return false;
        }
        return true;
    }

    //delete friend request sender
    @Override
    public boolean sendDeleteFriendRequest(String username) {
        Request request = requestWriter.deleteFriendRequest(username);
        try {
            request.writeDelimitedTo(socketClient.getOutputStream());
        } catch (IOException e) {
            System.err.println("cannot send delete friend request");
            return false;
        }
        return true;
    }

    //get friend list request sender
    @Override
    public boolean sendFriendListRequest() {
        Request request = requestWriter.createFriendListRequest();
        try {
            request.writeDelimitedTo(socketClient.getOutputStream());
        } catch (IOException e) {
            System.err.println("cannot send friend list request");
            return false;
        }
        return true;
    }

    //get status request sender and receiver
    @Override
	public boolean sendStatusRequest(String username) {
		Request request = requestWriter.createStatusReq(username);
        try {
            request.writeDelimitedTo(socketClient.getOutputStream());
        } catch (IOException e) {
            System.err.println("cannot send status request");
            return false;
        }
        return true;
    }

    //send end call request
    @Override
    public boolean sendEndCallRequest() {
        Request request = requestWriter.createEndCallReq();
        try {
            request.writeDelimitedTo(socketClient.getOutputStream());
        } catch (IOException e) {
            System.err.println("cannot send end call request");
            return false;
        }
        return true;
    }
    
    //close connection to the server
	public boolean closeConnection() {
		Request request = requestWriter.createLogOutReq();
		try {
			//tell the server we log out
			request.writeDelimitedTo(socketClient.getOutputStream());
			
			//close the connection
			socketClient.close();
			return true;
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to close socket");
		}
		return false;
	}
}
