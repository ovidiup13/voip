package handler.write;

import buffers.ClientRequest;

/**
 * Created by Ovidiu on 16/01/2015.
 */
public interface WriteRequest {

    public ClientRequest.Request createRegisterReq(String username, String password);

    public ClientRequest.Request createLogInReq(String username, String password);

    public ClientRequest.Request createLogOutReq(String username);

    public ClientRequest.Request createCallReq(String username);

    public ClientRequest.Request createEndCallReq(String username);
}
