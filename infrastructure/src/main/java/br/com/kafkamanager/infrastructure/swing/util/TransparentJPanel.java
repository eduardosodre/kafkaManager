package br.com.kafkamanager.infrastructure.swing.util;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class TransparentJPanel extends JPanel {

    public TransparentJPanel() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}