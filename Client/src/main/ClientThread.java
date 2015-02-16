package main;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import tcp.sockethandler.*;
import javax.swing.JOptionPane;

import buffers.ServerResponse.Response;



public class ClientThread extends Thread {
	
	private static Socket recSocket; 
	Response response;

	
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
					
					
				}
				
				//logout response
				else if (type.equals(Response.ResType.LOUT)){
					
					
					
				}
				
			
				
				//call request received
				else if (type.equals(Response.ResType.CALLINQ) ) {   
		
					//Accept with CALLRES.TRUE
					//reject with CALLRES.FALSE
					
				}
				
				//sent call request accepted
				else if (type.equals(Response.ResType.CALLREC)){
					
					
					
				}
				
				//sent call request rejected
				else if (type.equals(Response.ResType.NOCALL)){
					
					
				}
				
				//call ended
				else if(type.equals(Response.ResType.ECALL)) {
					
					
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
