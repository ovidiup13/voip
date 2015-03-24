package tcp.sockethandler;

public interface RequestSender {

    public boolean sendRegisterRequest(String username, String password);

    public boolean sendLogInRequest(String username, String password);

    public boolean sendLogOutRequest();

    public boolean sendCallRequest(String username);
    
    //accept or reject call
    public boolean sendCallResponse(boolean ok);
    
    public boolean sendAddFriendRequest(String username);

    public boolean sendDeleteFriendRequest(String username);
    
    public boolean sendFriendListRequest();

    public boolean sendEndCallRequest();
}
