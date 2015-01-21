package gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.*;
import java.awt.event.*;

/*Start screen for VoIP application
  Image stolen from internet, don't judge
  Last Updated: 20/01/2015 */




public class Start extends JFrame implements ActionListener{
 
    Home home = new Home();
    JPanel myPanel;
    JPanel panel2;
    JPanel backPanel;
    JLabel myLabel;
    ImageIcon image1;
    JButton login;
    JButton register;
    JButton OK;
    JButton cancel;
    
    private static void createGUI() {
        //Create and set up the window.
        Start first = new Start();
        first.setTitle("VoIP");
        first.setSize(500, 500);
        first.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        first.setVisible(true);
     
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createGUI();
            }
        });
    }
    
    public Start(){
        
        image1 = new ImageIcon(getClass().getResource("voip2.jpg"));
        myLabel = new JLabel(image1);
        myPanel = new JPanel();
        myPanel.add(myLabel);
        
        login = new JButton("Login");
        login.addActionListener(this);
        register = new JButton("Register");
        register.addActionListener(this);
        panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel2.add(login);
        panel2.add(register);
        
        
        backPanel = new JPanel();
        backPanel.setLayout(new GridLayout(2, 1, 0, 0));
        backPanel.add(myPanel);
        backPanel.add(panel2);
        this.add(backPanel);
         
    }
    
    
    @Override
    public void actionPerformed(ActionEvent event){
        //When login button is clicked, login method
        //from class LoginBox is called
        
        if (event.getSource() == login){
            LoginBox login = new LoginBox();
            login.login();
        }
        //Ditto for register button
        else if (event.getSource() == register){
                RegisterBox register = new RegisterBox();
                register.register();
        
        }
        
    }    
    
    class LoginBox implements ActionListener{
        //Creates a popup window, allowing user to login to system
        
        JFrame loginScreen = new JFrame();
        public void login(){
            
            loginScreen.setTitle("Login");
            loginScreen.setSize(300, 200);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            loginScreen.setVisible(true);
         
            JTextField username = new JTextField("Username");
            JPasswordField password = new JPasswordField("Password");
            OK = new JButton("OK");
            OK.addActionListener(this);
            cancel = new JButton("Cancel");
            cancel.addActionListener(this);
            JPanel panel1 = new JPanel();
            JPanel panel3 = new JPanel();
            JPanel panel4 = new JPanel();
         
            panel4.setLayout(new GridLayout(2, 1, 0, 0));
            panel3.setLayout(new FlowLayout(FlowLayout.CENTER));
            panel1.setLayout(new FlowLayout(FlowLayout.CENTER));
         
            panel1.add(username);
            panel1.add(password);
            panel3.add(OK);
            panel3.add(cancel);
            panel4.add(panel1);
            panel4.add(panel3);
            loginScreen.add(panel4); 
        }
        
        @Override
        public void actionPerformed(ActionEvent event){
            //Once details are entered and OK button selected,
            //(username and passowrd verification methods will be added asap)
            //user is taken to the "Home" screen
            
            if (event.getSource() == OK){
                home.setVisible(true);
                loginScreen.setVisible(false);
            }
            else if (event.getSource() == cancel){
                System.exit(0);
            }
        
        }
    
    
    }

    
    class RegisterBox implements ActionListener{
        //Ditto for registration
        
        JFrame register = new JFrame();
        public void register(){
            
            register.setTitle("Register");
            register.setSize(300, 200);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            register.setVisible(true);
    
            JTextField username = new JTextField("Username");
            JPasswordField password = new JPasswordField("Password");
            OK = new JButton("OK");
            OK.addActionListener(this);
            cancel = new JButton("Cancel");
            cancel.addActionListener(this);
            JPanel panel1 = new JPanel();
            JPanel panel3 = new JPanel();
            JPanel panel4 = new JPanel();
         
            panel4.setLayout(new GridLayout(2, 1, 0, 0));
            panel3.setLayout(new FlowLayout(FlowLayout.CENTER));
            panel1.setLayout(new FlowLayout(FlowLayout.CENTER));
         
            panel1.add(username);
            panel1.add(password);
            panel3.add(OK);
            panel3.add(cancel);
            panel4.add(panel1);
            panel4.add(panel3);
            register.add(panel4);
        }
        
        @Override
        public void actionPerformed(ActionEvent event){
            if(event.getSource() == OK){
                home.setVisible(true);
                register.setVisible(false);
            }
            else if (event.getSource() == cancel){
                System.exit(0);
            }
        
        }
    
    
    }
    
    
    
}