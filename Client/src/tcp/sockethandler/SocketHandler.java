package tcp.sockethandler;

import buffers.ClientRequest.Request;
import buffers.ServerResponse.Response;
import interfaces.RequestSender;
import p2p.SimpleVoIPCall;
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
			return true;
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to open socket");
		}
		return false;
	}
	
	@Override
	public boolean sendRegisterRequest(String username, String password) {
		Request request = requestWriter.createRegisterReq(username, password);
		try {
			request.writeDelimitedTo(socketClient.getOutputStream());
			
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to send register request");
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
	public boolean sendLogOutRequest(boolean confirm) {
		Request request = requestWriter.createLogOutReq(confirm);
		try {
			request.writeDelimitedTo(socketClient.getOutputStream());
		} catch (IOException e) {
			System.err.println("SocketHandler: failed to send logout request");
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
		}

        Response response = null;

        try {
            response = Response.parseDelimitedFrom(socketClient.getInputStream());
        } catch (IOException e) {
            System.err.println("SocketHandler: failed to open input stream");
        }

        Response.ResType type = response.getResType();
        if(type.equals(Response.ResType.ACT))
            return response.getReqResult().getOk();
        else if(type.equals(Response.ResType.CALL)){
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

	@Override
	public boolean sendStatusRequest(String username) {
		//to be implemented
        return false;
	}

	@Override
	public boolean sendEndCallRequest(boolean endCall) {
		//to be implemented
        return false;
	}
    

	public boolean closeConnection() {
		Request request = requestWriter.createLogOutReq(true);
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
