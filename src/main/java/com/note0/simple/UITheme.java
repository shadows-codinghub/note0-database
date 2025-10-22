package com.note0.simple;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

/**
 * A utility class to hold all the common colors, fonts, and styles
 * for the application to ensure a consistent neo-brutalism look and feel.
 */
public class UITheme {

    // --- Colors ---
    // A light, warm, off-white for the background
    public static final Color APP_BACKGROUND = new Color(248, 244, 236);
    // Pure white for content cards to contrast with the background
    public static final Color CARD_BACKGROUND = Color.WHITE;
    // Pure black for text and borders for high contrast
    public static final Color TEXT_COLOR = Color.BLACK;
    public static final Color BORDER_COLOR = Color.BLACK;
    // A bright, vibrant blue for primary actions
    public static final Color ACCENT_COLOR = new Color(0, 86, 255);
    // A strong red for danger actions
    public static final Color DANGER_COLOR = new Color(255, 59, 48);
    // A simple gray for secondary text or actions
    public static final Color SECONDARY_COLOR = new Color(108, 117, 125);

    // --- Fonts ---
    public static final Font HEADING_FONT = new Font("SansSerif", Font.BOLD, 28);
    public static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 14);
    public static final Font BODY_FONT = new Font("SansSerif", Font.PLAIN, 14);


    // --- Borders ---
    // Increased padding for a cleaner look
    public static final Border APP_PADDING = BorderFactory.createEmptyBorder(20, 20, 20, 20);
    // The main border style for buttons
    private static final Border BUTTON_BORDER = new CompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 2), // 2px black line
            BorderFactory.createEmptyBorder(10, 20, 10, 20)  // 10px vertical, 20px horizontal padding
    );

    /**
     * Creates the signature neo-brutalist shadow border.
     * It's a 2px black border combined with a 4px black shadow, offset down and to the right.
     * @return A composed Border object.
     */
    public static Border createShadowBorder() {
        Border contentBorder = new CompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 2), // The main 2px black border
                BorderFactory.createEmptyBorder(15, 15, 15, 15)  // 15px internal padding
        );
        
        // This MatteBorder creates the shadow: 0px top, 0px left, 4px bottom, 4px right
        Border shadow = BorderFactory.createMatteBorder(0, 0, 4, 4, BORDER_COLOR);

        // Combine the shadow (outside) and the content border (inside)
        return new CompoundBorder(shadow, contentBorder);
    }

    /**
     * Styles a JButton as a primary action button.
     * @param button The JButton to style.
     */
    public static void stylePrimaryButton(JButton button) {
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(LABEL_FONT);
        button.setBorder(BUTTON_BORDER);
        button.setOpaque(true);
    }

    /**
     * Styles a JButton to look like a "secondary" (e.g., refresh) button.
     * @param button The JButton to style.
     */
    public static void styleSecondaryButton(JButton button) {
        button.setBackground(CARD_BACKGROUND);
        button.setForeground(TEXT_COLOR);
        button.setFont(LABEL_FONT);
        button.setBorder(BUTTON_BORDER);
        button.setOpaque(true);
    }

    /**
     * Styles a JButton to look like a "danger" (e.g., delete) button.
     * @param button The JButton to style.
     */
    public static void styleDangerButton(JButton button) {
        button.setBackground(DANGER_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(LABEL_FONT);
        button.setBorder(BUTTON_BORDER);
        button.setOpaque(true);
    }
}
