package modelVoip;

import java.io.Serializable;
import java.net.*;

/**
 * 
 * 
 *
 */
public class User implements Serializable 
{	
    private String _name;
    private InetAddress _address;
    private int _port;
    private static final long serialVersionUID = 42L;

    public User(String name, InetAddress address, int port)
    {
        this._name = name;
        this._address = address;
        this._port = port;
    }

    public User()
    {
        this._name = "";
        this._address = null;
        this._port = -1;
    }
    
    public String getName()
    {
        return _name;
    }
    
    public InetAddress getAddress()
    {
    	return _address;
    }
    
    public int getPort()
    {
    	return _port;
    }

    public void setName(String name)
    {
        this._name = name;
    }
    
    @Override 
    public boolean equals(Object ob)
    {
    	User user = (User) ob;
    	return user.getName().equalsIgnoreCase(this.getName());
    }
    
    @Override 
    public int hashCode()
    {
    	String test = _name.toLowerCase() + "_";
		return test.hashCode();
	}
}