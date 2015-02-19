package main;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import tcp.sockethandler.*;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import p2p.SimpleVoIPCall;
import buffers.ServerResponse.Response;



public class ClientThread extends Thread {
	
	private static Socket recSocket; 
	Response response;
	String recUser = "";
	public SimpleVoIPCall play;

	
	public ClientThread(SocketHandler sockethandler ){
		recSocket = sockethandler.getSocket();
		System.out.println("Thread Started!");
		

	}
	
	
	public void run() {
		while(true) {
			try {
				response = Response.parseDelimitedFrom(recSocket.getInputStream());
			} catch (IOException e) {
				System.err.println("SocketHandler: failed to open input stream");
			}
			
			finally  {
				
				Response.ResType type = response.getResType();
				
				
				switch(type){
				
				case REG: { registerResponse(response);break;}
				case LIN: { loginResponse(response); break;}
				case LOUT: { logoutResponse(response); break;}
				case CALLINQ: { callinqResponse(response); break;}
				case CALLREC: { callrecResponse(response); break;}
				case NOCALL: { nocallResponse(response); break;}
				case ECALL: { endcallResponse(response); break;}
				case FLIST: { displayflistResponse(response); break;}
				case ADDF: { addfriendResponse(response);break;}
				case DELF: { deletefriendResponse(response); break;}
				default:
					break;
				
				
				}
				
			}
			
	
		}
			

	}
	
	
	//register response
	private void registerResponse(Response response){
		
		if (response.getReqResult().getOk()){
			JOptionPane.showMessageDialog(null, "Registration successful!");
		}
		else {
			JOptionPane.showMessageDialog(null, "Registration unsuccesful!");
		}
	}
	
	
	//login response 
	private void loginResponse(Response response){
		
		if( response.getReqResult().getOk()){
			//JOptionPane.showMessageDialog(null, "Login successful!");
			System.out.println("Login successul!");
			
			SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
			    	GuiMainClient.loginWindow.setVisible(false);
					GuiMainClient.BuildMainWindow();
					GuiMainClient.mainWindow.setTitle("VOIP-User: " + GuiMainClient.getUsername());
					GuiMainClient.logoutButton.setEnabled(true);
					GuiMainClient.FriendListRequest();
			    }
			  });
			
		}
		else {
			JOptionPane.showMessageDialog(null, "Login unsuccessful!");
			
		}
	}
	
	//logout response
	private void logoutResponse (Response response){
		
		//close connection
		if(response.getReqResult().getOk()){
			System.out.println("Connection closed.");
			System.exit(0);}
		else
			System.err.println("Client: Connection error, could not close connection");

	}
	

	
	//call request received
	
	
	private void callinqResponse(Response response){   

		String prompt = "Accept call from " + response.getUsername() + "?";
		int reply = JOptionPane.showConfirmDialog(null, prompt, "Call confirmation", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION)
        {				        	
        	recUser = response.getUsername();
        	System.out.println("Call accepted request sent!");
        	SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
			    	GuiMainClient.AcceptCallResponse();
			    	GuiMainClient.BuildCallInProgWindow();
			    	GuiMainClient.callinprogWindow.setVisible(true);
			    	GuiMainClient.callerUserLabel.setText(recUser);
			    }
			  });
        }
        else 
        	
        {
        	System.out.println("Call reject request was sent!");
        	SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
			    	GuiMainClient.RejectCallResponse();
			    }
			  });
        	
        }
		
	}
	
	
	
	
	//sent call request accepted
	private void callrecResponse (Response response){
		
		System.out.println("Call was accepted. Connection can begin for "+ recUser );
		
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	
		    	
		    	GuiMainClient.callstateLabel.setText("Connected");
		    	GuiMainClient.imageLabel.setVisible(true);
		    }
		  });
		
		
		play = new SimpleVoIPCall();
        play.start(
                response.getCallResponse().getIpAddress(),
                12345,
                response.getCallResponse().getCallID()
        );
		
		
	}
	
	//sent call request rejected
	private void nocallResponse (Response response){
		System.out.println("Call was rejected");
		JOptionPane.showMessageDialog(null, "Call was rejected!");
		
	}
	
	//call ended
	private void endcallResponse(Response response) {
		
		System.out.println("End call request received from partner!");
		
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	GuiMainClient.callinprogWindow.dispose();
		    }
		  });
		// doesn't work from both sides
		play.stop();
			
	//friend list 
	}
	

	private void displayflistResponse(final Response response){
		
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	
		    	GuiMainClient.onlineUsers.setListData( (response.getList().getUsernameList()).toArray());
		    }
		  });

	}
	
	//add friend response
	private void addfriendResponse (Response response){
		
	}
	
	//delete friend
	private void deletefriendResponse(Response response){
		
	}
	
	
	
	

}
