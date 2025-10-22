package com.note0.simple;

import javax.swing.*;
import java.awt.*;

/**
 * The main window of the application.
 * This class holds and manages the different panels (Login, Register, Feed, Dashboard)
 * using a CardLayout, allowing for seamless navigation between different views
 * without opening or closing new windows.
 * * New Navigation Flow: Login -> Main App (with tabs) -> specific tabs
 */
public class MainFrame extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;

    private final UserDAO userDAO;
    private final MaterialDAO materialDAO;
    private final SubjectDAO subjectDAO;
    private final CloudinaryService cloudinaryService;

    // Define panel names as constants for easy reference
    public static final String LOGIN_PANEL_ID = "LOGIN_PANEL";
    public static final String REGISTER_PANEL_ID = "REGISTER_PANEL";
    public static final String LOADING_PANEL_ID = "LOADING_PANEL";
    public static final String MAIN_APP_PANEL_ID = "MAIN_APP_PANEL";


    public MainFrame() {
        this.userDAO = new UserDAO();
        this.materialDAO = new MaterialDAO();
        this.subjectDAO = new SubjectDAO();
        this.cloudinaryService = new CloudinaryService();

        setTitle("Note0 - Note Sharing Application");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set the frame's content pane background
        getContentPane().setBackground(UITheme.APP_BACKGROUND);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(UITheme.APP_BACKGROUND); // Set main panel background

        // Create all panels
        LoginPanel loginPanel = new LoginPanel(this, userDAO);
        RegistrationPanel registrationPanel = new RegistrationPanel(this, userDAO);
        LoadingPanel loadingPanel = new LoadingPanel(); // <-- ADDED

        // Add panels to the card layout
        mainPanel.add(loginPanel, LOGIN_PANEL_ID);
        mainPanel.add(registrationPanel, REGISTER_PANEL_ID);
        mainPanel.add(loadingPanel, LOADING_PANEL_ID); // <-- ADDED

        add(mainPanel);

        cardLayout.show(mainPanel, LOGIN_PANEL_ID);
    }

    public void showLoginPanel() {
        cardLayout.show(mainPanel, LOGIN_PANEL_ID);
    }

    public void showRegistrationPanel() {
        cardLayout.show(mainPanel, REGISTER_PANEL_ID);
    }

    /**
     * Shows the loading panel.
     * This is called right before a long task (like login) starts.
     */
    public void showLoadingPanel() {
        cardLayout.show(mainPanel, LOADING_PANEL_ID);
    }

    public void showFeedPanel(User user) {
        JTabbedPane tabbedPane = new JTabbedPane();
        // Apply theme font to tabs
        tabbedPane.setFont(UITheme.LABEL_FONT);
        tabbedPane.setBackground(UITheme.APP_BACKGROUND);

        FeedPanel feedPanel = new FeedPanel(this, user, materialDAO, subjectDAO, cloudinaryService);
        tabbedPane.addTab("Home", feedPanel);

        DashboardPanel dashboardPanel = new DashboardPanel(this, user, materialDAO, subjectDAO, cloudinaryService);
        tabbedPane.addTab("Browse", dashboardPanel);
        
        ProfilePanel profilePanel = new ProfilePanel(userDAO, user);
        tabbedPane.addTab("Profile", profilePanel);

        if ("ADMIN".equals(user.getRole())) {
            AdminPanel adminPanel = new AdminPanel(subjectDAO, materialDAO, cloudinaryService);
            tabbedPane.addTab("Admin", adminPanel);
        }

        // We use a constant here, but we must add the component.
        // It's safer to remove the old one if it exists.
        Component[] components = mainPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JTabbedPane) {
                mainPanel.remove(component);
            }
        }
        mainPanel.add(tabbedPane, MAIN_APP_PANEL_ID);
        cardLayout.show(mainPanel, MAIN_APP_PANEL_ID);
    }

    public void showDashboardPanel(User user) {
        // This method is now effectively replaced by the tabbed pane in showFeedPanel
        // but we can keep it for potential future use or specific navigation needs.
        showFeedPanel(user); 
    }
}

