package com.ventas.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;

public class LoaderComponent extends JPanel {
    private Timer timer;
    private int angle = 0;
    private final int LOADER_SIZE = 80;
    private final int STROKE_WIDTH = 6;
    private Image backgroundImage;
    
    public LoaderComponent() {
        setPreferredSize(new Dimension(400, 300));
        
        // Timer para la animación del círculo
        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                angle += 60;
                if (angle >= 360) {
                    angle = 0;
                }
                repaint();
            }
        });
    }
    
    // Método para establecer la imagen de fondo
    public void setBackgroundImage(String imagePath) {
        try {
            backgroundImage = new ImageIcon(imagePath).getImage();
            repaint();
        } catch (Exception e) {
            System.err.println("Error cargando imagen: " + e.getMessage());
            backgroundImage = null;//si no se encuentra la imagen se fija un color por defecto
        }
    }
    
    // Sobrecarga para usar Image directamente
    public void setBackgroundImage(Image image) {
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
        if (backgroundImage != null) {
            // Escalar la imagen para que cubra todo el componente
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2d.setColor(new Color(94, 0, 0)); // color por defecto si no hay imagen
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        
        // Activar antialiasing para mejor calidad visual
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        
        // Dibujar el círculo de fondo (gris oscuro)
        g2d.setStroke(new BasicStroke(STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(new Color(70, 70, 70));
        g2d.drawOval(centerX - LOADER_SIZE/2, centerY - LOADER_SIZE/2 - 20, LOADER_SIZE, LOADER_SIZE);
        
        // Dibujar el arco animado (color rosado/morado)
        g2d.setColor(new Color(0, 0, 0));
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
        
        // Dibujar el texto "BIENVENIDO!!"
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.setColor(Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        String text = "BIENVENIDO!!";
        int textWidth = fm.stringWidth(text);
        int textX = centerX - textWidth / 2;
        int textY = centerY + 70;
        g2d.drawString(text, textX, textY);
        
        g2d.dispose();
    }
}
