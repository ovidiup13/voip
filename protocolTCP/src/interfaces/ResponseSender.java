package interfaces;

import java.net.Socket;

public interface ResponseSender {

    public void sendResponse(boolean ok, String cause);

    public void sendCallResponse(Socket socket, Socket other, int callID);

    public void sendEndCallResponse(boolean ok);
}
