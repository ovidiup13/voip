package main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;


import tcp.sockethandler.*;

/**
 * 
 * @author Alex Danila
 *
 */




public class GuiMainClient {

	// vars declarations

	private static String hostname = "";
	private static int port;
	private static String username = "";
	private static String password = "";
	public static SocketHandler client;

	// log in window

	private static JFrame loginWindow = new JFrame();
	private static JPanel imagePanel = new JPanel();
	private static JTextField portField = new JTextField(20);
	private static JTextField hostField = new JTextField(20);
	private static JTextField usernameField = new JTextField(20);
	private static JTextField passwordField = new JTextField(20);

	private static JLabel enterUsernameLabel = new JLabel();
	private static JLabel enterPasswordLabel = new JLabel();
	private static JLabel enterPortLabel = new JLabel();
	private static JLabel enterHostLabel = new JLabel();
	private static JButton registerButton = new JButton();
	private static JButton loginButton = new JButton();

	// main window

	private static JFrame mainWindow = new JFrame();
	public static JPanel userAvatar = new JPanel();
	public static JList onlineUsers = new JList();

	private static JButton callButton = new JButton();
	private static JButton logoutButton = new JButton();

	private static JLabel onlineLabel = new JLabel();
	private static JLabel usernameLabel = new JLabel();
	private static JLabel usernameBox = new JLabel();
	private static JScrollPane onlineScroller = new JScrollPane();

	// call window
	private static JFrame callWindow = new JFrame();
	private static JTextField callUserField = new JTextField(15);
	private static JButton makeCallButton = new JButton();
	private static JLabel callUserLabel = new JLabel();
	private static ImageIcon loadingBar = new ImageIcon("ajax-loader.gif",
			"loading-bar");
	public static JLabel imageLabel = new JLabel(loadingBar);

	public static void main(String[] args) {
		
		Connect();
		BuildLoginWindow();
		

	}

