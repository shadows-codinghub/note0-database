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

    public LoginPanel(MainFrame mainFrame, UserDAO userDAO) {
        this.mainFrame = mainFrame;
        this.userDAO = userDAO;

        // Use a GridBagLayout to center the form panel
        setLayout(new GridBagLayout());
        setBackground(UITheme.APP_BACKGROUND); // Use theme background

        // --- Create the main form panel ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.CARD_BACKGROUND); // White card background
        formPanel.setBorder(UITheme.createShadowBorder()); // Apply neo-brutalism shadow border
        
        GridBagConstraints gbc = new GridBagConstraints();
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

        // Add Action Listeners
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> mainFrame.showRegistrationPanel());
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email and password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            User user = userDAO.loginUser(email, password);
            if (user != null) {
                mainFrame.showFeedPanel(user);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error during login: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
