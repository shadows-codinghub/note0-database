package com.note0.simple;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * A helper class to store a consistent theme (colors, fonts, etc.)
 * for the entire application, making it easy to change the style later.
 */
public class UITheme {

    // --- Colors ---
    public static final Color APP_BACKGROUND = new Color(245, 247, 250); // Light gray background
    public static final Color CARD_BACKGROUND = Color.WHITE; // White cards
    public static final Color PRIMARY_COLOR = new Color(0, 123, 255); // Blue for primary actions
    public static final Color PRIMARY_FOREGROUND = Color.WHITE; // White text on primary buttons
    public static final Color SECONDARY_COLOR = new Color(108, 117, 125); // Gray for secondary text/actions
    public static final Color TEXT_COLOR = new Color(33, 37, 41); // Dark gray for main text
    public static final Color BORDER_COLOR = new Color(222, 226, 230); // Light gray for borders
    public static final Color DANGER_COLOR = new Color(220, 53, 69); // Red for delete/reject actions

    // --- Fonts ---
    // Using common system font "SansSerif" for broad compatibility
    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 28); // Large title font
    public static final Font HEADING_FONT = new Font("SansSerif", Font.BOLD, 20); // Section headings
    public static final Font BODY_FONT = new Font("SansSerif", Font.PLAIN, 14); // Standard text
    public static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 14); // Button text
    public static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 14); // Labels (forms, table headers)

    // --- Borders ---
    public static final Border APP_PADDING = new EmptyBorder(15, 15, 15, 15); // Padding around main panels
    public static final Border CARD_PADDING = new EmptyBorder(20, 20, 20, 20); // Padding inside cards
    // Increased vertical padding for text fields to make them taller
    public static final Border FIELD_PADDING = new EmptyBorder(14, 12, 14, 12);

    /**
     * Styles a primary action button (e.g., Login, Register, Upload, Approve).
     * @param button The JButton to style.
     */
    public static void stylePrimaryButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(PRIMARY_FOREGROUND);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false); // Remove focus border
        button.setBorder(new EmptyBorder(10, 20, 10, 20)); // Padding inside button
        button.setOpaque(true); // Needed for background color on some L&Fs
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
    }

    /**
     * Styles a secondary action button (e.g., Rate, Refresh, Choose File).
     * Often used for less critical actions or alternatives.
     * @param button The JButton to style.
     */
    public static void styleSecondaryButton(JButton button) {
        button.setBackground(CARD_BACKGROUND); // Usually white or light background
        button.setForeground(PRIMARY_COLOR); // Blue text
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        // Create an outlined border with padding inside
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, PRIMARY_COLOR), // 1px blue border
            new EmptyBorder(9, 19, 9, 19) // Inner padding (10-1 for border thickness)
        ));
        button.setOpaque(false); // Make background transparent (if needed by L&F)
        button.setContentAreaFilled(false); // Don't fill background
        button.setBorderPainted(true); // Ensure border is visible
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Styles a red "danger" button used for destructive actions (e.g., Delete, Reject).
     * @param button The JButton to style.
     */
    public static void styleDangerButton(JButton button) {
        button.setBackground(DANGER_COLOR);
        button.setForeground(PRIMARY_FOREGROUND); // White text
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20)); // Padding
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Styles a button to look like a hyperlink (e.g., Back to Login).
     * @param button The JButton to style.
     */
    public static void styleLinkButton(JButton button) {
        button.setForeground(PRIMARY_COLOR); // Blue text
        button.setFont(BODY_FONT); // Standard text font
        button.setOpaque(false);
        button.setContentAreaFilled(false); // No background fill
        button.setBorderPainted(false); // No border
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Optional: Add underline on hover (requires MouseListener)
    }

    /**
     * Styles a standard JTextField or JPasswordField.
     * @param field The text component to style.
     */
    public static void styleTextField(JTextField field) {
        field.setFont(BODY_FONT);
        // Add a light gray border and inner padding
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, BORDER_COLOR), // 1px border
            FIELD_PADDING // Inner padding defined above
        ));
    }
}

