package tcp.clienthandler;

public interface ResponseSender {

    void sendRegisterResponse(boolean ok, String message);

    void sendLogInResponse(boolean ok, String message);

    void sendLogOutResponse(boolean ok, String message);

    void sendCallResponse(Client target, Client other, int callID);

    void sendUnsuccessfulCall(boolean ok, String message, Client client);

    void sendCallInquiry(Client target);

    void sendEndCallResponse();

    void sendAddFriendResponse(boolean ok, String message);

    void sendDelFriendResponse(boolean ok, String message);

    void sendFriendRequestResponse(Client target, String username, boolean ok);


}
