package com.note0.simple;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A new panel to display the user's own uploaded materials
 * and allow them to delete them.
 */
public class MyUploadsPanel extends JPanel {

    private final MainFrame mainFrame;
    private final User loggedInUser;
    private final MaterialDAO materialDAO;
    private final CloudinaryService cloudinaryService;

    private JTable myUploadsTable;
    private DefaultTableModel myUploadsModel;
    private List<Material> myMaterials = new ArrayList<>();

    public MyUploadsPanel(MainFrame mainFrame, User user, MaterialDAO materialDAO, CloudinaryService cloudinaryService) {
        this.mainFrame = mainFrame;
        this.loggedInUser = user;
        this.materialDAO = materialDAO;
        this.cloudinaryService = cloudinaryService;

        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.APP_BACKGROUND);
        setBorder(UITheme.APP_PADDING);
        
        // Header
        JLabel titleLabel = new JLabel("My Uploaded Materials");
        titleLabel.setFont(UITheme.HEADING_FONT);
        titleLabel.setForeground(UITheme.TEXT_COLOR);
        add(titleLabel, BorderLayout.NORTH);

        // My uploads table
        String[] columnNames = {"Title", "Subject", "Rating", "Status"};
        myUploadsModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        myUploadsTable = new JTable(myUploadsModel);
        myUploadsTable.setRowHeight(30);
        myUploadsTable.setFont(UITheme.BODY_FONT);
        myUploadsTable.setGridColor(UITheme.BORDER_COLOR);
        myUploadsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JTableHeader header = myUploadsTable.getTableHeader();
        header.setFont(UITheme.LABEL_FONT);
        header.setBackground(UITheme.CARD_BACKGROUND);
        
        JScrollPane tableScrollPane = new JScrollPane(myUploadsTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR));
        add(tableScrollPane, BorderLayout.CENTER);
        
        // Action buttons for my uploads
        JPanel myUploadsActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        myUploadsActionPanel.setOpaque(false);
        
        JButton refreshMyUploadsButton = new JButton("Refresh");
        UITheme.styleSecondaryButton(refreshMyUploadsButton);
        
        JButton deleteMyUploadButton = new JButton("Delete Selected");
        UITheme.styleDangerButton(deleteMyUploadButton);
        
        deleteMyUploadButton.addActionListener(e -> deleteMyUpload());
        refreshMyUploadsButton.addActionListener(e -> loadMyUploads());
        
        myUploadsActionPanel.add(refreshMyUploadsButton);
        myUploadsActionPanel.add(deleteMyUploadButton);
        
        add(myUploadsActionPanel, BorderLayout.SOUTH);
        
        // Load initial data
        loadMyUploads();
    }
    
    private void loadMyUploads() {
        myUploadsModel.setRowCount(0);
        myMaterials.clear();
        try {
            myMaterials = materialDAO.getMaterialsByUser(loggedInUser.getId());
            for (Material material : myMaterials) {
                myUploadsModel.addRow(new Object[]{
                    material.getTitle(), 
                    material.getSubjectName(), 
                    String.format("%.1f", material.getAverageRating()),
                    material.getApprovalStatus()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not load your uploads: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteMyUpload() {
        int selectedRow = myUploadsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an upload to delete.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            if (selectedRow < myMaterials.size()) {
                Material material = myMaterials.get(selectedRow);
                
                JOptionPane.showMessageDialog(this, 
                    "Are you sure you want to delete '" + material.getTitle() + "'?\n\n" +
                    "This will remove the material from the database.\n" +
                    "The uploaded file will remain in Cloudinary storage.", 
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                
                // ... inside your delete method ...

        // ADD THIS LINE BACK - This is what creates the 'confirm' variable
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete '" + material.getTitle() + "'?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION);
        
        // Now this 'if' statement will work
        if (confirm == JOptionPane.YES_OPTION) { 
            materialDAO.deleteMaterial(material.getId());
            JOptionPane.showMessageDialog(this, "Upload deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadMyUploads(); // Refresh the list
        }
// ...
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting upload: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
