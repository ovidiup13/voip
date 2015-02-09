package writers;

import buffers.ServerResponse.Response;
import buffers.ServerResponse.Response.CallResponse;
import buffers.ServerResponse.Response.ReqResult;

import java.util.ArrayList;

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
	 * @return response to a request
	 * */
	public Response createActionResponse(boolean ok, String cause){
		return actionResponse(ok, cause);
	}

	private static Response actionResponse(boolean ok, String cause){
		ReqResult reqResult = ReqResult.newBuilder().setOk(ok).setCause(cause).build();
		return Response.newBuilder().
                setResType(Response.ResType.ACT).setReqResult(reqResult).build();
	}

	/**
	 * method that creates a response to a call request
	 * @param ipAddress
	 * 			The IP Address of the user to be called
     * @param callID
     *          The call ID as integer
	 * @return response to the call request
	 * */
	public Response createCallResponse(String ipAddress, int callID){
		return callResponse(ipAddress, callID);
	}

	private static Response callResponse(String ipAddress, int callID){
		CallResponse callResponse = CallResponse.newBuilder().setIpAddress(ipAddress).setCallID(callID).build();
		return Response.newBuilder().setResType(Response.ResType.CALLREC).
                setCallResponse(callResponse).build();
	}

	/**
	 * method that creates a response to be sent to the other user who was left on the call
	 * @param ok
	 * 			Flags confirmation that the user should end the call
	 * @return response of end call
	 * */
	public Response createEndCallResponse(boolean ok){
		return endCallResponse(ok);
	}

	private static Response endCallResponse(boolean ok) {
		return Response.newBuilder().setResType(Response.ResType.ECALL).setEndCall(ok).build();
	}
    
    /**
     * method that creates a response that contains the friend list of a client
     * @param usernames
     *         An ArrayList of friends of the user
     * @return response containing friend list
     * */
    public Response createFriendListResponse(ArrayList<String> usernames) { return friendListRes(usernames); }
    
    private static Response friendListRes(ArrayList<String> usernames){
        return Response.newBuilder().setResType(Response.ResType.FLIST)
                .setList(Response.FriendList.newBuilder().addAllUsername(usernames))
                .build();
    }
    
    /**
     * method that creates a response/message to be sent to a client if they are willing to accept/reject a acall
     * @param username
     *          The username of the user that is calling
     * @return response of type call inquiry
     * */
    public Response createCallInquiry(String username) { return callInquiry(username); }
    
    private static Response callInquiry(String username){
        return Response.newBuilder().setResType(Response.ResType.CALLINQ)
                .setUsername(username).build();
    }
    
    /**
     * method that creates a response for a status request
     * @param available
     *          Boolean value for a client true/false as available/unavailable
     * @return a response of type status          
     * */
    public Response createStatusResponse(boolean available) { return statusResponse(available); }
    
    private static Response statusResponse(boolean ok){
        return Response.newBuilder().setResType(Response.ResType.STS)
                .setStatus(ok).build();
    }
    
}
