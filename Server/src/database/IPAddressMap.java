package database;

import java.net.Socket;
import java.util.Hashtable;

public class IPAddressMap {
    //Hashtable is syncronized (if we have concurrent access here which I doubt)
    //store usernames as keys nad IPaddresses as values

    private Hashtable<String, Client> IPTable;

    public IPAddressMap() {
        IPTable = new Hashtable<String, Client>();
    }

    public boolean isOnline(String username) {
        return IPTable.containsKey(username);
    }
    
    public Client getClient(String username) {
    	return IPTable.get(username);
    }
    
    /*
    public ClientStatus getStatus(String username){
        return IPTable.get(username).getStatus();
    }
    
    public Socket getSocket(String username){
        return IPTable.get(username).getSocket();
    }

    public String getIP(String username) {
        return IPTable.get(username).getSocket().getLocalAddress().toString();
    } */

    public void addClient(Client client) {
        IPTable.put(client.getUsername(), client);
    }

    public void removeClient(Client client) {
        IPTable.remove(client.getUsername());
    }

}
