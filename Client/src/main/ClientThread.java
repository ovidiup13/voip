package main;

import buffers.ServerResponse.Response;
import com.google.protobuf.ProtocolStringList;
import p2p.SimpleVoIPCall;
import tcp.sockethandler.SocketHandler;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;



public class ClientThread extends Thread {
	
	private static Socket recSocket; 
	Response response;
	String recUser = "";
	public SimpleVoIPCall play;

	
	public ClientThread(SocketHandler sockethandler ){
		recSocket = sockethandler.getSocket();
	}
	
	
	public void run() {
		while(true) {
			try {
				response = Response.parseDelimitedFrom(recSocket.getInputStream());
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
                    case FRES: { AcceptedDeclineFrindRequestResponse(response); break;}
                    default:
                        break;
                }
			} catch (IOException | NullPointerException e) {
                GuiMainClient.disconnect();
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
			SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
			    	GuiMainClient.loginWindow.setVisible(false);
					GuiMainClient.BuildMainWindow(GuiMainClient.getUsername());
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
            GuiMainClient.beingCalledSound.close();
            GuiMainClient.callingSound.close();
			System.exit(0);
        }
		else
			System.err.println("Client: Connection error, could not close connection");

	}
	

	
	//call request received
	private void callinqResponse(Response response){
        	recUser = response.getUsername();
        	SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
			    	GuiMainClient.callerUserLabel.setText(recUser);
			    	GuiMainClient.usercallingLabel.setText(recUser);
			    	GuiMainClient.BuildCallInqWindow();
                    GuiMainClient.beingCalledSound.flush();
			    	GuiMainClient.beingCalledSound.start();
			    }
			  });
        }
        
	
	
	//sent call request accepted
	private void callrecResponse (final Response response){
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	
		    	GuiMainClient.callstateLabel.setText("Connected");
		    	GuiMainClient.imageLabel.setVisible(true);
		    	
		    	if (GuiMainClient.callingSound.isRunning())
		    	{ GuiMainClient.callingSound.stop();
		    		GuiMainClient.callingSound.flush();}
		    	
		    	
		    	GuiMainClient.startCall(response.getCallResponse().getIpAddress(), 12345, response.getCallResponse().getCallID());
		    	
		    }
		  });
		
	
	}
	
	//sent call request rejected
	private void nocallResponse (Response response){

		SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                if (GuiMainClient.callingSound.isRunning()) {
                    GuiMainClient.callingSound.stop();
                    GuiMainClient.callingSound.flush();
                }

                GuiMainClient.callinprogWindow.dispose();

            }
        });
	
	}
	
	//call ended
	private void endcallResponse(Response response) {
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	
		    	GuiMainClient.endCall();
		    	GuiMainClient.callinprogWindow.dispose();
		    	GuiMainClient.callinqWindow.dispose();
		    	
		    	
		    	
		    	 if (GuiMainClient.beingCalledSound.isRunning()){
		     		GuiMainClient.beingCalledSound.stop();
		     		GuiMainClient.beingCalledSound.flush();}
		    	
		    }   	 
		  });
	
	}
	
	//friend list 
	private void displayflistResponse(final Response response){
		
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	//convert response into array of FriendListItems
		    	//what this SHOULD do in future is add all the friends, then add a title with text "Pending Requests",
		    	//then add the pending users, with pending set to true.
		    	ArrayList<FriendListItem> friends = new ArrayList<FriendListItem>();
                ArrayList<FriendListItem> pending = new ArrayList<FriendListItem>();
		    	
                friends.add(new FriendListItem(FriendListItemMode.TITLE, "Friends:", 0,""));
                
		    	ProtocolStringList usernames = response.getList().getUsernameList();

		    	for (int i = 0; i<usernames.size(); i++) {
                    //get data
                    String friend = usernames.get(i++);
                    int status = Integer.parseInt(usernames.get(i++));
                    String lastSeen = usernames.get(i++);
                    int onlineStatus = Integer.parseInt(usernames.get(i));

                    if(onlineStatus < 2) onlineStatus++;
                    
                    if(status == 2)
                        friends.add(new FriendListItem(FriendListItemMode.FRIEND, friend, onlineStatus,lastSeen));
                    else
                        pending.add(new FriendListItem(FriendListItemMode.REQUEST, friend, onlineStatus,lastSeen));

		    	}
		    	
		    	if (pending.size() > 0) friends.add(new FriendListItem(FriendListItemMode.TITLE, "Pending Requests:", 0,""));

                friends.addAll(pending);
		    	
		    	GuiMainClient.friendListItems = friends.toArray(new FriendListItem[friends.size()]);
		    	GuiMainClient.onlineUsers.setListData(GuiMainClient.friendListItems);
		    	
		    }
		  });

	}
	
	//add friend response
	private void addfriendResponse (Response response){
			if (response.getReqResult().getOk())
				System.out.println("You action  to user "+response.getReqResult().getCause()+" was successul ");
			else{
				System.out.println("You action  to user "+response.getReqResult().getCause()+" was unsuccesful ");
			}
		}
	
	
	private void AcceptedDeclineFrindRequestResponse(Response response2) {
		new FriendRequestResponseNotificaion(response.getUsername(), response.getStatus());
	
		
	}
	
	
	//delete friend
	private void deletefriendResponse(Response response){
		
	}

}
