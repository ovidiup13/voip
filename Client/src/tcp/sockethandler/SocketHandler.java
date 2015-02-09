package tcp.sockethandler;

import buffers.ClientRequest.Request;
import buffers.ServerResponse.Response;
import interfaces.RequestSender;
import p2p.SimpleVoIPCall;
import writers.RequestWriter;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

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

        //get response
        Response response = null;
        
        try {
            response = Response.parseDelimitedFrom(socketClient.getInputStream());
        } catch (IOException e) {
            System.err.println("SocketHandler: failed to open input stream");
            return false;
        }
        
        return response.getReqResult().getOk();
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

        //get response
        Response response = null;

        try {
            response = Response.parseDelimitedFrom(socketClient.getInputStream());
        } catch (IOException e) {
            System.err.println("SocketHandler: failed to open input stream");
            return false;
        }

        return response.getReqResult().getOk();
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

        //get response
        Response response = null;

        try {
            response = Response.parseDelimitedFrom(socketClient.getInputStream());
        } catch (IOException e) {
            System.err.println("SocketHandler: failed to open input stream");
            return false;
        }

        return response.getReqResult().getOk();
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

        
        /*
        * This part will be handled in the receiver thread
        * * * */
        Response response = null;

        try {
            response = Response.parseDelimitedFrom(socketClient.getInputStream());
        } catch (IOException e) {
            System.err.println("SocketHandler: failed to open input stream");
            return false;
        }

        Response.ResType type = response.getResType();
        if(type.equals(Response.ResType.ACT))
            return response.getReqResult().getOk();
        else if(type.equals(Response.ResType.CALLREC)){
            SimpleVoIPCall play = new SimpleVoIPCall();
            play.start(
                    response.getCallResponse().getIpAddress(),
                    12345,
                    response.getCallResponse().getCallID()
            );
            
            return true;
        }
        return false;
	}
    
    /*
    * handled in receiver thread
    * * * */
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
    public boolean addFriendRequest(String username) {
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
    public boolean deleteFriendRequest(String username) {
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
    public ArrayList<String> getFriendListRequest() {
        Request request = requestWriter.createFriendListRequest();
        try {
            request.writeDelimitedTo(socketClient.getOutputStream());
        } catch (IOException e) {
            System.err.println("cannot send friend list request");
            return null;
        }

        Response response = null;
        
        try {
            response = Response.parseDelimitedFrom(socketClient.getInputStream());
        } catch (IOException e) {
            System.err.println("cannot get friend list");
            return null;
        }
        
        ArrayList<String> friends = (ArrayList<String>) response.getList().getUsernameList();
        return friends;
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
        
        Response response = null;

        try {
            response = Response.parseDelimitedFrom(socketClient.getInputStream());
        } catch (IOException e) {
            System.err.println("cannot get user status");
            return false;
        }
        
        return response.getStatus();
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
