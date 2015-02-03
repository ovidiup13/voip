package interfaces;

public interface RequestSender {

    public boolean sendRegisterRequest(String username, String password);

    public boolean sendLogInRequest(String username, String password);

    public boolean sendLogOutRequest(boolean confirm);

    public boolean sendCallRequest(String username);
    
    public boolean sendStatusRequest(String username);

    public boolean sendEndCallRequest(boolean endCall);
}
