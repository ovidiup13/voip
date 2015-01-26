import java.io.*;
import java.net.*;
import java.util.Map;

/**
 * 
 */
public class ServerThread extends Thread
{
	private Socket socket = null;
	private static byte[] sendbuf = null;
	private static byte[] recbuf = null;
    private static User cur_user = new User();
	
    public ServerThread(Socket socket)
    {
    	super();
    	this.socket = socket;
    }
    
	public void run()
	{
        try
        {
        	int state;
        	System.out.println("Remote Socket Address " + socket.getRemoteSocketAddress());
        	
        	sendbuf = new byte[socket.getSendBufferSize()];
        	recbuf = new byte[socket.getReceiveBufferSize()];
        	
        	state = socket.getInputStream().read();
    		if (state == Message.USER)
    		{
    			Server.prevLen = Server.users.size();
	        	socket.getInputStream().read(recbuf);
				cur_user = (User) toObject(recbuf);
				Server.users.add(cur_user);
				Server.Maptest.put(cur_user.getName(),socket);
				Server.usernames.add(cur_user.getName());
				Server.curLen = Server.users.size();
    		}
			
        	while(true)
        	{
        		
        		PrintUsers();
        		state = socket.getInputStream().read();
        		if (state == Message.HASHSET)
        		{
        			for(Map.Entry<String, Socket> entry: Server.Maptest.entrySet())
    				{
        				sendbuf = toByteArray(Message.HASHSET);
    					entry.getValue().getOutputStream().write(sendbuf);
    					entry.getValue().getOutputStream().flush();
        				
    					sendbuf = toByteArray(Server.usernames);
    					entry.getValue().getOutputStream().write(sendbuf);
    					entry.getValue().getOutputStream().flush();
    				}
        		}
        		else if (state == Message.CALL)
        		{
        			socket.getInputStream().read(recbuf);
        			Message temp = (Message) toObject(recbuf);
        			Socket rec = null;
        			if (!Server.usernames.contains(temp.getRecipient()))
        			{
        				Message noUser = new Message("", temp.getRecipient(), "");
        				sendbuf = toByteArray(noUser);
        				rec = Server.Maptest.get(temp.getOrigin());
        				
        				rec.getOutputStream().write(Message.NONEXISTANT);
        				rec.getOutputStream().flush();
        				
            			rec.getOutputStream().write(sendbuf);
        				rec.getOutputStream().flush();
        				
        			}
        			else
        			{
        				sendbuf = toByteArray(temp);
        				rec = Server.Maptest.get(temp.getRecipient());
        				
        				rec.getOutputStream().write(Message.REQUEST);
        				rec.getOutputStream().flush();
        				
        				rec.getOutputStream().write(sendbuf);
        				rec.getOutputStream().flush();
        			}
        		}
        		else if (state == Message.DECLINE)
        		{
        			socket.getInputStream().read(recbuf);
        			Message temp = (Message) toObject(recbuf);
        			Socket rec = Server.Maptest.get(temp.getOrigin());
        			sendbuf = toByteArray(temp);
        			
        			rec.getOutputStream().write(Message.DECLINE);
    				rec.getOutputStream().flush();
        			
        			rec.getOutputStream().write(sendbuf);
    				rec.getOutputStream().flush();
        		}
        		else if (state == Message.ACCEPT)
        		{
        			socket.getInputStream().read(recbuf);
        			String temp = (String) toObject(recbuf);
        			
        			System.out.println(temp);
        			String[] data = temp.split(":");
        			String name = data[2];
        			
        			String address = data[0] + ":" + data[1];
        			
        			Socket rec = Server.Maptest.get(name);
        			sendbuf = toByteArray(address);
        			
        			rec.getOutputStream().write(Message.ACCEPT);
    				rec.getOutputStream().flush();
        			
        			rec.getOutputStream().write(sendbuf);
    				rec.getOutputStream().flush();
        		}
        		else if (state == Message.BYE)
        		{
        			socket.getInputStream().read(recbuf);
        			Message Temp = (Message) toObject(recbuf);
        			
        			Message send = new Message("server", "", Temp.getOrigin() + " disconnected...\n");
        			for(Map.Entry<String, Socket> entry: Server.Maptest.entrySet())
    				{
        				
        				if (!entry.getKey().equals(Temp.getOrigin()))
        				{
        					sendbuf = toByteArray(send);
	    					
	    					entry.getValue().getOutputStream().write(Message.DC);
	    					entry.getValue().getOutputStream().flush();
	    					
	    					entry.getValue().getOutputStream().write(sendbuf);
	    					entry.getValue().getOutputStream().flush();
        				}
    				}
        			
        			sendbuf = toByteArray(Temp);
        			Socket rec = Server.Maptest.get(Temp.getOrigin());
        			
        			rec.getOutputStream().write(Message.REMOVED);
    				rec.getOutputStream().flush();   
        			
        			rec.getOutputStream().write(sendbuf);
    				rec.getOutputStream().flush();      			
        			
        			User dc = new User(Temp.getOrigin(), null, 0);
        			Server.prevLen = Server.usernames.size();
            		Server.Maptest.remove(Temp.getOrigin());
            		Server.users.remove(dc);
            		Server.usernames.remove(Temp.getOrigin());
            		Server.curLen = Server.usernames.size();
            		PrintUsers();
            		break;
        		}
        	}
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } 
        catch (ClassNotFoundException e)
        {
			e.printStackTrace();
		}
	}
	
