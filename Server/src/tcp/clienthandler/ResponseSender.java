package tcp.clienthandler;

import database.Client;

public interface ResponseSender {

    public void sendRegisterResponse(boolean ok, String message);
    
    public void sendLogInResponse(boolean ok, String message);
    
    public void sendLogOutResponse(boolean ok, String message);

    public void sendCallResponse(Client target, Client other, int callID);

    public void sendUnsuccessfulCall(boolean ok, String message, Client client);

    public void sendCallInquiry(Client target);

    public void sendEndCallResponse();
    
    public void sendAddFriendResponse(boolean ok, String message);
    
    public void sendDelFriendResponse(boolean ok, String message);
    
    public void sendFriendRequestResponse(Client target,String username, boolean ok);

    
}
