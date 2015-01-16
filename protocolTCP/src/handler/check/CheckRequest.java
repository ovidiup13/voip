package handler.check;

import buffers.ClientRequest;
import handler.UnknownFieldException;

/**
 * Created by Ovidiu on 16/01/2015.
 */
public interface CheckRequest {

    public void checkReg(ClientRequest.Request request) throws UnknownFieldException;

    public void checkLin(ClientRequest.Request request) throws UnknownFieldException;

    public void checkLout(ClientRequest.Request request) throws UnknownFieldException;

    public void checkCall(ClientRequest.Request request) throws UnknownFieldException;

    public void checkEndCall(ClientRequest.Request request) throws UnknownFieldException;
}
