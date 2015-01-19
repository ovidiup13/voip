package tcp.clienthandler;

import java.io.IOException;
import java.net.Socket;
import buffers.ClientRequest.Request;
import buffers.ServerResponse.Response;
import tcp.messagehandler.ResponseWriter;

public class ClientHandler implements Runnable {

	private Socket client;
	ResponseWriter responseWriter;

	public ClientHandler(Socket client) {
		this.client = client;
		responseWriter = new ResponseWriter();
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
			//if ok, send confirmation response
			sendResponse(true, "registration successful");
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
