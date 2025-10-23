package com.note0.simple;

import com.formdev.flatlaf.FlatDarkLaf; // <-- 1. IMPORT THE DARK THEME
import javax.swing.SwingUtilities;
import java.sql.SQLException;

/**
 * The main entry point for the Note0 Desktop Application.
 * This class is responsible for initializing and launching the main application window.
 * It uses SwingUtilities.invokeLater to ensure that the GUI is created and updated
 * on the Event Dispatch Thread (EDT), which is the standard and required practice
 * for thread-safe Swing applications.
 */
public class Main {

    public static void main(String[] args) {
        
        // --- 2. SET THE MODERN DARK LOOK AND FEEL ---
        try {
            // This is the only line that needs to change:
            FlatDarkLaf.setup(); // This sets a clean, professional dark theme.
            
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        // ------------------------------------------

        // Schedule a job for the event dispatch thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create admin user if it doesn't exist
                try {
                    UserDAO userDAO = new UserDAO();
                    userDAO.createAdminUser();
                } catch (SQLException e) {
                    System.err.println("Error creating admin user: " + e.getMessage());
                }
                
                // Create the main application frame and make it visible.
                // The MainFrame will handle all other components and logic from here.
                new MainFrame().setVisible(true);
            }
        });
    }
}
