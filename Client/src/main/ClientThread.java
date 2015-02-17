package main;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import tcp.sockethandler.*;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import p2p.SimpleVoIPCall;
import buffers.ServerResponse.Response;



public class ClientThread extends Thread {
	
	private static Socket recSocket; 
	Response response;
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
				
				//register response
				if(type.equals(Response.ResType.REG) ){
					
					if (response.getReqResult().getOk()){
						JOptionPane.showMessageDialog(null, "Registration successful!");
					}
					else {
						JOptionPane.showMessageDialog(null, "Registration unsuccesful!");
					}
				}
				
				
				//login response 
				else if (type.equals(Response.ResType.LIN)){
					
					if( response.getReqResult().getOk()){
						//JOptionPane.showMessageDialog(null, "Login successful!");
						System.out.println("Login successul!");
						
						SwingUtilities.invokeLater(new Runnable() {
						    public void run() {
						    	GuiMainClient.loginWindow.setVisible(false);
								GuiMainClient.BuildMainWindow();
								GuiMainClient.mainWindow.setTitle("VOIP-User: " + GuiMainClient.getUsername());
								GuiMainClient.logoutButton.setEnabled(true);
						    }
						  });
						
					}
					else {
						JOptionPane.showMessageDialog(null, "Login unsuccessful!");
						
					}
				}
				
				//logout response
				else if (type.equals(Response.ResType.LOUT)){
					
					//close connection
					if(response.getReqResult().getOk()){
						System.out.println("Connection closed.");
						System.exit(0);}
					else
						System.err.println("Client: Connection error, could not close connection");
			
				}
				
			
				
				//call request received
				else if (type.equals(Response.ResType.CALLINQ) ) {   
		
					//Accept with CALLRES.TRUE
					//reject with CALLRES.FALSE
					
					String prompt = "Accept call from " + response.getUsername() + "?";
					int reply = JOptionPane.showConfirmDialog(null, prompt, "Call confirmation", JOptionPane.YES_NO_OPTION);
			        if (reply == JOptionPane.YES_OPTION)
			        {				        	
			        	
			        	System.out.println("Call accepted request sent!");
			        	
			        	SwingUtilities.invokeLater(new Runnable() {
						    public void run() {
						    	GuiMainClient.AcceptCallResponse();
						    	
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
				else if (type.equals(Response.ResType.CALLREC)){
					
					System.out.println("Call was accepted. Connection can begin for "+ response.getUsername() );
					
					SwingUtilities.invokeLater(new Runnable() {
					    public void run() {
					    	GuiMainClient.BuildCallInProgWindow();
					    	GuiMainClient.callinprogWindow.setVisible(true);
					    	GuiMainClient.callerUserLabel.setText("You are in a call with "+ response.getUsername());
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
				else if (type.equals(Response.ResType.NOCALL)){
					System.out.println("Call was rejected");
					JOptionPane.showMessageDialog(null, "Call was rejected!");
					
				}
				
				//call ended
				else if(type.equals(Response.ResType.ECALL)) {
					
					System.out.println("End call request received from partner!");
					
					SwingUtilities.invokeLater(new Runnable() {
					    public void run() {
					    	// response needs username field to work
					    	GuiMainClient.callerUserLabel.setText("Call ended by user:" +  response.getUsername());
					    }
					  });
					// doesn't work from both sides
					play.stop();
						
				//friend list 
				}
				else if(type.equals(Response.ResType.FLIST)){
					
					
				}
				//add friend response
				else if (type.equals(Response.ResType.ADDF)){
					
				}
				
				//delete friend
				else if(type.equals(Response.ResType.DELF)){
					
				}
				

				
			}
			
			
			
			
			
			
			
		}
			
			
			
			
			
			
			
			
			
			
			
			
			

		

		
		
		
		
		
		

	}

}
