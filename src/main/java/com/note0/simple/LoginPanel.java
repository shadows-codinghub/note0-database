package com.note0.simple;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginPanel extends JPanel {

    private final MainFrame mainFrame;
    private final UserDAO userDAO;
    private final MaterialDAO materialDAO;
    private final SubjectDAO subjectDAO;
    private final CloudinaryService cloudinaryService;

    private JTextField emailField = new JTextField(20);
    private JPasswordField passwordField = new JPasswordField(20);
    private JButton loginButton = new JButton("Login");
    private JButton registerButton = new JButton("Go to Register");
    private JPanel formPanel;

    /**
     * Helper class to store the result of the background login task.
     * This allows us to return both the User and the fully-built JTabbedPane.
     */
    private static class LoginResult {
        final User user;
        final JTabbedPane tabbedPane;

        LoginResult(User user, JTabbedPane tabbedPane) {
            this.user = user;
            this.tabbedPane = tabbedPane;
        }
    }

    public LoginPanel(MainFrame mainFrame, UserDAO userDAO) {
        this.mainFrame = mainFrame;
        this.userDAO = userDAO;
        
        // We need all the DAOs to pre-build the panels in the background
        this.materialDAO = new MaterialDAO();
        this.subjectDAO = new SubjectDAO();
        this.cloudinaryService = new CloudinaryService();


        // --- UI Setup (No changes here) ---
        setLayout(new GridBagLayout());
        setBackground(UITheme.APP_BACKGROUND); 
        formPanel = new JPanel(new GridBagLayout()); 
        formPanel.setBackground(UITheme.CARD_BACKGROUND); 
        formPanel.setBorder(UITheme.createShadowBorder()); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel titleLabel = new JLabel("Note0 Login");
        titleLabel.setFont(UITheme.HEADING_FONT); 
        titleLabel.setForeground(UITheme.TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 20, 10); 
        formPanel.add(titleLabel, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 10, 10, 10);
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(UITheme.LABEL_FONT);
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(emailLabel, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(emailField, gbc);
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(UITheme.LABEL_FONT);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(passLabel, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(passwordField, gbc);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(UITheme.CARD_BACKGROUND); 
        UITheme.stylePrimaryButton(loginButton); 
        UITheme.styleSecondaryButton(registerButton); 
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 10, 10, 10); 
        formPanel.add(buttonPanel, gbc);
        add(formPanel, new GridBagConstraints());
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

        // Create a SwingWorker to run ALL database calls on a background thread
        // It now returns a 'LoginResult' object
        SwingWorker<LoginResult, Void> worker = new SwingWorker<LoginResult, Void>() {
            
            /**
             * This runs on a background thread.
             * No Swing UI code here!
             */
            @Override
            protected LoginResult doInBackground() throws Exception {
                // 1. First task: Log the user in
                User user = userDAO.loginUser(email, password);
                
                if (user != null) {
                    // 2. Second task: If login is successful, pre-build all panels
                    // This is where all the *other* database calls will happen,
                    // safely in the background.
                    JTabbedPane tabbedPane = new JTabbedPane();
                    tabbedPane.setFont(UITheme.LABEL_FONT);
                    tabbedPane.setBackground(UITheme.APP_BACKGROUND);

                    FeedPanel feedPanel = new FeedPanel(mainFrame, user, materialDAO, subjectDAO, cloudinaryService);
                    tabbedPane.addTab("Home", feedPanel);

                    DashboardPanel dashboardPanel = new DashboardPanel(mainFrame, user, materialDAO, subjectDAO, cloudinaryService);
                    tabbedPane.addTab("Browse", dashboardPanel);
                    
                    ProfilePanel profilePanel = new ProfilePanel(userDAO, user);
                    tabbedPane.addTab("Profile", profilePanel);

                    if ("ADMIN".equals(user.getRole())) {
                        AdminPanel adminPanel = new AdminPanel(subjectDAO, materialDAO, cloudinaryService);
                        tabbedPane.addTab("Admin", adminPanel);
                    }
                    
                    // Return the fully-built result
                    return new LoginResult(user, tabbedPane);
                } else {
                    // Login failed, return null
                    return new LoginResult(null, null);
                }
            }

            /**
             * This runs on the UI thread after doInBackground() finishes.
             * Safe to update the UI here.
             */
            @Override
            protected void done() {
                try {
                    LoginResult result = get(); // Get the result from doInBackground()
                    
                    if (result.user != null) {
                        // Success! Show the main app feed.
                        // This is now an INSTANT operation because the pane is already built.
                        mainFrame.showFeedPanel(result.tabbedPane);
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

