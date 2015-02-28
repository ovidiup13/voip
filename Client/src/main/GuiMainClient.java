package main;

import p2p.SimpleVoIPCall;
import tcp.sockethandler.SocketHandler;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;

/**
 * @author Team H
 */

public class GuiMainClient {

    // vars declarations

    private static String hostname = "";
    private static int port;
    private static String username = "";
    private static String password = "";
    public static SocketHandler client;
    private static ClientThread clientThread;
    private static KeepAliveThread keepAliveThread;
    public static boolean isConnected = false;

    // log in window

    public static JFrame loginWindow = new JFrame();
    private static BgPanel imagePanel = new BgPanel();
    private static JTextField hostField = new JTextField(20);
    private static JTextField usernameField = new JTextField(20);
    private static JPasswordField passwordField = new JPasswordField(20);

    private static JLabel enterUsernameLabel = new JLabel();
    private static JLabel enterPasswordLabel = new JLabel();
    private static JLabel enterHostLabel = new JLabel();
    private static JLabel connectionResultLabel = new JLabel();
    private static JButton connectionButton = new JButton();
    private static JButton registerButton = new JButton();
    private static JButton loginButton = new JButton();

    public static FriendListItem[] friendListItems;
    private static JPopupMenu popupMenu;

    // main window //


    //menu
    private static JMenuBar menuBar = new JMenuBar();


    // Define and add two drop down menu to the menubar
    private static JMenu optMenu = new JMenu("Options");


    // Create and add simple menu item to one of the drop down menu
    private static JMenuItem addfAction = new JMenuItem("Add friend");
    private static JMenuItem helpAction = new JMenuItem("Help");
    private static JMenuItem logoutAction = new JMenuItem("Log out");


    //rest of the main window

    public static JFrame mainWindow = new JFrame();
    public static JLabel userAvatar = new JLabel();
    public static JList<FriendListItem> onlineUsers = new JList<FriendListItem>();

    public static JButton callButton = new JButton();
    public static JButton logoutButton = new JButton();

    
    private static BgPanel usernameBar = new BgPanel();
    private static JPanel namePanel = new JPanel();
    private static JLabel onlineText = new JLabel();
    private static JLabel usernameLabel = new JLabel();
    private static JScrollPane onlineScroller = new JScrollPane();


    //add friend window

    public static JFrame addFriendWindow = new JFrame();
    public static JTextField addFriendField = new JTextField(15);
    public static JButton addFriendButton = new JButton();
    private static JLabel addFriendLabel = new JLabel();

    //delete friend window


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
    public static JButton stopCallButton = new JButton();


    public static SimpleVoIPCall play;
    public static String deleteTarget;

    /*
    public GuiMainClient(){
        main();
    }

    */
    public static void main(String args[]) {

        frameIco = createImageIcon("hype.png", "icon");
        mainWindow.setIconImage(frameIco.getImage());
        loginWindow.setIconImage(frameIco.getImage());
        callinprogWindow.setIconImage(frameIco.getImage());
        callinqWindow.setIconImage(frameIco.getImage());
        addFriendWindow.setIconImage(frameIco.getImage());
        BuildLoginWindow();

    }

    public static String getUsername() {
        return username;
    }


