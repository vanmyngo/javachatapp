package com.chatapp.frontend;

import com.chatapp.backend.client.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;

public class ChatClientGUI extends JFrame {
    private JTextArea messageArea;
    private JTextField textField;
    private ChatClient client;
    private JButton exitButton;
    private JPanel bottomPanel;

    /**
     * Constructor
     */
    public ChatClientGUI() {
        // Set up chat window
        super("Chat Application");
        setSize(400,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Variables
        String serverAddress = "127.0.0.1";
        int serverPort = 5000;

        // Styling
        Color backgroundColor = new Color(171, 110, 65);
        Color buttonColor = new Color(43, 43, 39);
        Color textColor = new Color(250, 250, 250);
        Font textFont = new Font("Courier New", Font.PLAIN, 14);
        Font buttonFont = new Font("Courier New", Font.BOLD, 12);

        // Set up message window
        messageArea = new JTextArea();
        setMessageArea(backgroundColor, textColor, textFont);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        add(scrollPane, BorderLayout.CENTER);

        // Prompt for user name
        String name = JOptionPane.showInputDialog(this,
                "Enter your name: ",
                "Name Entry",
                JOptionPane.PLAIN_MESSAGE);
        // Change chat window to include user name
        this.setTitle("Chat Application - " + name);

        // Set up input text area
        textField = new JTextField();
        setTextField(backgroundColor, textColor, textFont);
        textField.addActionListener(e -> {
            // Format for displayed message
            String message = String.format("[%s] %s: %s",
                    new SimpleDateFormat("HH:mm:ss").format(new Date()),
                    name,
                    textField.getText());

            // Send message to server
            client.sendMessage(message);

            // Text field cleared and ready for next message
            textField.setText("");
        });
        add(textField, BorderLayout.SOUTH);

        // Set up exit button
        // When clicked, exit window (Status = 0/normal exit)
        exitButton = new JButton("Exit");
        setExitButton(buttonColor, buttonFont);
        exitButton.addActionListener(_ -> {
            // Send departure message to server
            String departureMessage = name + " has left chat.";
            client.sendMessage(departureMessage);

            // Wait for message to send before exiting
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }

            // Exit application
            System.exit(0);
        });

        // Panel to hold text area and exit button
        bottomPanel = new JPanel(new BorderLayout());
        setBottomPanel(backgroundColor);

        // Initialize and start ChatClient
        try {
            this.client = new ChatClient(serverAddress, serverPort, this::onMessageReceived);
            client.startClient();
        } catch (Exception e) {
            Logger.getLogger(ChatClientGUI.class.getName()).log(Level.SEVERE, e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Error connecting to server",
                    "Connection error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    /**
     * Print message in message area
     * @param message Message to be displayed
     */
    private void onMessageReceived(String message) {
        SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));
    }

    /**
     * Initial setup for message area
     * @param backgroundColor Background color for message area
     * @param textColor Text color for message area
     * @param textFont Text font for message area
     */
    private void setMessageArea(Color backgroundColor, Color textColor, Font textFont) {
        messageArea.setEditable(false);
        setMessageAreaColor(backgroundColor, textColor);
        setMessageAreaFont(textFont);
    }

    /**
     * Set background and text color for message area
     * @param backgroundColor Background color for message area
     * @param textColor Text color for message area
     */
    public void setMessageAreaColor(Color backgroundColor, Color textColor) {
        messageArea.setBackground(backgroundColor);
        messageArea.setForeground(textColor);
    }

    /**
     * Set text font for message area
     * @param textFont Text font for message area
     */
    public void setMessageAreaFont(Font textFont) {
        messageArea.setFont(textFont);
    }

    /**
     * Initial setup for text field
     * @param backgroundColor Background color for text field
     * @param textColor Text color for text field
     * @param textFont Text font for text field
     */
    private void setTextField(Color backgroundColor, Color textColor, Font textFont) {
        setTextFieldColor(backgroundColor, textColor);
        setTextFieldFont(textFont);
    }

    /**
     * Set background and text color for text field
     * @param backgroundColor Background color for text field
     * @param textColor Text color for text field
     */
    public void setTextFieldColor(Color backgroundColor, Color textColor) {
        textField.setBackground(backgroundColor);
        textField.setForeground(textColor);
    }

    /**
     * Set font for text field
     * @param textFont Text font for text field
     */
    public void setTextFieldFont(Font textFont) {
        textField.setFont(textFont);
    }

    /**
     * Initial setup for exit button
     * @param buttonColor Button background color
     * @param buttonFont Button text font
     */
    private void setExitButton(Color buttonColor, Font buttonFont) {
        setExitButtonColor(buttonColor, Color.WHITE);
        setExitButtonFont(buttonFont);
    }

    /**
     * Set background and text color for exit button
     * @param buttonColor Background color for button
     */
    public void setExitButtonColor(Color buttonColor, Color buttonTextColor) {
        exitButton.setBackground(buttonColor);
        exitButton.setForeground(buttonTextColor);
    }

    /**
     * Set font for exit button
     * @param buttonFont Text font for button
     */
    public void setExitButtonFont(Font buttonFont) {
        exitButton.setFont(buttonFont);
    }

    /**
     * Initial setup for bottom panel
     * @param backgroundColor Background color for bottom panel
     */
    private void setBottomPanel(Color backgroundColor) {
        setBottomPanelColor(backgroundColor);
        bottomPanel.add(textField, BorderLayout.CENTER);
        bottomPanel.add(exitButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Set background color for bottom panel
     * @param backgroundColor Background color for bottom panel
     */
    public void setBottomPanelColor(Color backgroundColor) {
        bottomPanel.setBackground(backgroundColor);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChatClientGUI().setVisible(true);
        });
    }
}
