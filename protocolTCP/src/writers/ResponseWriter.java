package writers;

import buffers.ServerResponse.Response;
import buffers.ServerResponse.Response.CallResponse;
import buffers.ServerResponse.Response.Result;

import java.util.ArrayList;

/**
 * Class that contains an API for creating and reading types of Responses
 *
 * @author Ovidiu Popoviciu
 * */
public class ResponseWriter {

	/**
	 * method that creates a response to a registration request
	 * @param ok
	 * 			Flags whether the registration was successful or not
	 * @param cause	 * 			The cause of failure as string
	 * @return response to a request
	 * */
	public Response createRegistrationResponse(boolean ok, String cause){
		return RegResponse(ok, cause);
	}

	private static Response RegResponse(boolean ok, String cause){
		Result reqResult = Result.newBuilder().setOk(ok).setCause(cause).build();
		return Response.newBuilder().
                setResType(Response.ResType.REG).setReqResult(reqResult).build();
	}

    /**
     * method that creates a response to a log in request
     * @param ok
     * 			Flags whether the log in was successful or not
     * @param cause	 * 			The cause of failure as string
     * @return response to a request
     * */
    public Response createLogInResponse(boolean ok, String cause){
        return LogInResponse(ok, cause);
    }

    private static Response LogInResponse(boolean ok, String cause){
        Result reqResult = Result.newBuilder().setOk(ok).setCause(cause).build();
        return Response.newBuilder().
                setResType(Response.ResType.LIN).setReqResult(reqResult).build();
    }

    /**
     * method that creates a response to a log out request
     * @param ok
     * 			Flags whether the log out was successful or not
     * @param cause	 * 			The cause of failure as string
     * @return response to a request
     * */
    public Response createLogOutResponse(boolean ok, String cause){
        return LogOutResponse(ok, cause);
    }

    private static Response LogOutResponse(boolean ok, String cause){
        Result reqResult = Result.newBuilder().setOk(ok).setCause(cause).build();
        return Response.newBuilder().
                setResType(Response.ResType.LOUT).setReqResult(reqResult).build();
    }

	/**
	 * method that creates a response to a call request
	 * @param ipAddress
	 * 			The IP Address of the user to be called
     * @param callID
     *          The call ID as integer
	 * @return response to the call request
	 * */
	public Response createCallSuccessResponse(String ipAddress, int callID){
		return callResponse(ipAddress, callID);
	}

	private static Response callResponse(String ipAddress, int callID){
		CallResponse callResponse = CallResponse.newBuilder().setIpAddress(ipAddress).setCallID(callID).build();
		return Response.newBuilder().setResType(Response.ResType.CALLREC).
                setCallResponse(callResponse).build();
	}

    /**
     * method that creates a response for a unsucessfull call
     * @param ok
     * 			Flags whether the request was successful or not
     * @param message
     *  		The cause of failure as string
     * @return response to a request
     * */
    public Response createCallUnSuccessResponse(boolean ok, String message){
        return callUnsucessResponse(ok, message);
    }

    private static Response callUnsucessResponse(boolean ok, String message){
        Result reqResult = Result.newBuilder().setOk(ok).setCause(message).build();
        return Response.newBuilder().
                setResType(Response.ResType.NOCALL).setReqResult(reqResult).build();
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

    /**
     * method that creates a response to a friend request
     * @param ok
     * 			Flags whether the request was successful or not
     * @param cause	 * 			The cause of failure as string
     * @return response to a request
     * */
    public Response createFriendRequestResponse(boolean ok, String cause){
        return friendReqResponse(ok, cause);
    }

    private static Response friendReqResponse(boolean ok, String cause){
        Result reqResult = Result.newBuilder().setOk(ok).setCause(cause).build();
        return Response.newBuilder().
                setResType(Response.ResType.ADDF).setReqResult(reqResult).build();
    }

    /**
     * method that creates a response to a delete friend request
     * @param ok
     * 			Flags whether the request was successful or not
     * @param cause	 * 			The cause of failure as string
     * @return response to a request
     * */
    public Response createDeleteFriendResponse(boolean ok, String cause){
        return friendDelResponse(ok, cause);
    }

    private static Response friendDelResponse(boolean ok, String cause){
        Result reqResult = Result.newBuilder().setOk(ok).setCause(cause).build();
        return Response.newBuilder().
                setResType(Response.ResType.DELF).setReqResult(reqResult).build();
    }
    
}
