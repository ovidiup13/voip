package main;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;










import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import p2p.SimpleVoIPCall;

import com.jtattoo.plaf.smart.SmartLookAndFeel;

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
	private static ClientThread clientThread;

	// log in window

	public static JFrame loginWindow = new JFrame();
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
	public static JButton loginButton = new JButton();
	
	public static FriendListItem[] friendListItems;
	private static JPopupMenu popupMenu;

	// main window //
	
	
	//menu
     private static JMenuBar menuBar = new JMenuBar();
    
  
    // Define and add two drop down menu to the menubar
     private static JMenu optMenu = new JMenu("Options");
    
    
    // Create and add simple menu item to one of the drop down menu
    private static JMenuItem addfAction = new JMenuItem("Add friend");
    private static JMenuItem delfAction = new JMenuItem("Delete friend");
    private static JMenuItem helpAction = new JMenuItem("Help");
    private static JMenuItem logoutAction = new JMenuItem("Log out");
    private static JMenuItem testNotifcaion = new JMenuItem("TestNotificaions");
 
    
	
	
	//rest of the main window

	public static JFrame mainWindow = new JFrame();
	public static JPanel userAvatar = new JPanel();
	public static JList<FriendListItem> onlineUsers = new JList<FriendListItem>();

	public static JButton callButton = new JButton();
	public static JButton logoutButton = new JButton();

	private static JLabel onlineLabel = new JLabel();
	private static JLabel usernameLabel = new JLabel();
	private static JLabel usernameBox = new JLabel();
	private static JScrollPane onlineScroller = new JScrollPane();
	
	
	//add friend window
	
	public static JFrame addFriendWindow = new JFrame();
	public static JTextField addFriendField = new JTextField(15);
	public static JButton addFriendButton = new JButton();
	private static JLabel addFriendLabel = new JLabel();
		
	//delete friend window
		
		

	// call initiated window
	public static JFrame callWindow = new JFrame();
	public static JTextField callUserField = new JTextField(15);
	public static JButton makeCallButton = new JButton();
	private static JLabel callUserLabel = new JLabel();
	
	
	
	
	
	// call inquiry window
	
	public static JFrame callinqWindow = new JFrame();
	public static JLabel callmsgLabel = new JLabel();
	public static JLabel usercallingLabel = new JLabel();
	public static JButton acceptCallButton = new JButton();
	public static JButton declineCallButton = new JButton();
	
	
	 
	
	
	// call in progress window
	public static JFrame callinprogWindow = new JFrame();
	public static JPanel avatarPanel = new JPanel();
	public static JLabel callerUserLabel = new JLabel();
	public static JLabel userstatusLabel = new JLabel();
	public static JLabel callstateLabel = new JLabel();
	
	private static ImageIcon loadingBar = new ImageIcon("ajax-loader.gif", "loading bar");
	public static JLabel imageLabel = new JLabel(loadingBar);
	
	public static JButton startCallButton = new JButton();
	public static  JButton stopCallButton = new JButton();
	private static URL resource;
	
	
	public static SimpleVoIPCall play;
	
	/*
	public GuiMainClient(){
		main();
	}
	
	*/
	public static void main(String args[]) {
	
			frameIco = createImageIcon("hype.png", "icon");
			mainWindow.setIconImage(frameIco.getImage());
			callWindow.setIconImage(frameIco.getImage());
			loginWindow.setIconImage(frameIco.getImage());
			callinprogWindow.setIconImage(frameIco.getImage());
			callinqWindow.setIconImage(frameIco.getImage());
			addFriendWindow.setIconImage(frameIco.getImage());
            Connect();
    		BuildLoginWindow();
       
	}

	public static String getUsername() {
		return username;
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
	
	public static ImageIcon frameIco;

	public static void BuildMainWindow() {
		
		
		//menu building
		
		menuBar.add(optMenu);
		optMenu.add(addfAction);
		optMenu.add(delfAction);
		optMenu.add(helpAction);
		optMenu.add(logoutAction);
		optMenu.add(testNotifcaion);
		
		addfAction.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				BuildAddFriendWindow();
				

			}
		});
		
		delfAction.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				System.out.println("Delete friend clicked!");
				DeleteFriendRequest();
			}
		});
		
		helpAction.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				System.out.println("Help clicked!");

			}
		});
		
		logoutAction.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				System.out.println("Log out clicked!");
				LogoutRequest();
				

			}
		});
		
		testNotifcaion.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				System.out.println("testNotificaions clicked!");
				NotificationForSentFriendRequest("Viktor", false);
				

			}

		
		});
		
		mainWindow.setJMenuBar(menuBar);
		
		
		
		
		//window building

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
		onlineLabel.setText("Friends list:");
		mainWindow.getContentPane().add(onlineLabel);
		onlineLabel.setBounds(70, 230, 170, 16);

		onlineScroller
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		onlineScroller
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		onlineScroller.setViewportView(onlineUsers);
		mainWindow.getContentPane().add(onlineScroller);
		onlineScroller.setBounds(20, 250, 270, 180);
		
		onlineUsers.setCellRenderer( new FriendsCellRenderer() );

		usernameLabel.setText("");
		mainWindow.getContentPane().add(usernameLabel);
		usernameLabel.setBounds(70, 10, 170, 15);

		usernameBox.setHorizontalAlignment(SwingConstants.CENTER);
		mainWindow.getContentPane().add(usernameBox);
		usernameBox.setBounds(70, 17, 170, 20);

		popupMenu = new JPopupMenu();
		JMenuItem removeFriendItem = new JMenuItem("Remove Friend");
		removeFriendItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				System.out.println("removing friend"); //probably keep a variable that is set to the target friend when the popup menu is spawned
			}
		});
		
		popupMenu.add(removeFriendItem);
		mainWindow.setVisible(true);

		onlineUsers.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent evt) {
				if (evt.isPopupTrigger()) showPopup(evt);
			}
			
			public void mouseReleased(MouseEvent evt) {
				if (evt.isPopupTrigger()) showPopup(evt);
			}
			
			public void showPopup(MouseEvent evt) {
		    	if (friendListItems == null) return;
		    	
		    	JList list = (JList)evt.getSource();
	            int index = list.locationToIndex(evt.getPoint());
	            FriendListItem item = friendListItems[index];
	            
	            if (item.mode == FriendListItemMode.FRIEND) popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
			}
			
		    public void mouseClicked(MouseEvent evt) {
		    	if (friendListItems == null) return;
		    	
		    	JList list = (JList)evt.getSource();
	            int index = list.locationToIndex(evt.getPoint());
	            //here we'll want to initiate a call if the user is online
	            FriendListItem item = friendListItems[index];
		        
	            if (item.mode == FriendListItemMode.REQUEST && evt.getClickCount() == 1) { //click friend request (accept/decline)
	            	int x = evt.getPoint().x-list.getLocation().x;
	            	if (list.getWidth()-x < 26) System.out.println("accept");
	            	else if (list.getWidth()-x < 46) System.out.println("decline");
	            } else if (item.mode == FriendListItemMode.FRIEND && item.status == 1 && evt.getClickCount() == 2) { //double click friend (call)
		            CallRequest(item.text);
		            System.out.println("double clicked: "+item.text);
		        } 
		    }
		});

	}
	
	

	public static void BuildCallWindow() {

		callWindow = new JFrame();
		callWindow.setIconImage(frameIco.getImage());
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
		if((makeCallButton.getActionListeners().length == 0)){
			makeCallButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent event) {
					CallRequest(callUserField.getText());
				}
			});
			}
		callWindow.setVisible(true);
		

	}
	
	public static void BuildAddFriendWindow() {
		addFriendWindow = new JFrame();
		addFriendWindow.setIconImage(frameIco.getImage());
		addFriendWindow.setTitle("Add Friend");
		addFriendWindow.setLayout(null);
		addFriendWindow.setSize(310, 80);
		//addFriendWindow.setLocationRelativeTo(null);
		

		addFriendLabel.setText("Username:");
		addFriendWindow.getContentPane().add(addFriendLabel);
		addFriendLabel.setBounds(5, 5, 100, 20);

		addFriendField.setText("");
		addFriendWindow.getContentPane().add(addFriendField);
		addFriendField.setBounds(105, 5, 200, 20);

		addFriendButton.setText("Add");
		addFriendWindow.getContentPane().add(addFriendButton);
		addFriendButton.setBounds(105, 30, 80, 20);
		if((addFriendButton.getActionListeners().length == 0)){
			addFriendButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent event) {
					AddFriendRequest();
				}
		});
		}
		addFriendWindow.setVisible(true);
		

	}
	
	
	public static void BuildCallInqWindow(){
		
		callinqWindow.setTitle("Incoming call");
		callinqWindow.setLayout(null);
		callinqWindow.setSize(310, 300);
		callinqWindow.setLocationRelativeTo(null);
		
		callmsgLabel.setText("You have an incoming call from ");
		callmsgLabel.setHorizontalAlignment(SwingConstants.CENTER);
		callmsgLabel.setBounds(55,100,200,20);
		callinqWindow.getContentPane().add(callmsgLabel);
		
		
		usercallingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		usercallingLabel.setBounds(55,125,200,20);
		callinqWindow.getContentPane().add(usercallingLabel);
		
		
		acceptCallButton.setText("Accept");
		acceptCallButton.setHorizontalAlignment(SwingConstants.CENTER);
		callinqWindow.getContentPane().add(acceptCallButton);
		acceptCallButton.setBounds(20, 220, 80, 20);
		if((acceptCallButton.getActionListeners().length == 0)){
			acceptCallButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent event) {
					AcceptCallResponse();
				}
			});
			}
		
		declineCallButton.setText("Decline");
		declineCallButton.setHorizontalAlignment(SwingConstants.CENTER);
		callinqWindow.getContentPane().add(declineCallButton);
		declineCallButton.setBounds(200, 220, 80, 20);
		if((declineCallButton.getActionListeners().length == 0)){
			declineCallButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent event) {
					RejectCallResponse();
				}
			});
			}
		
		
		callinqWindow.setVisible(true);
		
		
		
		
	}
	
	
	public static void BuildCallInProgWindow (){
		
		callinprogWindow = new JFrame();
		callinprogWindow.setIconImage(frameIco.getImage());
		callinprogWindow.setTitle("VOIP Call ");
		callinprogWindow.setLayout(null);
		callinprogWindow.setSize(310, 300);
		callinprogWindow.setLocationRelativeTo(null);
		
		avatarPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		callinprogWindow.getContentPane().add(avatarPanel);
		avatarPanel.setBounds(105, 20, 105, 105);
		
		

		//callerUserLabel.setText("Username ??");
		callerUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
		callinprogWindow.getContentPane().add(callerUserLabel);
		callerUserLabel.setBounds(55, 140, 200, 20);
		
		callstateLabel.setText("Calling...");
		callstateLabel.setHorizontalAlignment(SwingConstants.CENTER);
		callinprogWindow.getContentPane().add(callstateLabel);
		callstateLabel.setBounds(55, 165, 200, 20);

		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		callinprogWindow.getContentPane().add(imageLabel);
		imageLabel.setBounds(70, 190, 170, 10);
		imageLabel.setVisible(false);
		
		
		/*
		startCallButton.setText("Yes");
		callWindow.getContentPane().add(startCallButton);
		startCallButton.setBounds(105, 30, 80, 20);
		startCallButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				AcceptCallResponse();
			}
		});
		
		*/
		
		stopCallButton.setText("End call");
		stopCallButton.setHorizontalAlignment(SwingConstants.CENTER);
		callinprogWindow.getContentPane().add(stopCallButton);
		stopCallButton.setBounds(105, 220, 80, 20);
		if((stopCallButton.getActionListeners().length == 0)){
			stopCallButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent event) {
					EndCallRequest();
				}
			});
			}

		callinprogWindow.setVisible(false);
		
	}
	
	
	public static void Connect(){
		
		try
		{	
			//hostname = hostField.getText().trim();
			//port = Integer.parseInt(portField.getText().trim());
			
			hostname = "localhost";
			port = 9991;
			
			client = new SocketHandler(hostname, port);
			if(client.startConnection()){
				System.out.println("Connection established!");
				
				
			clientThread = new ClientThread(client);
			clientThread.start();
			
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
			
			client.sendLogInRequest(username, password);
			System.out.println("Message for LOG IN: ");
			System.out.println("Log in request sent from client");
		
			
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
			
			client.sendRegisterRequest(username, password);
			System.out.println("Message for REGISTER: ");
			System.out.println("Register request sent from client");
		
		
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Enter an username and a password!");
		}
		
		
		
	}
	
	public static void LogoutRequest(){
		
		client.sendLogOutRequest();
		System.out.println("Logout request sent");
		clientThread.interrupt();
	}
	
	public static void CallRequest(String target){
		
		if (!target.equals(""))
		{
			String usercalled = target.trim();
			client.sendCallRequest(usercalled);
			System.out.println("Message for CALL: ");
			System.out.println("Call request sent");
			callUserField.setText("");
			callWindow.dispose();
			callerUserLabel.setText(usercalled);
			BuildCallInProgWindow();
			callinprogWindow.setVisible(true);
			
			
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Enter user to call.");
		}
		
	}

	
	public static void EndCallRequest(){
		
			client.sendEndCallRequest();
			callinprogWindow.dispose();
			callstateLabel.setText("Disconnected!");
			endCall();
			
			
		
	}
	
	public static void AcceptCallResponse(){
		
		client.sendCallResponse(true);
		System.out.println("Call was accepted");
		callinqWindow.dispose();
		BuildCallInProgWindow();
		callinprogWindow.setVisible(true);
		
		
		
		
	}
		
	public static void RejectCallResponse(){
		
		client.sendCallResponse(false);
		System.out.println("Call was rejected");
		callinqWindow.dispose();
		
		
	}
	
	
	public static void startCall(String ip, int port, int callid){
		
		System.out.println("Call started!");
		play = new SimpleVoIPCall();
		play.start(ip, port,callid);
		
		
	
	}
	
	
	public static void endCall(){
		System.out.println("Call fucking ended!");
		if (play != null) play.stop();
	}
	
	
	
	public static void FriendListRequest(){
		
		
		client.sendFriendListRequest();
			}
	
	public static void AddFriendRequest(){
		if (!addFriendField.getText().equals(""))
		{
			String username = addFriendField.getText().trim();
			client.sendAddFriendRequest(username);
			System.out.println("AddFriend request sent: "+ username);
			addFriendField.setText("");
			addFriendWindow.dispose();

		}else
		{
			JOptionPane.showMessageDialog(null, "Enter username to add.");

		}

	}
	

	
	
	public static void DeleteFriendRequest(){
		
		
		String username = "";
		client.sendDeleteFriendRequest(username);
		
	}
	
	
	
	private static void NotificationForSentFriendRequest(String username, boolean accepted) {
		String message=null;
		ImageIcon icon = null;
		if(accepted){
			 message = "User "+ username +" accepted your friend request";
			  icon = createImageIcon("small-tick.png",
		                "this is small tick");
		}else{
			 message = "User "+ username +" declined your friend request";
			 icon = createImageIcon("small-x.png",
		                "this is small x");
		}
		String header = "Message";
		final JFrame frame = new JFrame();
		frame.setSize(300,125);
		frame.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1.0f;
		constraints.weighty = 1.0f;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		JLabel headingLabel = new JLabel(header);
		//resource = clientThread.getContextClassLoader().getResource("small-tick.png");
		//ImageIcon icon = new ImageIcon(resource);
		headingLabel .setIcon(icon); 
		headingLabel.setOpaque(false);
		frame.add(headingLabel, constraints);
		constraints.gridx++;
		constraints.weightx = 0f;
		constraints.weighty = 0f;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.NORTH;
		JButton closeButton = new JButton("X");
		closeButton.setMargin(new Insets(1, 4, 1, 4));
		closeButton.setFocusable(false);
		frame.add(closeButton, constraints);
		constraints.gridx = 0;
		constraints.gridy++;
		constraints.weightx = 1.0f;
		constraints.weighty = 1.0f;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		JLabel messageLabel = new JLabel("<Html><div style=\"text-align: center;\">"+message+"</html>");
		frame.add(messageLabel, constraints);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setVisible(true);
		
		 closeButton = new JButton(new AbstractAction("x") {
	        public void actionPerformed(final ActionEvent e) {
	               frame.dispose();
	        }
	});
		 Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();// size of the screen
		 Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());// height of the task bar
		 frame.setLocation(scrSize.width - frame.getWidth(), scrSize.height - toolHeight.bottom - frame.getHeight());
		
		 new Thread(){
		      @Override
		      public void run() {
		           try {
		                  Thread.sleep(5000); // time after which pop up will be disappeared.
		                  frame.dispose();
		           } catch (InterruptedException e) {
		                  e.printStackTrace();
		           }
		      };
		}.start();
	
	
	}

	protected static ImageIcon createImageIcon(String path,String description) {
		java.net.URL imgURL = 	GuiMainClient.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	
	

}