	public static void BuildLoginWindow() {

		loginWindow.setTitle("VOIP Login ");
		loginWindow.setLayout(null);
		loginWindow.setSize(310, 500);
		loginWindow.setLocationRelativeTo(null);

		imagePanel.setBorder(BorderFactory.createRaisedBevelBorder());
		loginWindow.getContentPane().add(imagePanel);
		imagePanel.setBounds(10, 10, 270, 250);

		enterHostLabel.setText("Host name:");
		//loginWindow.getContentPane().add(enterHostLabel);
		enterHostLabel.setBounds(10, 290, 100, 20);

		hostField.setText("localhost");
		//loginWindow.getContentPane().add(hostField);
		hostField.setBounds(100, 290, 180, 20);

		enterPortLabel.setText("Port number:");
		//loginWindow.getContentPane().add(enterPortLabel);
		enterPortLabel.setBounds(10, 315, 100, 20);

		portField.setText("9991");
		//loginWindow.getContentPane().add(portField);
		portField.setBounds(100, 315, 180, 20);

		enterUsernameLabel.setText("Username:");
		loginWindow.getContentPane().add(enterUsernameLabel);
		enterUsernameLabel.setBounds(10, 345, 100, 20);

		usernameField.setText("username");
		loginWindow.getContentPane().add(usernameField);
		usernameField.setBounds(100, 345, 180, 20);

		enterPasswordLabel.setText("Password: ");
		loginWindow.getContentPane().add(enterPasswordLabel);
		enterPasswordLabel.setBounds(10, 375, 100, 20);

		passwordField.setText("default");
		loginWindow.getContentPane().add(passwordField);
		passwordField.setBounds(100, 375, 180, 20);

		registerButton.setText("Register");
		loginWindow.getContentPane().add(registerButton);
		registerButton.setBounds(10, 405, 90, 20);
		registerButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				RegisterRequest();
			}
		});

		loginButton.setText("Login");
		loginWindow.getContentPane().add(loginButton);
		loginButton.setBounds(190, 405, 90, 20);
		loginButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				LoginRequest();
				
				

			}
		});

		loginWindow.setVisible(true);
		

		

	}

	public static void BuildMainWindow() {

		mainWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				LogoutRequest();

			}
		});

		mainWindow.setSize(310, 500);
		mainWindow.setResizable(false);
		mainWindow.getContentPane().setLayout(null);
		mainWindow.setSize(310, 500);
		mainWindow.setLocationRelativeTo(null);

		userAvatar.setBorder(BorderFactory.createRaisedBevelBorder());
		mainWindow.getContentPane().add(userAvatar);
		userAvatar.setBounds(10, 10, 150, 150);

		callButton.setText("Call");
		mainWindow.getContentPane().add(callButton);
		callButton.setBounds(10, 170, 115, 25);
		callButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				BuildCallWindow();

			}
		});

		logoutButton.setText("Logout");
		mainWindow.getContentPane().add(logoutButton);
		logoutButton.setBounds(170, 170, 115, 25);
		logoutButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				LogoutRequest();

			}
		});

		// Loading Bar

		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainWindow.getContentPane().add(imageLabel);
		imageLabel.setBounds(70, 210, 170, 10);
		imageLabel.setVisible(false);

		// Online users list

		onlineLabel.setHorizontalAlignment(SwingConstants.CENTER);
		onlineLabel.setText("Online users:");
		mainWindow.getContentPane().add(onlineLabel);
		onlineLabel.setBounds(70, 230, 170, 16);

		onlineScroller
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		onlineScroller
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		onlineScroller.setViewportView(onlineUsers);
		mainWindow.getContentPane().add(onlineScroller);
		onlineScroller.setBounds(70, 250, 170, 180);

		usernameLabel.setText("");
		mainWindow.getContentPane().add(usernameLabel);
		usernameLabel.setBounds(70, 10, 170, 15);

		usernameBox.setHorizontalAlignment(SwingConstants.CENTER);
		mainWindow.getContentPane().add(usernameBox);
		usernameBox.setBounds(70, 17, 170, 20);

		mainWindow.setVisible(true);

		

	}

	public static void BuildCallWindow() {

		callWindow = new JFrame();
		callWindow.setTitle("Call Receiver");
		callWindow.setLayout(null);
		callWindow.setSize(310, 100);
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
		makeCallButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				CallRequest();
			}
		});

		callWindow.setVisible(true);

	}
	
	
	public static void Connect(){
		
		try
		{	
			//hostname = hostField.getText().trim();
			//port = Integer.parseInt(portField.getText().trim());
			
			hostname = "192.168.173.1";
			port = 9991;
			
			client = new SocketHandler(hostname, port);
			if(client.startConnection()){
				System.out.println("Connection established!");
			}
		}
		catch (Exception e)
		{
			System.out.println("Error connecting from client!");
			JOptionPane.showMessageDialog(null, "Server not responding!");
			e.printStackTrace();
			System.exit(0);
		}
		
	}
	
	public static void LoginRequest(){
		
		if (!usernameField.getText().equals("") || !passwordField.getText().equals(""))
		{		
			
			username = usernameField.getText().trim();	
			password = passwordField.getText().trim();
			
			if(client.sendLogInRequest(username, password)){
			System.out.println("Message for LOG IN: ");

			loginWindow.setVisible(false);
			BuildMainWindow();
			mainWindow.setTitle("VOIP-User: " + username);
			logoutButton.setEnabled(true);
			
			
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			}
			
			else {
				
				JOptionPane.showMessageDialog(null, "Login unsucessful! Try again!");
			}
			
	
			
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Invalid username or password!");
		}
		
		
	}
	
	public static void RegisterRequest(){
		
		
		if (!usernameField.getText().equals("") || !passwordField.getText().equals(""))
		{		
			
			username = usernameField.getText().trim();	
			password = passwordField.getText().trim();
			
			if (
			client.sendRegisterRequest(username, password)){
			System.out.println("Message for REGISTER: ");
			JOptionPane.showMessageDialog(null, "Registration successful!");
			}
			
			else {
				JOptionPane.showMessageDialog(null, "Registration unsuccessful!");
			}
			
		
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Enter an username and a password!");
		}
		
		
		
	}
	
	public static void LogoutRequest(){
		
		client.sendLogOutRequest();
		System.out.println("Logout request sent");
		
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//close connection
				if(!client.closeConnection())
					System.err.println("Client: Connection error, could not close connection");
				else
					System.out.println("Connection closed.");
		
		System.exit(0);
		
		
	}
	
	public static void CallRequest(){
		
		if (!callUserField.getText().equals(""))
		{
			
			
			client.sendCallRequest(callUserField.getText().trim());
			System.out.println("Message for CALL: ");
			

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			
			System.out.println("Call request sent");
			callUserField.setText("");
			callWindow.dispose();
			
			
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Enter user to call.");
		}
		
	}
	

	
	
	

}
