package server;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private ServerSocket serverSocket;
	private int port;

	public Server(int port){
		this.port = port;
	}

	public void start() throws IOException {
		System.out.println("Server: Initialising server...");
		serverSocket = new ServerSocket(port);
		System.out.println("Server: Server started.");
		
		while(true){
		
		System.out.println("Server: Awaiting client...");
		Socket client = serverSocket.accept();
		
		System.out.println("Server: Client found, sending message...");
		new Thread(new ClientHandler(client));
		}
	}

}

