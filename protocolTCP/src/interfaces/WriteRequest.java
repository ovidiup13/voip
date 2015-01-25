package interfaces;

/**
 * Created by Ovidiu on 16/01/2015.
 */
public interface WriteRequest {

    public boolean createRegisterReq(String username, String password);

    public boolean createLogInReq(String username, String password);

    public boolean createLogOutReq(boolean confirm);

    public boolean createCallReq(String username);
    
    public boolean createStatusReq(String username);

    public boolean createEndCallReq(boolean endCall);
}
