package com.chatapp.backend.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Consumer<String> onMessageReceived;

    public ChatClient(String serverAddress,
                      int serverPort,
                      Consumer<String> onMessageReceived)
            throws Exception {
        // Connect to server
        this.socket = new Socket(serverAddress, serverPort);
        Logger.getLogger(ChatClient.class.getName()).log(Level.INFO, "Client connected to server: " + serverAddress);

        // Sending and receiving message from server
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.onMessageReceived = onMessageReceived;
    }

    /**
     * Send user input message to server
     * @param message Message from user
     */
    public void sendMessage(String message) {
        out.println(message);
    }

    /**
     * Start client and continue catching any incoming message from server
     */
    public void startClient() {
        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    onMessageReceived.accept(line);
                }
            } catch (Exception e) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, e.getMessage());
            }
        }).start();
    }
}
