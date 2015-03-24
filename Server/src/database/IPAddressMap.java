package database;

import tcp.clienthandler.Client;

import java.util.Hashtable;

/*
    IP address table used for current online users. Reduces load on our database.
* */
public class IPAddressMap {
    //Hashtable is syncronized (if we have concurrent access here which I doubt)
    //store usernames as keys nad IPaddresses as values

    private Hashtable<String, Client> IPTable;

    public IPAddressMap() {
        IPTable = new Hashtable<>();
    }

    public boolean isOnline(String username) {
        return IPTable.containsKey(username);
    }

    public Client getClient(String username) {
        return IPTable.get(username);
    }

    public void addClient(Client client) {
        IPTable.put(client.getUsername(), client);
    }

    public void removeClient(Client client) {
        IPTable.remove(client.getUsername());
    }

}
