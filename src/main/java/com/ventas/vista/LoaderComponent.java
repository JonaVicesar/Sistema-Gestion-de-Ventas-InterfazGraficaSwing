package com.ventas.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;

public class LoaderComponent extends JPanel {
    private Timer timer;
    private int angle = 0;
    private final int LOADER_SIZE = 80;
    private final int STROKE_WIDTH = 6;
    private BufferedImage backgroundImage;
    
    // Colores consistentes con MenuPrincipal
    private final Color COLOR_PRIMARIO = new Color(33, 150, 243);
    private final Color COLOR_ACENTO = new Color(255, 193, 7);
    private final Color COLOR_FONDO_OSCURO = new Color(37, 37, 37);
    
    public LoaderComponent() {
        setPreferredSize(new Dimension(400, 300));
        setBackground(COLOR_FONDO_OSCURO);
        
        timer = new Timer(50, e -> {
            angle += 15;
            if (angle >= 360) angle = 0;
            repaint();
        });
    }
    
    public void setBackgroundImage(BufferedImage image) {
        this.backgroundImage = image;
        repaint();
    }
    
    public void startAnimation() {
        timer.start();
    }
    
    public void stopAnimation() {
        timer.stop();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Fondo con imagen o color sólido
        if (backgroundImage != null) {
            // Aplicar efecto de desenfoque
            Graphics2D bg = (Graphics2D) g2d.create();
            bg.setComposite(AlphaComposite.SrcOver.derive(0.7f));
            bg.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            bg.dispose();
        }
        
        // Activar antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        
        // Círculo de fondo
        g2d.setStroke(new BasicStroke(STROKE_WIDTH));
        g2d.setColor(new Color(70, 70, 70, 180));
        g2d.fillOval(centerX - LOADER_SIZE/2, centerY - LOADER_SIZE/2 - 20, LOADER_SIZE, LOADER_SIZE);
        
        // Arco animado (color primario)
        g2d.setColor(COLOR_PRIMARIO);
        Arc2D.Float arc = new Arc2D.Float(
            centerX - LOADER_SIZE/2, 
            centerY - LOADER_SIZE/2 - 20, 
            LOADER_SIZE, 
            LOADER_SIZE, 
            angle, 
            90, 
            Arc2D.OPEN
        );
        g2d.draw(arc);
        
        // Texto
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.setColor(COLOR_ACENTO);
        String text = "BIENVENIDO";
        FontMetrics fm = g2d.getFontMetrics();
        int textX = centerX - fm.stringWidth(text) / 2;
        int textY = centerY + 70;
        g2d.drawString(text, textX, textY);
        
        g2d.dispose();
    }
}