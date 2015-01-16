package handler.write;

import buffers.ServerResponse;

/**
 * Created by Ovidiu on 16/01/2015.
 */
public interface WriteResponse {

    public ServerResponse.Response createActionResponse(boolean ok, String cause);

    public ServerResponse.Response createCallResponse(boolean ok, String cause, String ipAddress);

    public ServerResponse.Response createEndCallResponse(boolean ok);
}
