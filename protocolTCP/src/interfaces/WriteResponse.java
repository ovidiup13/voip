package interfaces;

/**
 * Created by Ovidiu on 16/01/2015.
 */
public interface WriteResponse {

    public boolean createActionResponse(boolean ok, String cause);

    public boolean createCallResponse(boolean ok, String cause, String ipAddress, int callID);

    public boolean createEndCallResponse(boolean ok);
}
