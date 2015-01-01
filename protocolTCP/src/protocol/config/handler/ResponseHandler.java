package protocol.config.handler;

import protocol.config.buffers.ServerResponse.Response;
import protocol.config.buffers.ServerResponse.Response.ReqResult;
import protocol.config.buffers.ServerResponse.Response.CallResponse;

/**
 * Class that contains an API for creating and reading types of Responses
 *
 * @author Ovidiu Popoviciu
 * */
public class ResponseHandler {

	/*
	 * cannot instantiate this class
	 * */
	private ResponseHandler(){}

	/**
	 * method that creates a response to a request
	 * @param ok
	 * 			Flags whether the action was successful or not
	 * @param cause
	 * 			The cause of failure as string
	 * @returns response to a request
	 * */
	public static Response createActionResponse(boolean ok, String cause){
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
	public static Response createCallResponse(boolean ok, String cause, String ipAddress){
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
	public static Response createEndCallResponse(boolean ok){
		return endCallResponse(ok);
	}

	private static Response endCallResponse(boolean ok) {
		return Response.newBuilder().setResType(Response.ResType.ECALL).setEndCall(ok).build();
	}
	
	/**
	 * method that checks action response field
	 * @param response
	 * @throws protocol.config.handler.UnknownFieldException
	 * 			In the case that one of the fields is missing
	 * */
	public static void checkActionResponse(Response response) throws UnknownFieldException {
		if(!response.hasResult())
			throw new UnknownFieldException("request result field is missing");
		if(!response.getResult().hasOk())
			throw new UnknownFieldException("confirmation flag field is missing for action response");
		/*if(!response.getResult().hasCause())
			throw new UnknownFieldException("cause field is missing for action response");*/
	}

	/**
	 * method that checks action response field
	 * @param response
	 * @throws protocol.config.handler.UnknownFieldException
	 * 			In the case that one of the fields is missing
	 * */
	public static void checkCallResponse(Response response) throws UnknownFieldException {
		if(!response.hasCallResponse())
			throw new UnknownFieldException("call response field is missing");
		if(!response.getCallResponse().hasOk())
			throw new UnknownFieldException("confirmation flag field is missing for call response");
		/*if(!response.getCallResponse().hasCause())
			throw new UnknownFieldException("cause field is missing for call response");
		if(!response.getCallResponse().hasIpAddress())
			throw new UnknownFieldException("ip address field is missing for call response");*/
	}

	/**
	 * method that checks endCall response field
	 * @param response
	 * @throws protocol.config.handler.UnknownFieldException
	 * 			In the case that one of the fields is missing
	 * */
	public static void checkEndCallResponse(Response response) throws UnknownFieldException {
		if(!response.hasEndCall())
			throw new UnknownFieldException("end call field is missing");
		if(!response.hasEndCall())
			throw new UnknownFieldException("confirmation flag is missing for endCall response");
	}
}