	public static void PrintUsers()
	{		
		if (Server.curLen == 0)
		{
			Server._serverLog.append("Users: \n");
			Server._serverLog.append(" None\n");
			
			try
			{
				for(Map.Entry<String, Socket> entry: Server.Maptest.entrySet())
				{
					Object[] onlineUsers = Server.usernames.toArray();
					sendbuf = toByteArray(onlineUsers);
	
					entry.getValue().getOutputStream().write(Message.HASHSET);
					entry.getValue().getOutputStream().flush();
	
					entry.getValue().getOutputStream().write(sendbuf);
					entry.getValue().getOutputStream().flush();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if (Server.prevLen != Server.curLen)
		{
			Server._serverLog.append("Users: \n");
			for (int i = 0; i < Server.usernames.size(); i++)
			{
				Server._serverLog.append(" " + Server.usernames.get(i) + "\n"); 
			}
			
			try
			{
				for(Map.Entry<String, Socket> entry: Server.Maptest.entrySet())
				{
					Object[] onlineUsers = Server.usernames.toArray();
					sendbuf = toByteArray(onlineUsers);
	
					entry.getValue().getOutputStream().write(Message.HASHSET);
					entry.getValue().getOutputStream().flush();
	
					entry.getValue().getOutputStream().write(sendbuf);
					entry.getValue().getOutputStream().flush();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		Server._serverLog.append("\n");
	}
	
	public static void ServerClosed()
	{
		try
		{
			for(Map.Entry<String, Socket> entry: Server.Maptest.entrySet())
			{
				entry.getValue().getOutputStream().write(Message.SERVERDOWN);
				entry.getValue().getOutputStream().flush();
			}
			System.exit(0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static byte[] toByteArray(Object obj) throws IOException
    {
        byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        
        try 
        {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
        }
        finally
        {
            if (oos != null)
            {
                oos.close();
            }
            if (bos != null)
            {
                bos.close();
            }
        }
        return bytes;
    }

    public static Object toObject(byte[] bytes) throws IOException, ClassNotFoundException
    {
        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        
        try
        {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = ois.readObject();
        }
        finally
        {
            if (bis != null)
            {
                bis.close();
            }
            if (ois != null)
            {
                ois.close();
            }
        }
        return obj;
    }

}
