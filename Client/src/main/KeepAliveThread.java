package main;

import buffers.ClientRequest.Request;
import writers.RequestWriter;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Ovidiu on 23/02/2015.
 */
public class KeepAliveThread extends Thread{
    
    private Socket socket;
    private RequestWriter writer;
    
    public KeepAliveThread(Socket socket){
        this.socket = socket;
        writer = new RequestWriter();
    }


    @Override
    public void run() {
        int tries = 0;
        while(true){
            //wait 5 seconds
            try {
                Thread.sleep(5000);
                Request request = writer.createStatusReq("k");
                request.writeDelimitedTo(socket.getOutputStream());
            } catch (InterruptedException e) {
                break;
                //gui close everything
            } catch (IOException e) {
                tries++;
                if(tries % 2 == 0)
                    GuiMainClient.disconnect();
            }
        }
    }
}
