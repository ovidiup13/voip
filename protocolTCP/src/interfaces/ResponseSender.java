package interfaces;

import database.Client;

import java.util.ArrayList;

public interface ResponseSender {

    public void sendResponse(boolean ok, String cause);

    public void sendCallResponse(Client target, Client other, int callID);
    
    public void sendFriendListResponse(ArrayList<String> list);
    
    public void sendCallInquiry(String username);

    public void sendEndCallResponse(boolean ok);
}
