package database;

import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {

    private Socket socket;
    private ClientStatus status;
    private String username;
    private Client callTarget;
    
    public Client(Socket socket){
        this.socket = socket;
        this.status = ClientStatus.NOT_LOGGED_IN;
    }
    
    public void setUsername(String name) {
    	this.username = name;
    }
    
    public String getUsername() {
    	return this.username;
    }
    
    public ClientStatus getStatus(){
        return status;
    }
    
    public String getHostName() {
    	return ((InetSocketAddress)socket.getRemoteSocketAddress()).getHostName();
    }
    
    public Socket getSocket(){
        return socket;
    }
    
    public void setStatus(ClientStatus status){
        this.status = status;
    }
    
    //set client which will be called
    public void setClientCalled(Client client){
        this.callTarget = client;
    }
    
    //remove the client from call
    public void removeClientCalled(){
        this.callTarget = null;
    }
    
}
