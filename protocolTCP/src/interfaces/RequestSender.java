package interfaces;

public interface RequestSender {

    public void sendRegisterRequest(String username, String password);

    public void sendLogInRequest(String username, String password);

    public void sendLogOutRequest(boolean confirm);

    public void sendCallRequest(String username);
    
    public void sendStatusRequest(String username);

    public void sendEndCallRequest(boolean endCall);
}
