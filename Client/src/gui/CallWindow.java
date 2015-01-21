package gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/*This is the window that is invoked when the user selects a contact to call
  Simple interface, unfinished, needs to be integrated to the system as we need
  the callee's status (online, busy, etc) to determine if the user can make the call*/


public class CallWindow extends JFrame implements ActionListener{
    JPanel myPanel; 
    JPanel myPanel2;
    JPanel backPanel;
    JButton call;
    JButton endCall;
    JLabel myLabel;
    ImageIcon image1;
  
    
    
    private static void createGUI() {
        //Create and set up the window.
        CallWindow first = new CallWindow();
        first.setTitle("Call");
        first.setSize(300, 400);
        first.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
    
    public CallWindow(){
        backPanel = new JPanel();
        backPanel.setLayout(new GridLayout(2, 1, 0, 0));
        call = new JButton("Call");
        call.addActionListener(this);
        endCall = new JButton("End Call");
        endCall.addActionListener(this);
        image1 = new ImageIcon(getClass().getResource("image2.png"));
        myLabel = new JLabel(image1);
        myLabel.setPreferredSize(new Dimension(400, 400));
 
        myPanel = new JPanel();
        myPanel.setLayout(new GridLayout(1, 0, 0, 0));
        myPanel2 = new JPanel();
        myPanel2.setLayout(new FlowLayout(FlowLayout.CENTER));
        myPanel2.add(call);
        myPanel2.add(endCall);
  
        myPanel.add(myLabel);
        backPanel.add(myPanel);
        backPanel.add(myPanel2);
       
        this.add(backPanel);
       
    }
        
    @Override
    public void actionPerformed(ActionEvent event){
        if (event.getSource() == call){
            //make call
        }
        else if (event.getSource() == endCall){
            System.exit(0);
        }
    
    }
    
    
}