package com.note0.simple;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginPanel extends JPanel {

    private final MainFrame mainFrame;
    private final UserDAO userDAO;

    private JTextField emailField = new JTextField(20);
    private JPasswordField passwordField = new JPasswordField(20);
    private JButton loginButton = new JButton("Login");
    private JButton registerButton = new JButton("Go to Register");
    private JPanel formPanel; // <-- Made formPanel a field to disable it

    public LoginPanel(MainFrame mainFrame, UserDAO userDAO) {
        this.mainFrame = mainFrame;
        this.userDAO = userDAO;

        // Use a GridBagLayout to center the form panel
        setLayout(new GridBagLayout());
        setBackground(UITheme.APP_BACKGROUND); // Use theme background

        // --- Create the main form panel ---
        // Use the class field
        formPanel = new JPanel(new GridBagLayout()); 
        formPanel.setBackground(UITheme.CARD_BACKGROUND); // White card background
        formPanel.setBorder(UITheme.createShadowBorder()); // Apply neo-brutalism shadow border
        
        GridBagConstraints gbc = new GridBagConstraints();
// ... (rest of the constructor code is identical) ...
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Note0 Login");
        titleLabel.setFont(UITheme.HEADING_FONT); // Use theme heading font
        titleLabel.setForeground(UITheme.TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 20, 10); // More padding below title
        formPanel.add(titleLabel, gbc);

        // --- Reset constraints for fields ---
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(UITheme.LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(emailLabel, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(emailField, gbc);

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(UITheme.LABEL_FONT);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passLabel, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(passwordField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(UITheme.CARD_BACKGROUND); // Match card background
        
        UITheme.stylePrimaryButton(loginButton); // Style the login button
        UITheme.styleSecondaryButton(registerButton); // Style the register button

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 10, 10, 10); // More padding above buttons
        formPanel.add(buttonPanel, gbc);
        
        // --- Add the form panel to the main panel (which centers it) ---
        add(formPanel, new GridBagConstraints());
// ... (rest of the constructor code is identical) ...

        // Add Action Listeners
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> mainFrame.showRegistrationPanel());
    }

    /**
     * Handles the login logic using a SwingWorker for a non-blocking UI.
     */
    private void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email and password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Show the loading panel FIRST
        mainFrame.showLoadingPanel();

        // Create a SwingWorker to run the database call on a background thread
        SwingWorker<User, Void> worker = new SwingWorker<User, Void>() {
            
            /**
             * This runs on a background thread.
             * No Swing UI code here!
             */
            @Override
            protected User doInBackground() throws Exception {
                // This is the long-running task
                return userDAO.loginUser(email, password);
            }

            /**
             * This runs on the UI thread after doInBackground() finishes.
             * Safe to update the UI here.
             */
            @Override
            protected void done() {
                try {
                    User user = get(); // Get the result from doInBackground()
                    
                    if (user != null) {
                        // Success! Show the main app feed
                        mainFrame.showFeedPanel(user);
                    } else {
                        // Login failed, show error and go back to login panel
                        JOptionPane.showMessageDialog(mainFrame, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                        mainFrame.showLoginPanel();
                    }
                } catch (Exception e) {
                    // Database or other error, show error and go back to login panel
                    JOptionPane.showMessageDialog(mainFrame, "Database error during login: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                    mainFrame.showLoginPanel();
                    e.printStackTrace();
                }
            }
        };

        // Start the worker
        worker.execute();
    }
}

