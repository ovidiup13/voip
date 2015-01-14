package p2p;

import java.awt.event.*;

import javax.swing.*;

public class TestProgram extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5659204030938202053L;
	private JLabel label;
	
	public TestProgram() throws Exception {
        setSize(256, 256);
        setTitle("SimpleVoIPCall");
        setVisible(true);
        
        label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        this.add(label);
        
        label.setText("Awaiting information...");
        
        
        String IP = JOptionPane.showInputDialog(this, "Enter the IP of the other participant in the call");
        int port = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter the port the call will be on. (eg. 12345 is usually safe)"));
        int callID = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter the Call ID. (must be the same on both systems!)"));
        
        addWindowListener(new WindowCloser());
        SimpleVoIPCall play = new SimpleVoIPCall();
        play.start(
        		IP,
        		port, 
        		callID
        );
        
        label.setText("<html>Attempting to stream audio.<br><br>"+"IP Address: "+IP+"<br>Port: "+port+"<br>callID: "+callID);
	}
	
    private class WindowCloser extends WindowAdapter {
        public void windowClosing(WindowEvent event) {
            System.exit(0);
        }
    }
    
    public void updateLabel(String text) {
        label.setText("Bytes recieved in last input buffer:"+text);
    }
	
	public static void main(String args[]) throws Exception {
		TestProgram frame = new TestProgram();
	}
}