package br.com.kafkamanager.infrastructure.swing.util;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private Image backgroundImage;

    public BackgroundPanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public BackgroundPanel() {
        this.backgroundImage = new ImageIcon(
            Objects.requireNonNull(
                getClass().getClassLoader().getResource("background.jpg"))).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
        repaint();
    }
}

