package interfaces;

import java.net.Socket;

import database.Client;

public interface ResponseSender {

    public void sendResponse(boolean ok, String cause);

    public void sendCallResponse(Client target, Client other, int callID);

    public void sendEndCallResponse(boolean ok);
}
