import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

/**
 * @author Alex
 *
 */
public class Client
{
	private static ClientThread _myClient;
	private static String _userName = "";
	private static String _hostName = "";
	private static String _portNumber = "";
	
	
	//main window
	private static JFrame mainWindow = new JFrame();
	public static JList onlineUsers = new JList();
	
	
	private static JButton connectButton = new JButton();
	private static JButton disconnectButton = new JButton();
	private static JButton callButton = new JButton();
	
	private static JLabel onlineLabel = new JLabel();
	private static JLabel userNameLabel = new JLabel();
	private static JLabel userNameBox = new JLabel();
	private static JScrollPane onlineScroller = new JScrollPane();
	
	//log in window
	private static JFrame loginWindow = new JFrame();
	private static JTextField userNameBoxField = new JTextField(20);
	private static JTextField portNumberField = new JTextField(20);
	private static JTextField hostNameField = new JTextField(20);
	private static JButton enterButton = new JButton();
	private static JLabel enterUserNameLabel = new JLabel();
	private static JLabel enterPortLabel = new JLabel();
	private static JLabel enterHostLabel = new JLabel();
	
	//call window
	private static JFrame callWindow = new JFrame();
	private static JTextField callUserField = new JTextField(15);
	private static JButton makeCallButton = new JButton();
	private static JLabel callUserLabel = new JLabel();
	private static ImageIcon loadingBar = new ImageIcon("ajax-loader.gif", "loading-bar");
	public static JLabel imageLabel = new JLabel(loadingBar);
	
	
	
	public static void main(String[] args)
	{
		BuildMainWindow();
	}
	
	public static void Connect()
	{
		try
		{			
			loginWindow.setVisible(false);
			mainWindow.setVisible(true);
			
			_myClient = new ClientThread(_userName, _hostName, _portNumber);
			_myClient.start();
		}
		catch (Exception e)
		{
			System.out.println("Error in connect from clientgui");
			JOptionPane.showMessageDialog(null, "Server not responding");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	
	public static void BuildMainWindow()
	{
		mainWindow.addWindowListener(
				new WindowAdapter()
				{
					public void windowClosing(WindowEvent evt) 
					{
						DCButton();
						
					}
				});
		
		mainWindow.setSize(300, 320);
		mainWindow.setResizable(false);
		mainWindow.getContentPane().setLayout(null);
		mainWindow.setSize(300, 320);
		mainWindow.setLocationRelativeTo(null);
		
		
		disconnectButton.setText("Disconnect");
		mainWindow.getContentPane().add(disconnectButton);
		disconnectButton.setBounds(170, 40, 115, 25);
		disconnectButton.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent event)
					{
						DCButton();
						
					}
				});
		
		callButton.setText("Call");
		mainWindow.getContentPane().add(callButton);
		callButton.setBounds(10, 40, 115, 25);
		callButton.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent event)
					{
						CallButton();
					}
				});
		
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainWindow.getContentPane().add(imageLabel);
		imageLabel.setBounds(70, 75, 170, 10);
		imageLabel.setVisible(false);
		
		
		onlineLabel.setHorizontalAlignment(SwingConstants.CENTER);
		onlineLabel.setText("Online users:");
		mainWindow.getContentPane().add(onlineLabel);
		onlineLabel.setBounds(70, 90, 170, 16);
		
		onlineScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		onlineScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		onlineScroller.setViewportView(onlineUsers);
		mainWindow.getContentPane().add(onlineScroller);
		onlineScroller.setBounds(70, 110 , 170, 180);
		
		userNameLabel.setText("");
		mainWindow.getContentPane().add(userNameLabel);
		userNameLabel.setBounds(70, 10 , 170, 15);
		
		userNameBox.setHorizontalAlignment(SwingConstants.CENTER);
		mainWindow.getContentPane().add(userNameBox);
		userNameBox.setBounds(70, 17 , 170, 20);
		
		BuildLoginWindow();
	}
	
	public static void BuildLoginWindow()
	{		
		loginWindow.setTitle("VOIP Login Window");
		loginWindow.setLayout(null);
		loginWindow.setSize(400, 180);
		loginWindow.setLocationRelativeTo(null);
		
		enterHostLabel.setText("Host name:");
		loginWindow.getContentPane().add(enterHostLabel);
		enterHostLabel.setBounds(5, 5, 100, 20);
		
		hostNameField.setText("localhost");
		loginWindow.getContentPane().add(hostNameField);
		hostNameField.setBounds(105, 5, 260, 20);
		
		enterPortLabel.setText("Port number:");
		loginWindow.getContentPane().add(enterPortLabel);
		enterPortLabel.setBounds(5, 35, 100, 20);
		
		portNumberField.setText("3000");
		loginWindow.getContentPane().add(portNumberField);
		portNumberField.setBounds(105, 35, 260, 20);
		
		enterUserNameLabel.setText("Username:");
		loginWindow.getContentPane().add(enterUserNameLabel);
		enterUserNameLabel.setBounds(5, 65, 100, 20);
		
		userNameBoxField.setText("");
		loginWindow.getContentPane().add(userNameBoxField);
		userNameBoxField.setBounds(105, 65, 260, 20);
		
		enterButton.setText("Login");
		loginWindow.getContentPane().add(enterButton);
		enterButton.setBounds(105, 95, 80, 20);
		enterButton.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent event)
					{
						EnterChat();
					}
				});
		
		loginWindow.setVisible(true);
	}
	
	public static void EnterChat()
	{
		if (!userNameBoxField.getText().equals(""))
		{		
			_userName = userNameBoxField.getText().trim();		
			_hostName = hostNameField.getText().trim();
			_portNumber = portNumberField.getText().trim();
			loginWindow.setVisible(false);
			mainWindow.setTitle("VOIP - Logged in as: " + _userName);
			disconnectButton.setEnabled(true);
			connectButton.setEnabled(false);
			Connect();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Enter an username.");
		}
	}
	
	
	
	
	public static void DCButton()
	{
		Message bye = new Message(_userName, "", "%BYE%");
		ClientThread.Send(bye);
	}
	

	
	
	public static void CallButton()
	{		
		callWindow = new JFrame();
		callWindow.setTitle("Call Receiver");
		callWindow.setLayout(null);
		callWindow.setSize(320, 100);
		callWindow.setLocationRelativeTo(null);
		
		callUserLabel.setText("Username:");
		callWindow.getContentPane().add(callUserLabel);
		callUserLabel.setBounds(5, 5, 100, 20);
		
		callUserField.setText("");
		callWindow.getContentPane().add(callUserField);
		callUserField.setBounds(105, 5, 200, 20);
		
		makeCallButton.setText("Call");
		callWindow.getContentPane().add(makeCallButton);
		makeCallButton.setBounds(105, 30, 80, 20);
		makeCallButton.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent event)
					{
						RequestCall();
					}
				});
		
		callWindow.setVisible(true);
		
		
	}
		
	
	public static void RequestCall()
	{
		if (!callUserField.getText().equals(""))
		{
			Message call = new Message(_userName, callUserField.getText().trim(), "%CALL ME MAYBE%");
			ClientThread.Send(call);
			System.out.println("call request sent");
			callUserField.setText("");
			callWindow.dispose();
			
			
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Enter user to call.");
		}
	}
}
