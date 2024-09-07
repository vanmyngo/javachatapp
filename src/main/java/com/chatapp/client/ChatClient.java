package com.chatapp.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatClient {
    private Socket socket = null;
    private BufferedReader inputConsole = null;
    private PrintWriter out = null;
    private BufferedReader in = null;

    public ChatClient(String ipAddress, int port) throws Exception {
        try {
            // Connect to server
            socket = new Socket(ipAddress, port);
            Logger.getLogger(ChatClient.class.getName()).log(Level.INFO, "Client connected to server: " + ipAddress);

            // Sending and receiving message from server
            inputConsole = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(socket.getOutputStream(), true);
            String message = "";

            // Client runs until user input "exit"
            while (!message.equals("exit")) {
                // Read message from user's console
                message = inputConsole.readLine();

                // Send message to server
                out.println(message);

                // Print incoming message from server
                System.out.println(in.readLine());
            }
        } catch (Exception e) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, e.getMessage());
            throw e;
        } finally {
            // Close all connections
            socket.close();
            inputConsole.close();
            out.close();
        }
    }

    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient("127.0.0.1", 5000);
    }
}
