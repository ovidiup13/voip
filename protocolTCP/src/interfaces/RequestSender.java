package interfaces;

public interface RequestSender {

    public boolean sendRegisterRequest(String username, String password);

    public boolean sendLogInRequest(String username, String password);

    public boolean sendLogOutRequest();

    public boolean sendCallRequest(String username);
    
    //accept or reject call
    public boolean sendCallResponse(boolean ok);
    
    public boolean addFriendRequest(String username);
    
    public boolean deleteFriendRequest(String username);
    
    public boolean getFriendListRequest();
    
    public boolean sendStatusRequest(String username);

    public boolean sendEndCallRequest();
}
