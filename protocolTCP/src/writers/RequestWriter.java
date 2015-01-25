package writers;

import buffers.ClientRequest.Request;
import buffers.ClientRequest.Request.LogIn;
import buffers.ClientRequest.Request.Registration;

/**
 * Class that contains an API for creating and reading types of Request
 * 
 * @author Ovidiu Popoviciu
 * */
public final class RequestWriter {

	/**
	 * method that creates a register request
	 * 
	 * @param username
	 *            The username of client to be registered
	 * @param password
	 *            The password of client to be registered
	 * @return request of type register
	 */
	public Request createRegisterReq(String username, String password) {
		return registerReq(username, password);
	}

	private static Request registerReq(String username, String password) {
		// build registration message
		Registration reg = Registration.newBuilder().setUsername(username)
				.setPassword(password).build();
		// build request message
		Request req = Request.newBuilder().setRqType(Request.ReqType.REG)
				.setReg(reg).build();
		return req;
	}

	/**
	 * method that creates a login request
	 * 
	 * @param username
	 *            The username of client to be logged in
	 * @param password
	 *            The password of client to be logged in
	 * @return request of type log-in
	 */
	public Request createLogInReq(String username, String password) {
		return logInReq(username, password);
	}

	private static Request logInReq(String username, String password) {
		// build login message
		LogIn lin = LogIn.newBuilder().setUsername(username)
				.setPassword(password).build();
		// build request message
		Request req = Request.newBuilder().setRqType(Request.ReqType.LIN)
				.setLin(lin).build();
		return req;
	}

	/**
	 * method that creates a logout request
	 * 
	 * @param confirm
	 *            The username of client to be logged out
	 * @return request of type log-out
	 */
	public Request createLogOutReq(boolean confirm) { return logOutReq(confirm); }

	private static Request logOutReq(boolean confirm) {
		// build request message
		Request req = Request.newBuilder().setRqType(Request.ReqType.LOUT)
				.setConfirmation(confirm).build();
		return req;
	}

	/**
	 * method that creates a call request
	 * 
	 * @param username
	 *            The username of the client to be called
	 * @return request of type call
	 */
	public Request createCallReq(String username) {
		return callReq(username);
	}

	private static Request callReq(String username) {
		// build request message
		Request req = Request.newBuilder().setRqType(Request.ReqType.CALL)
				.setUsername(username).build();
		return req;
	}

	/**
	 * method that returns endcall request
	 *
	 * @param username
	 *            The username of the client to end the connection with
	 * @return request of type end call
	 * */
	public Request createStatusReq(String username) { return statusReq(username); }
	
	private static Request statusReq(String username){
		//build request
		Request req = Request.newBuilder().setRqType(Request.ReqType.STS)
				.setUsername(username).build();
		return req;
	}
	
	/**
	 * method that returns endcall request
	 * 
	 * @param confirm
	 *            The username of the client to end the connection with
	 * @return request of type end call
	 * */
	public Request createEndCallReq(boolean confirm) {
		return endCallReq(confirm);
	}

	private static Request endCallReq(boolean confirm) {
		// build request message
		Request req = Request.newBuilder().setRqType(Request.ReqType.ECALL)
				.setConfirmation(confirm).build();
		return req;
	}

}
