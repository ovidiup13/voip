package main;

import database.ConnectToDb;
import database.IPAddressMap;
import tcp.clienthandler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;

public class MainServer {

    private static final int port = 9991;

    public static void main(String[] args) {
        boolean listening = true;

        try {
            System.out.println("Starting server...");

            //initialize socket, cache and db
            ServerSocket serverSocket = new ServerSocket(port);
            ConnectToDb db = new ConnectToDb();
            db.makeConnection();
            IPAddressMap ClientMap = new IPAddressMap();
            while (listening) {
                System.out.println("Listening for connections...");
                new Thread(new ClientHandler(serverSocket.accept(), ClientMap, db)).start();
            }
            serverSocket.close();
            db.closeEverything();
        } catch (IOException e) {
            System.err.println("Server: could not init socket");
        }
    }

}
