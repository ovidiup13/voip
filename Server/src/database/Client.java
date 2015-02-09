package database;

import java.net.Socket;

/**
 * Created by Ovidiu on 09/02/2015.
 */
public class Client {

    private Socket socket;
    // 0 for IDLE, 1 for IN CALL, 2 for WAITING
    private int status; 
    private String username;
    //reference to client called
    private Client clientCalled;

    public Client(Socket socket, int status, String username) {
        this.socket = socket;
        this.status = status;
        this.username = username;
        this.clientCalled = null;
    }

    //get status of current user
    public int getStatus() {
        return status;
    }

    //get socket of current user
    public Socket getSocket() {
        return socket;
    }

    //set status of current user
    public void setStatus(int status) {
        this.status = status;
    }
    
    //set client which will be called
    public void setClientCalled(Client client){
        this.clientCalled = client;
    }
    
    //remove the client from call
    public void removeClientCalled(){
        this.clientCalled = null;
    }
    
}
