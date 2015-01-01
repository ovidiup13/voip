package protocol.config.handler;

import protocol.config.buffers.ClientRequest.Request;
import protocol.config.buffers.ClientRequest.Request.CallTo;
import protocol.config.buffers.ClientRequest.Request.EndCall;
import protocol.config.buffers.ClientRequest.Request.LogIn;
import protocol.config.buffers.ClientRequest.Request.LogOut;
import protocol.config.buffers.ClientRequest.Request.Registration;
import protocol.config.buffers.ClientRequest.Request.ReqType;

/**
 * Class that contains an API for creating and reading types of Request
 * 
 * @author Ovidiu Popoviciu
 * */
public final class RequestHandler {

	/**
	 * cannot instantiate this class
	 * */
	private RequestHandler() {
	}

	/**
	 * method that creates a register request
	 * 
	 * @param username
	 *            The username of client to be registered
	 * @param password
	 *            The password of client to be registered
	 * @return request of type register
	 */
	public static Request createRegisterReq(String username, String password) {
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
	public static Request createLogInReq(String username, String password) {
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
	 * @param username
	 *            The username of client to be logged out
	 * @return request of type log-out
	 */
	public static Request createLogOutReq(String username) {
		return logOutReq(username);
	}

	private static Request logOutReq(String username) {
		// build logout message
		LogOut lout = LogOut.newBuilder().setUsername(username).build();
		// build request message
		Request req = Request.newBuilder().setRqType(Request.ReqType.LOUT)
				.setLout(lout).build();
		return req;
	}

	/**
	 * method that creates a call request
	 * 
	 * @param username
	 *            The username of the client to be called
	 * @return request of type call
	 */
	public static Request createCallReq(String username) {
		return callReq(username);
	}

	private static Request callReq(String username) {
		// build call message
		CallTo call = CallTo.newBuilder().setUserCalled(username).build();
		// build request message
		Request req = Request.newBuilder().setRqType(Request.ReqType.CALL)
				.setCallTo(call).build();
		return req;
	}

	/**
	 * method that returns endcall request
	 * 
	 * @param username
	 *            The username of the client to end the connection with
	 * @return request of type end call
	 * */
	public static Request createEndCallReq(String username) {
		return endCallReq(username);
	}

	private static Request endCallReq(String username) {
		// build end call message
		EndCall ecall = EndCall.newBuilder().setUserEnded(username).build();
		// build request message
		Request req = Request.newBuilder().setRqType(Request.ReqType.ECALL)
				.setEndCall(ecall).build();
		return req;
	}
	
	/**
	 * method that checks registration field
	 * @param request
	 * 			The Request object to be checked
	 * @throws protocol.config.handler.UnknownFieldException
	 * 			In the case that one of the fields is missing
	 * */
	public static void checkReg(Request request) throws UnknownFieldException {
		if(!request.hasReg())
			throw new UnknownFieldException("registration field is missing");
		if(!request.getReg().hasUsername())
			throw new UnknownFieldException("username field is missing for registration");
		if(!request.getReg().hasPassword())
			throw new UnknownFieldException("password field is missing for registration");
		
		/*if(!request.getReg().hasHint())
			throw new UnknownFieldException("hint field is missing");*/
	}

	/**
	 * method that checks login field
	 * @param request
	 * 			The Request object to be checked
	 * @throws protocol.config.handler.UnknownFieldException
	 * 			In the case that one of the fields is missing
	 * */
	public static void checkLin(Request request) throws UnknownFieldException {
		if(!request.hasLin())
			throw new UnknownFieldException("login field is missing");
		if(!request.getLin().hasUsername())
			throw new UnknownFieldException("username field is missing for login");
		if(!request.getLin().hasPassword())
			throw new UnknownFieldException("password field is missing for login");
		if(!request.getLin().hasIpAddress())
			throw new UnknownFieldException("ip address field is missing for login");
	}

	/**
	 * method that checks logout field
	 * @param request
	 * 			The Request object to be checked
	 * @throws protocol.config.handler.UnknownFieldException
	 * 			In the case that one of the fields is missing
	 * */
	public static void checkLout(Request request) throws UnknownFieldException {
		if(!request.hasLout())
			throw new UnknownFieldException("logout field is missing");
		if(!request.getLout().hasUsername())
			throw new UnknownFieldException("username field is missing for logout");
	}

	/**
	 * method that checks callTo field
	 * @param request
	 * 			The Request object to be checked
	 * @throws protocol.config.handler.UnknownFieldException
	 * 			In the case that one of the fields is missing
	 * */
	public static void checkCall(Request request) throws UnknownFieldException {
		if(!request.hasCallTo())
			throw new UnknownFieldException("call to field is missing");
		if(!request.getCallTo().hasUserCalled())
			throw new UnknownFieldException("user called field is missing for call to");
	}

	/**
	 * method that checks endCall field
	 * @param request
	 * 			The Request object to be checked
	 * @throws protocol.config.handler.UnknownFieldException
	 * 			In the case that one of the fields is missing
	 * */
	public static void checkEndCall(Request request) throws UnknownFieldException {
		if(!request.hasEndCall())
			throw new UnknownFieldException("call end field is missing");
		if(!request.getEndCall().hasUserEnded())
			throw new UnknownFieldException("user ended field is missing for call end");
	}
	
}
