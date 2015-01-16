package tcp.messagehandler;

import buffers.ServerResponse.Response;
import buffers.ServerResponse.Response.ReqResult;
import buffers.ServerResponse.Response.CallResponse;

/**
 * Class that contains an API for creating and reading types of Responses
 *
 * @author Ovidiu Popoviciu
 * */
public class ResponseWriter {

	/**
	 * method that creates a response to a request
	 * @param ok
	 * 			Flags whether the action was successful or not
	 * @param cause	 * 			The cause of failure as string
	 * @returns response to a request
	 * */
	public Response createActionResponse(boolean ok, String cause){
		return actionResponse(ok, cause);
	}

	private static Response actionResponse(boolean ok, String cause){
		ReqResult reqResult = ReqResult.newBuilder().setOk(ok).setCause(cause).build();
		Response response = Response.newBuilder().
				setResType(Response.ResType.ACT).setResult(reqResult).build();
		return response;
	}

	/**
	 * method that creates a response to a call request
	 * @param ok
	 * 			Flags if the call request is successful or not
	 * @param cause
	 * 			The cause of failure as string
	 * @param ipAddress
	 * 			The IP Address of the user to be called
	 * @returns response to the call request
	 * */
	public Response createCallResponse(boolean ok, String cause, String ipAddress){
		return callResponse(ok, cause, ipAddress);
	}

	private static Response callResponse(boolean ok, String cause, String ipAddress){
		CallResponse callResponse = CallResponse.newBuilder().
				setOk(ok).setCause(cause).setIpAddress(ipAddress).build();
		Response response = Response.newBuilder().setResType(Response.ResType.CALL).
				setCallResponse(callResponse).build();
		return response;
	}

	/**
	 * method that creates a response to be sent to the other user who was left on the call
	 * @param ok
	 * 			Flags confirmation that the user should end the call
	 * @returns response of end call
	 * */
	public Response createEndCallResponse(boolean ok){
		return endCallResponse(ok);
	}

	private static Response endCallResponse(boolean ok) {
		return Response.newBuilder().setResType(Response.ResType.ECALL).setEndCall(ok).build();
	}

}
