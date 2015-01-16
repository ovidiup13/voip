package tcp.messagehandler;

import buffers.ServerResponse;
import handler.check.CheckResponse;
import handler.UnknownFieldException;

/**
 * Created by Ovidiu on 16/01/2015.
 */
public class ResponseChecker implements CheckResponse {

    /**
     * method that checks action response field
     * @param response
     * @throws handler.UnknownFieldException
     * 			In the case that one of the fields is missing
     * */
    public void checkActionResponse(ServerResponse.Response response) throws UnknownFieldException {
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
     * @throws handler.UnknownFieldException
     * 			In the case that one of the fields is missing
     * */
    public void checkCallResponse(ServerResponse.Response response) throws UnknownFieldException {
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
     * @throws handler.UnknownFieldException
     * 			In the case that one of the fields is missing
     * */
    public void checkEndCallResponse(ServerResponse.Response response) throws UnknownFieldException {
        if(!response.hasEndCall())
            throw new UnknownFieldException("end call field is missing");
        if(!response.hasEndCall())
            throw new UnknownFieldException("confirmation flag is missing for endCall response");
    }
}
