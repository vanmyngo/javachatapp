package com.chatapp.backend.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServer {
    // List to keep track of all connected clients
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        // Start server on port 5000
        ServerSocket serverSocket = new ServerSocket(5000);
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Server started");

        // Waiting for clients
        while (true) {
            // Connect clients
            Socket clientSocket = serverSocket.accept();
            Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Client connected: " + clientSocket);

            // New thread for each client
            ClientHandler clientThread = new ClientHandler(clientSocket, clients);
            clients.add(clientThread);
            new Thread(clientThread).start();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private List<ClientHandler> clients;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * Constructor
     * @param socket Connected client socket
     * @param clients List of connected client
     * @throws Exception
     */
    public  ClientHandler(Socket socket, List<ClientHandler> clients) throws Exception {
        this.clientSocket = socket;
        this.clients = clients;
        this.out = new PrintWriter(clientSocket.getOutputStream(),true);
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    /**
     * Run server and be on standby for incoming message from connected client
     */
    public void run() {
        String message;
        try {
            // When receiving message from client
            while ((message = in.readLine()) != null) {
                // Broadcast message to all clients
                for (ClientHandler client : clients) {
                    client.out.println(message);
                }
            }
        } catch (SocketException socketException) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.INFO, "Disconnected client: " + clientSocket);
        } catch (Exception e) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.INFO, e.getMessage());
        } finally {
            try {
                // Close all connections
                in.close();
                out.close();
                clientSocket.close();
            } catch (Exception e) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, e.getMessage());
            }
        }
    }
}