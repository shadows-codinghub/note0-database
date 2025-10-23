package com.note0.simple;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class RegistrationPanel extends JPanel {

    private final MainFrame mainFrame;
    private final UserDAO userDAO;

    private JTextField fullNameField = new JTextField(20);
    private JTextField emailField = new JTextField(20);
    private JPasswordField passwordField = new JPasswordField(20);
    private JButton registerButton = new JButton("Register");
    private JButton backToLoginButton = new JButton("Back to Login");

    public RegistrationPanel(MainFrame mainFrame, UserDAO userDAO) {
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
        JLabel titleLabel = new JLabel("Create Account");
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

        // Full Name
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(UITheme.LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(nameLabel, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(fullNameField, gbc);
        
        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(UITheme.LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(emailField, gbc);

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(UITheme.LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(passwordField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(UITheme.CARD_BACKGROUND); // Match card background
        
        UITheme.stylePrimaryButton(registerButton); // Style the register button
        UITheme.styleSecondaryButton(backToLoginButton); // Style the back button

        buttonPanel.add(registerButton);
        buttonPanel.add(backToLoginButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 10, 10, 10); // More padding above buttons
        formPanel.add(buttonPanel, gbc);
        
        // --- Add the form panel to the main panel (which centers it) ---
        add(formPanel, new GridBagConstraints());

        // Action Listeners
        registerButton.addActionListener(e -> handleRegister());
        backToLoginButton.addActionListener(e -> mainFrame.showLoginPanel());
    }

    private void handleRegister() {
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (fullName.isBlank() || email.isBlank() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password);

        try {
            userDAO.registerUser(user);
            JOptionPane.showMessageDialog(this, "Registration Successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.showLoginPanel();
        } catch (SQLException ex) {
            if (ex.getSQLState().equals("23505")) {
                JOptionPane.showMessageDialog(this, "This email address is already registered.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "A database error occurred: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
