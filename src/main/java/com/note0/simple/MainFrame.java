package com.note0.simple;

import javax.swing.*;
import java.awt.*;

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
        // We pass all DAOs to LoginPanel so it can build the app in the background
        LoginPanel loginPanel = new LoginPanel(this, userDAO); 
        RegistrationPanel registrationPanel = new RegistrationPanel(this, userDAO);
        LoadingPanel loadingPanel = new LoadingPanel(); 

        // Add panels to the card layout
        mainPanel.add(loginPanel, LOGIN_PANEL_ID);
        mainPanel.add(registrationPanel, REGISTER_PANEL_ID);
        mainPanel.add(loadingPanel, LOADING_PANEL_ID); 

        add(mainPanel);

        cardLayout.show(mainPanel, LOGIN_PANEL_ID);
    }

    public void showLoginPanel() {
        // Clear the main app panel when logging out to save memory
        Component[] components = mainPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JTabbedPane) {
                mainPanel.remove(component);
            }
        }
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

    /**
     * Shows the main application panel.
     * This method now just adds a pre-built JTabbedPane,
     * making it an instantaneous UI operation.
     * * @param tabbedPane The fully constructed JTabbedPane from the SwingWorker
     */
    public void showFeedPanel(JTabbedPane tabbedPane) {
        // We must remove the old app panel if one exists (e.g., from a previous login)
        Component[] components = mainPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JTabbedPane) {
                mainPanel.remove(component);
            }
        }
        
        // Add the new, pre-built panel and switch to it
        mainPanel.add(tabbedPane, MAIN_APP_PANEL_ID);
        cardLayout.show(mainPanel, MAIN_APP_PANEL_ID);
    }

    /**
     * Re-creates the main app panel from scratch.
     * This is called when a user logs in, or when a refresh is needed
     * from within the app (e.g., after rating).
     * This method now safely calls the main rebuild helper.
     * @param user The user for whom to build the panels.
     */
    public void showFeedPanel(User user) {
        System.out.println("Legacy showFeedPanel(User) called. Triggering full app rebuild.");
        rebuildAndShowMainApp(user);
    }

    /**
     * This method is now updated to trigger a full, non-blocking reload
     * of the entire main application, complete with loading spinner.
     * @param user The user object (no longer used to build panels here)
     */
    public void showDashboardPanel(User user) {
        System.out.println("showDashboardPanel(User) called. Triggering full app rebuild.");
        rebuildAndShowMainApp(user);
    }

    /**
     * Shows the loading screen, builds all main app panels in a background
     * thread, and then switches to the main app panel.
     * This is the "correct" way to refresh or load the main app.
     * @param user The currently logged-in user.
     */
    public void rebuildAndShowMainApp(User user) {
        // 1. Show the loading panel
        showLoadingPanel();

        // 2. Create a SwingWorker to build the UI in the background
        SwingWorker<JTabbedPane, Void> worker = new SwingWorker<JTabbedPane, Void>() {
            @Override
            protected JTabbedPane doInBackground() throws Exception {
                // This runs on a background thread
                // Pre-build all panels here
                JTabbedPane tabbedPane = new JTabbedPane();
                tabbedPane.setFont(UITheme.LABEL_FONT);
                tabbedPane.setBackground(UITheme.APP_BACKGROUND);

                FeedPanel feedPanel = new FeedPanel(MainFrame.this, user, materialDAO, subjectDAO, cloudinaryService);
                tabbedPane.addTab("Home", feedPanel);

                DashboardPanel dashboardPanel = new DashboardPanel(MainFrame.this, user, materialDAO, subjectDAO, cloudinaryService);
                tabbedPane.addTab("Browse", dashboardPanel);
                
                ProfilePanel profilePanel = new ProfilePanel(userDAO, user);
                tabbedPane.addTab("Profile", profilePanel);

                if ("ADMIN".equals(user.getRole())) {
                    AdminPanel adminPanel = new AdminPanel(subjectDAO, materialDAO, cloudinaryService);
                    tabbedPane.addTab("Admin", adminPanel);
                }
                
                return tabbedPane;
            }

            @Override
            protected void done() {
                try {
                    JTabbedPane tabbedPane = get(); // Get the pre-built pane
                    // This is now an instant UI operation
                    showFeedPanel(tabbedPane);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Error loading application: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    showLoginPanel(); // Send back to login on error
                    e.printStackTrace();
                }
            }
        };

        // 3. Start the worker
        worker.execute();
    }
}