    public static void BuildLoginWindow() {

        loginWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        });

        loginWindow.setTitle("Hype Login");
        loginWindow.setLayout(null);
        loginWindow.setSize(295, 465);
        loginWindow.setLocationRelativeTo(null);

        imagePanel.setBorder(BorderFactory.createRaisedBevelBorder());
        loginWindow.getContentPane().add(imagePanel);
        imagePanel.setBounds(10, 10, 270, 270);
        imagePanel.setBackground(createImageIcon("hypebig.png", "logo").getImage());

        enterHostLabel.setText("Host name:");
        loginWindow.getContentPane().add(enterHostLabel);
        enterHostLabel.setBounds(10, 290, 100, 20);

        hostField.setText("localhost");
        loginWindow.getContentPane().add(hostField);
        hostField.setBounds(100, 290, 180, 20);

        connectionResultLabel.setText("");
        loginWindow.getContentPane().add(connectionResultLabel);
        connectionResultLabel.setBounds(20, 315, 190, 20);

        connectionButton.setText("Connect");
        connectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isConnected) {
                    hostname = hostField.getText();
                    try {
                        Connect();
                    } catch (IOException e1) {
                        connectionResultLabel.setText("Connection failed!");
                        connectionResultLabel.setForeground(Color.RED);
                        JOptionPane.showMessageDialog(null, "Could not connect to server.");
                        isConnected = false;
                        return;
                    }
                    
                    connectionResultLabel.setText("Connection successful!");
                    connectionResultLabel.setForeground(Color.GREEN);
                    isConnected = true;
                    hostField.setEditable(false);
                    connectionButton.setEnabled(false);
                }
            }
        });
        loginWindow.getContentPane().add(connectionButton);
        connectionButton.setBounds(190, 315, 90, 20);

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
                if(!isConnected)
                    JOptionPane.showMessageDialog(null, "You are not connected to a server. Please connect and try again.");
                else
                    RegisterRequest();
            }
        });

        loginButton.setText("Login");
        loginWindow.getContentPane().add(loginButton);
        loginButton.setBounds(190, 405, 90, 20);
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                if(!isConnected)
                    JOptionPane.showMessageDialog(null, "You are not connected to a server. Please connect and try again.");
                else
                    LoginRequest();
            }
        });

        loginWindow.setResizable(false);
        loginWindow.setVisible(true);


    }

    public static ImageIcon frameIco;

    public static void BuildMainWindow(String username) {

    	BorderLayout mainLayout = new BorderLayout();
        mainWindow.setLayout(mainLayout);
        
        //menu building

        menuBar.add(optMenu);
        
        optMenu.add(addfAction);
        optMenu.add(helpAction);
        optMenu.add(logoutAction);

        addfAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                BuildAddFriendWindow();


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

        mainWindow.setJMenuBar(menuBar);


        //window building

        mainWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                LogoutRequest();

            }
        });

        mainWindow.setSize(310, 500);
        mainWindow.setTitle("Hype");
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setMinimumSize(new Dimension(250, 250));
        
        // Online users list

        onlineScroller
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        onlineScroller
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        onlineScroller.setViewportView(onlineUsers);
        mainWindow.add(onlineScroller, BorderLayout.CENTER);
        onlineScroller.setMinimumSize(new Dimension(10, 10));

        onlineUsers.setCellRenderer(new FriendsCellRenderer());
        
        //init top bar
       
        usernameBar.setBackground(createImageIcon("topbg.png", "top bar bg").getImage());
        usernameBar.setMinimumSize(new Dimension(50, 100));
        mainWindow.add(usernameBar, BorderLayout.NORTH);
        BorderLayout bgPanLayout = new BorderLayout();
        usernameBar.setLayout(bgPanLayout);
        
        usernameBar.add(userAvatar, BorderLayout.WEST);
        userAvatar.setIcon(createImageIcon("hype.png", "user icon"));
        userAvatar.setSize(48, 48);
        userAvatar.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        
        namePanel.setOpaque(false);
        namePanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        BoxLayout nameLayout = new BoxLayout(namePanel, BoxLayout.Y_AXIS);
        namePanel.setLayout(nameLayout);
        usernameBar.add(namePanel, BorderLayout.CENTER);
        
        usernameLabel.setText(username);
        usernameLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        namePanel.add(usernameLabel);
        
        
        onlineText.setText("online");
        onlineText.setForeground(Color.GRAY);
        namePanel.add(onlineText);
        
        //end init top bar

        popupMenu = new JPopupMenu();
        JMenuItem removeFriendItem = new JMenuItem("Remove Friend");
        removeFriendItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                if (deleteTarget != null) DeleteFriendRequest(deleteTarget);
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

                JList list = (JList) evt.getSource();
                int index = list.locationToIndex(evt.getPoint());
                FriendListItem item = friendListItems[index];

                if (item.mode == FriendListItemMode.FRIEND) {
                    deleteTarget = item.text;
                    popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }

            public void mouseClicked(MouseEvent evt) {
                if (friendListItems == null) return;

                JList list = (JList) evt.getSource();
                int index = list.locationToIndex(evt.getPoint());
                //here we'll want to initiate a call if the user is online
                FriendListItem item = friendListItems[index];

                if (item.mode == FriendListItemMode.REQUEST && evt.getClickCount() == 1) { //click friend request (accept/decline)
                    int x = evt.getPoint().x - list.getLocation().x;
                    if (list.getWidth() - x < 26) {
                        AddFriendRequest(item.text);
                        System.out.println("accept");
                    } else if (list.getWidth() - x < 46) {
                        DeleteFriendRequest(item.text);
                        System.out.println("decline");
                    }
                } else if (item.mode == FriendListItemMode.FRIEND && item.status == 1 && evt.getClickCount() == 2) { //double click friend (call)
                    CallRequest(item.text);
                    System.out.println("double clicked: " + item.text);
                }
            }
        });

    }

    public static void BuildAddFriendWindow() {
        addFriendWindow = new JFrame();
        addFriendWindow.setIconImage(frameIco.getImage());
        addFriendWindow.setTitle("Add Friend");
        addFriendWindow.setResizable(false);
        addFriendWindow.setLayout(null);
        addFriendWindow.getContentPane().setPreferredSize(new Dimension(300, 50));
        addFriendWindow.pack();
        //addFriendWindow.setSize(310, 80);
        addFriendWindow.setLocationRelativeTo(null);


        addFriendLabel.setText("Username:");
        addFriendWindow.getContentPane().add(addFriendLabel);
        addFriendLabel.setBounds(5, 5, 100, 20);

        addFriendField.setText("");
        addFriendWindow.getContentPane().add(addFriendField);
        addFriendField.setBounds(105, 5, 200, 20);

        addFriendButton.setText("Add");
        addFriendWindow.getContentPane().add(addFriendButton);
        addFriendButton.setBounds(105, 30, 80, 20);
        if ((addFriendButton.getActionListeners().length == 0)) {
            addFriendButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent event) {
                    AddFriendRequest(addFriendField.getText().trim());
                }
            });
        }
        addFriendWindow.setVisible(true);


    }


    public static void BuildCallInqWindow() {

        callinqWindow.setTitle("Incoming call");
        callinqWindow.setLayout(null);
        callinqWindow.setResizable(false);
        callinqWindow.setSize(310, 300);
        callinqWindow.setLocationRelativeTo(null);

        callmsgLabel.setText("You have an incoming call from ");
        callmsgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        callmsgLabel.setBounds(55, 100, 200, 20);
        callinqWindow.getContentPane().add(callmsgLabel);


        usercallingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        usercallingLabel.setBounds(55, 125, 200, 20);
        callinqWindow.getContentPane().add(usercallingLabel);


        acceptCallButton.setText("Accept");
        acceptCallButton.setHorizontalAlignment(SwingConstants.CENTER);
        callinqWindow.getContentPane().add(acceptCallButton);
        acceptCallButton.setBounds(20, 220, 80, 20);
        if ((acceptCallButton.getActionListeners().length == 0)) {
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
        if ((declineCallButton.getActionListeners().length == 0)) {
            declineCallButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent event) {
                    RejectCallResponse();
                }
            });
        }


        callinqWindow.setVisible(true);


    }


    public static void BuildCallInProgWindow() {

        callinprogWindow = new JFrame();

        callinprogWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                EndCallRequest();
            }
        });

        callinprogWindow.setIconImage(frameIco.getImage());
        callinprogWindow.setTitle("VOIP Call ");
        callinprogWindow.setLayout(null);
        callinprogWindow.setSize(310, 300);
        callinprogWindow.setResizable(false);
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
        if ((stopCallButton.getActionListeners().length == 0)) {
            stopCallButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent event) {
                    EndCallRequest();
                }
            });
        }

        callinprogWindow.setVisible(false);

    }


    public static void Connect() throws IOException {


        //hostname = hostField.getText().trim();
        //port = Integer.parseInt(portField.getText().trim());

        //hostname = "localhost";
        port = 9991;

        client = new SocketHandler(hostname, port);
        if (client.startConnection()) {
            System.out.println("Connection established!");
    
            //start sending
           keepAliveThread = new KeepAliveThread(client.getSocket());
           keepAliveThread.start();

            clientThread = new ClientThread(client);
            clientThread.start();

        }
		/*catch (Exception e)
		{
			System.out.println("Error connecting from client!");
			JOptionPane.showMessageDialog(null, "Server not responding!");
			e.printStackTrace();
			System.exit(0);
		}*/

    }
    
    public static void disconnect(){
        JOptionPane.showMessageDialog(null, "You have been disconnected from the server. System will exit.");
        loginWindow.dispose();
        keepAliveThread.interrupt();
        clientThread.interrupt();
        System.exit(0);
    }

    public static void LoginRequest() {

        if (!usernameField.getText().equals("") || !passwordField.getText().equals("")) {

            username = usernameField.getText().trim();
            password = passwordField.getText().trim();

            client.sendLogInRequest(username, password);
            System.out.println("Message for LOG IN: ");
            System.out.println("Log in request sent from client");


        } else {
            JOptionPane.showMessageDialog(null, "Invalid username or password!");
        }


    }

    public static void RegisterRequest() {


        if (!usernameField.getText().equals("") || !passwordField.getText().equals("")) {
            username = usernameField.getText().trim();
            password = passwordField.getText().trim();

            client.sendRegisterRequest(username, password);
            System.out.println("Message for REGISTER: ");
            System.out.println("Register request sent from client");


        } else {
            JOptionPane.showMessageDialog(null, "Enter an username and a password!");
        }


    }

    public static void LogoutRequest() {

        client.sendLogOutRequest();
        System.out.println("Logout request sent");
        clientThread.interrupt();
        keepAliveThread.interrupt();
    }

    public static void CallRequest(String target) {

        if (!target.equals("")) {
            String usercalled = target.trim();
            client.sendCallRequest(usercalled);
            System.out.println("Message for CALL: ");
            System.out.println("Call request sent");
            callerUserLabel.setText(usercalled);
            BuildCallInProgWindow();
            callinprogWindow.setVisible(true);


        } else {
            JOptionPane.showMessageDialog(null, "Enter user to call.");
        }

    }


    public static void EndCallRequest() {

        client.sendEndCallRequest();
        callinprogWindow.dispose();
        callstateLabel.setText("Disconnected!");
        endCall();


    }

    public static void AcceptCallResponse() {

        client.sendCallResponse(true);
        System.out.println("Call was accepted");
        callinqWindow.dispose();
        BuildCallInProgWindow();
        callinprogWindow.setVisible(true);


    }

    public static void RejectCallResponse() {

        client.sendCallResponse(false);
        System.out.println("Call was rejected");
        callinqWindow.dispose();


    }


    public static void startCall(String ip, int port, int callid) {

        System.out.println("Call started!");
        System.out.println(callid);
        play = new SimpleVoIPCall();
        play.start(ip, port, callid);


    }


    public static void endCall() {
        System.out.println("Call fucking ended!");
        if (play != null) play.stop();
    }


    public static void FriendListRequest() {


        client.sendFriendListRequest();
    }

    public static void AddFriendRequest(String username) {
        if (!username.equals("")) {
            client.sendAddFriendRequest(username);
            System.out.println("AddFriend request sent: " + username);
            addFriendField.setText("");
            addFriendWindow.dispose();

        } else {
            JOptionPane.showMessageDialog(null, "Enter username to add.");

        }

    }


    public static void DeleteFriendRequest(String username) {
        client.sendDeleteFriendRequest(username);

    }




    protected static ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = GuiMainClient.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }


}
