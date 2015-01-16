package handler.check;

import buffers.ServerResponse;
import handler.UnknownFieldException;

/**
 * Created by Ovidiu on 16/01/2015.
 */
public interface CheckResponse {

    public void checkActionResponse(ServerResponse.Response response) throws UnknownFieldException;

    public void checkCallResponse(ServerResponse.Response response) throws UnknownFieldException;

    public void checkEndCallResponse(ServerResponse.Response response) throws UnknownFieldException;
}
