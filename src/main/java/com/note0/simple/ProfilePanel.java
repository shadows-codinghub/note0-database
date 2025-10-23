package com.note0.simple;

import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {

    private final UserDAO userDAO;
    private final User currentUser;

    private JTextField fullNameField = new JTextField(20);
    private JTextField emailField = new JTextField(20);
    private JTextField collegeNameField = new JTextField(20);
    private JSpinner semesterSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));
    private JButton updateButton = new JButton("Update Profile");

    public ProfilePanel(UserDAO userDAO, User currentUser) {
        this.userDAO = userDAO;
        this.currentUser = currentUser;

        // Use a GridBagLayout to center the form
        setLayout(new GridBagLayout());
        setBackground(UITheme.APP_BACKGROUND);

        // Create the form panel with a shadow border
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.CARD_BACKGROUND);
        formPanel.setBorder(UITheme.createShadowBorder());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Your Profile");
        titleLabel.setFont(UITheme.HEADING_FONT);
        titleLabel.setForeground(UITheme.TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Full Name
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(UITheme.LABEL_FONT);
        nameLabel.setForeground(UITheme.TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(fullNameField, gbc);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(UITheme.LABEL_FONT);
        emailLabel.setForeground(UITheme.TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(emailLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(emailField, gbc);

        // College Name
        JLabel collegeLabel = new JLabel("College Name:");
        collegeLabel.setFont(UITheme.LABEL_FONT);
        collegeLabel.setForeground(UITheme.TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(collegeLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(collegeNameField, gbc);

        // Semester
        JLabel semesterLabel = new JLabel("Semester:");
        semesterLabel.setFont(UITheme.LABEL_FONT);
        semesterLabel.setForeground(UITheme.TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(semesterLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(semesterSpinner, gbc);

        // Update Button
        UITheme.stylePrimaryButton(updateButton);
        gbc.gridx = 1; gbc.gridy = 5; 
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(updateButton, gbc);

        updateButton.addActionListener(e -> updateUserProfile());

        // Add the form panel to the centering wrapper
        add(formPanel, new GridBagConstraints());

        loadProfile();
    }

    private void loadProfile() {
// ... (existing code, no changes) ...
        fullNameField.setText(currentUser.getFullName());
        emailField.setText(currentUser.getEmail());
        collegeNameField.setText(currentUser.getCollegeName());
        semesterSpinner.setValue(currentUser.getSemester());
    }

    private void updateUserProfile() {
// ... (existing code, no changes) ...
        currentUser.setFullName(fullNameField.getText());
        currentUser.setEmail(emailField.getText());
        currentUser.setCollegeName(collegeNameField.getText());
        currentUser.setSemester((int) semesterSpinner.getValue());

        try {
            userDAO.updateUser(currentUser);
            JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating profile: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
