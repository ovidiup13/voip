package tcp.messagehandler;

import buffers.ClientRequest;
import handler.check.CheckRequest;
import handler.UnknownFieldException;

/**
 * Created by Ovidiu on 16/01/2015.
 */
public class RequestChecker implements CheckRequest {

    /**
     * method that checks registration field
     * @param request
     * 			The Request object to be checked
     * @throws handler.UnknownFieldException
     * 			In the case that one of the fields is missing
     * */
    public void checkReg(ClientRequest.Request request) throws UnknownFieldException {
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
     * @throws handler.UnknownFieldException
     * 			In the case that one of the fields is missing
     * */
    public void checkLin(ClientRequest.Request request) throws UnknownFieldException {
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
     * @throws handler.UnknownFieldException
     * 			In the case that one of the fields is missing
     * */
    public void checkLout(ClientRequest.Request request) throws UnknownFieldException {
        if(!request.hasLout())
            throw new UnknownFieldException("logout field is missing");
        if(!request.getLout().hasUsername())
            throw new UnknownFieldException("username field is missing for logout");
    }

    /**
     * method that checks callTo field
     * @param request
     * 			The Request object to be checked
     * @throws handler.UnknownFieldException
     * 			In the case that one of the fields is missing
     * */
    public void checkCall(ClientRequest.Request request) throws UnknownFieldException {
        if(!request.hasCallTo())
            throw new UnknownFieldException("call to field is missing");
        if(!request.getCallTo().hasUserCalled())
            throw new UnknownFieldException("user called field is missing for call to");
    }

    /**
     * method that checks endCall field
     * @param request
     * 			The Request object to be checked
     * @throws handler.UnknownFieldException
     * 			In the case that one of the fields is missing
     * */
    public void checkEndCall(ClientRequest.Request request) throws UnknownFieldException {
        if(!request.hasEndCall())
            throw new UnknownFieldException("call end field is missing");
        if(!request.getEndCall().hasUserEnded())
            throw new UnknownFieldException("user ended field is missing for call end");
    }

}
