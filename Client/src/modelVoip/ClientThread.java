package modelVoip;

import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;

/**
 * 
 */
public class ClientThread extends Thread
{
	private static Socket socket = null;
	private static DatagramSocket callSocket = null;
	private static byte[] sendbuf = null;
	private static byte[] recbuf = null;
	public static User user = new User();
	public static String username = "";
	public static String hostname = "";
	public static String portnum = "";
	public static String musername = "";
	public static String message = "";
	
	/**
	 * constuctor to start a ClientThread
	 * 
	 * @param 
	 */
	public ClientThread(String user, String host, String port)
	{
		super();
		ClientThread.username = user;
		ClientThread.hostname = host;
		ClientThread.portnum = port;
	}
	
	public void run()
	{
		InetAddress address;
		int port = -1;
		int state = 0;
		
		try 
		{
			socket = new Socket(hostname, Integer.parseInt(portnum));
						
			sendbuf = new byte[socket.getSendBufferSize()];
			recbuf = new byte[socket.getReceiveBufferSize()];
						
			address = socket.getInetAddress();
			port = socket.getPort();
			user = new User(username, address, port);
			
			//send user data to server
			sendbuf = toByteArray(user);
			socket.getOutputStream().write(Message.USER);
			socket.getOutputStream().write(sendbuf);
			socket.getOutputStream().flush();
			
			while(true)
			{
				Message rec = new Message();
				try
				{
					state = socket.getInputStream().read();
				}
				finally
				{
					if (state == Message.HASHSET)
					{
						socket.getInputStream().read(recbuf);
						Object[] online = (Object[]) toObject(recbuf);
						String[] onlineList = new String[online.length];
						for (int i = 0; i < online.length; i++)
						{
							onlineList[i] = (String) online[i];
						}
						Client.onlineUsers.setListData(onlineList);
					}
					
					else if (state == Message.DC)
					{
						socket.getInputStream().read(recbuf);
						rec = (Message) toObject(recbuf);
						
					}
					else if (state == Message.NONEXISTANT)
					{
						socket.getInputStream().read(recbuf);
						rec = (Message) toObject(recbuf);
						JOptionPane.showMessageDialog(null, "Imaginary people like \"" + rec.getRecipient() + "\" don't count...");
					}
					else if (state == Message.REQUEST)
					{
						socket.getInputStream().read(recbuf);
						rec = (Message) toObject(recbuf);
						
						Message confirmation = new Message();
						String prompt = "Accept call from " + rec.getOrigin() + "?";
						int reply = JOptionPane.showConfirmDialog(null, prompt, "Call confirmation", JOptionPane.YES_NO_OPTION);
				        if (reply == JOptionPane.YES_OPTION)
				        {				        	
				        	String addressName = socket.getRemoteSocketAddress().toString() + ":" + rec.getOrigin();
				        	System.out.println(addressName);
				        	
				        	sendbuf = toByteArray(addressName);
				        	socket.getOutputStream().write(Message.ACCEPT);
				        	socket.getOutputStream().flush();
				        	socket.getOutputStream().write(sendbuf);
				        	socket.getOutputStream().flush();
				        	
				        	UDPreceiver();
				        	Client.imageLabel.setVisible(true);
				        }
				        else 
				        {
				        	//send false
				        	confirmation = new Message(rec.getOrigin(), rec.getRecipient(), "%IMPOSSIBRU%");
				        	Send(confirmation);
				        }
					}
					else if (state == Message.ACCEPT)
					{
						socket.getInputStream().read(recbuf);
						String temp = (String) toObject(recbuf);
						System.out.println(temp);
						String[] inetaddress = temp.split(":");
						System.out.println(inetaddress[0]);
						JOptionPane.showMessageDialog(null, rec.getRecipient() +" Accepted");
						
						
						//call a method to start calls?
						Client.imageLabel.setVisible(true);
						UDPsender(inetaddress[0]);
					}
					else if (state == Message.DECLINE)
					{
						socket.getInputStream().read(recbuf);
						rec = (Message) toObject(recbuf);
						JOptionPane.showMessageDialog(null, rec.getRecipient() +" Declined");
						callSocket.close();
						
					}
					else if (state == Message.SERVERDOWN)
					{
						JOptionPane.showMessageDialog(null, "Server has gone down...");
						System.exit(0);
					}
					else if (state == Message.ERROR)
					{
						socket.getInputStream().read(recbuf);
						rec = (Message) toObject(recbuf);
						JOptionPane.showMessageDialog(null, "It's bad to talk to yourself...");
					}
					else if (state == Message.REMOVED)
					{
						socket.getInputStream().read(recbuf);
						rec = (Message) toObject(recbuf);
						System.exit(0);
					}
				}
			}
		}
		catch (SocketException e) 
		{
			JOptionPane.showMessageDialog(null, "Server is not responding");
			System.exit(0);
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
	
	public static void Send(Message message)
	{
		try
		{
			sendbuf = toByteArray(message);

			//dc message
			if (message.getRecipient().equalsIgnoreCase("") &&
				message.getMessage().equalsIgnoreCase("%BYE%"))
			{
				socket.getOutputStream().write(Message.BYE);
			}
			//call message
			else if (message.getMessage().equalsIgnoreCase("%CALL ME MAYBE%"))
			{
				socket.getOutputStream().write(Message.CALL);
				callSocket = new DatagramSocket();
			}
			//accepted call message
			else if (message.getMessage().equalsIgnoreCase("%SURE SURE%"))
			{
				socket.getOutputStream().write(Message.ACCEPT);
			}
			//rejected call message
			else if (message.getMessage().equalsIgnoreCase("%IMPOSSIBRU%"))
			{
				socket.getOutputStream().write(Message.DECLINE);
			}
			
			socket.getOutputStream().write(sendbuf);
			socket.getOutputStream().flush();
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
    
    public static void UDPsender(String inet)
    {
    	try
    	{
    		String[] mal = inet.split("/");
    		String host = mal[0];
    		System.out.println(host);
	    	callSocket = new DatagramSocket();
			byte[] buffer = "Sender".getBytes();
			
			InetAddress[] testAddr = InetAddress.getAllByName(host);
	
			System.out.println(testAddr[0]);
			callSocket.connect(testAddr[0], 3001);
			
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, testAddr[0], 3001);

			callSocket.send(packet);
			
			packet = new DatagramPacket(buffer, buffer.length);
			
			callSocket.receive(packet);
			byte[] message = packet.getData();
			
			for (byte mes: message)
			{
				System.out.print((char)mes);
			}
			System.out.println();
			callSocket.close();
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    }
	
    public static void UDPreceiver()
    {
    	try
    	{
	    	callSocket = new DatagramSocket(3001);
			
			byte[] buffer = new byte[100];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			
			callSocket.receive(packet);
			
			byte[] message = packet.getData();
			
			for (byte mes: message)
			{
				System.out.print((char)mes);
			}
			System.out.println();
			
			buffer = "Received".getBytes();
			DatagramPacket rPacket = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
			
			
			callSocket.send(rPacket);
			callSocket.close();
	    }
		catch (Exception e)
		{
			e.printStackTrace();
		}
    }
}
