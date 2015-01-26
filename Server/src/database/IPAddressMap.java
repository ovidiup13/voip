package database;

import java.util.Hashtable;

public class IPAddressMap {
	//Hashtable is syncronized (if we have concurrent access here which I doubt)
	//store usernames as keys nad IPaddresses as values
	
	private Hashtable<String, String> IPTable = new Hashtable<String,String>();

	
	public String  getIP(String username) {
		return IPTable.get(username);
	}

	public void addIP(String usename, String IPAdress) {
		IPTable.put(usename, IPAdress);
	}
	
	public void removeIP(String username){
		IPTable.remove(username);
	}
	

}
