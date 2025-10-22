package com.note0.simple;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A panel that displays a smooth, circular, looping spinner
 * and a "Loading..." message. This is animated using a Swing Timer.
 */
public class LoadingPanel extends JPanel {

    public LoadingPanel() {
        // Use GridBagLayout to center the components vertically
        setLayout(new GridBagLayout());
        setBackground(UITheme.APP_BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        // 1. The custom circular spinner component
        CircularSpinner spinner = new CircularSpinner();
        gbc.gridy = 0;
        add(spinner, gbc);

        // 2. The "Logging in..." label
        JLabel loadingLabel = new JLabel("Logging in, please wait...");
        loadingLabel.setFont(UITheme.HEADING_FONT);
        loadingLabel.setForeground(UITheme.TEXT_COLOR);
        
        gbc.gridy = 1;
        add(loadingLabel, gbc);
    }

    /**
     * An inner class that represents the spinning circular indicator.
     * It uses a Timer to animate itself by re-painting at regular intervals.
     */
    private class CircularSpinner extends JPanel {

        private javax.swing.Timer timer;
        private int rotationAngle = 0; // The starting angle of the arc

        public CircularSpinner() {
            // Set the preferred size for the spinner area
            setPreferredSize(new Dimension(60, 60));
            // Make the panel transparent
            setOpaque(false);

            // Create a timer that fires every 16ms (for ~60 FPS)
            timer = new javax.swing.Timer(16, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Increment the rotation angle.
                    // (Adding 6 degrees each frame gives a good spin speed)
                    rotationAngle = (rotationAngle + 6) % 360;
                    
                    // Trigger a repaint, which will call paintComponent
                    repaint();
                }
            });
            
            // Start the animation timer
            timer.start();
        }

        /**
         * This method is called every time repaint() is triggered by the timer.
         * It draws the spinning arc.
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // Use Graphics2D for better rendering control
            Graphics2D g2d = (Graphics2D) g.create();
            
            try {
                // 1. Turn on Anti-aliasing for smooth, non-pixelated edges
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 2. Calculate the position and size of the arc
                int strokeWidth = 5; // How thick the arc line is
                // Make the arc 5px smaller than the panel's edges
                int diameter = Math.min(getWidth(), getHeight()) - (strokeWidth * 2);
                int x = (getWidth() - diameter) / 2;
                int y = (getHeight() - diameter) / 2;
                
                // 3. Set the "pen" (Stroke)
                // We use a 5px thick, rounded line
                g2d.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                // 4. Set the color from our theme
                g2d.setColor(UITheme.ACCENT_COLOR);

                // 5. Draw the arc
                // This draws a 270-degree arc (like a "C") starting at the 'rotationAngle'
                g2d.drawArc(x, y, diameter, diameter, rotationAngle, 270);
                
            } finally {
                // Clean up the graphics context
                g2d.dispose();
            }
        }
    }
}

