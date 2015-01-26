package database;

import java.net.Socket;
import java.util.Hashtable;

public class IPAddressMap {
    //Hashtable is syncronized (if we have concurrent access here which I doubt)
    //store usernames as keys nad IPaddresses as values

    private Hashtable<String, Socket> IPTable;

    public IPAddressMap() {
        IPTable = new Hashtable<String, Socket>();
    }

    public boolean isOnline(String username) {
        return IPTable.containsKey(username);
    }

    public Socket getIP(String username) {
        return IPTable.get(username);
    }

    public void addIP(String usename, Socket IPAdress) {
        IPTable.put(usename, IPAdress);
    }

    public void removeIP(String username) {
        IPTable.remove(username);
    }

}
