package database;

import java.net.Socket;

/**
 * Created by Ovidiu on 09/02/2015.
 */
public class Client {
    
    private Socket socket;
    private int status; // 0 for IDLE, 1 for IN CALL, 2 for WAITING
    private String username;
    
    public Client(Socket socket, int status, String username){
        this.socket = socket;
        this.status = status;
        this.username = username;
    }
    
    public int getStatus(){
        return status;
    }
    
    public Socket getSocket(){
        return socket;
    }
    
    public void setStatus(int status){
        this.status = status;
    }
}
