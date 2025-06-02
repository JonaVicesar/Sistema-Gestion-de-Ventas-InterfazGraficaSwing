package com.ventas.vista;

import java.awt.Color;
import java.util.Timer;
import javax.swing.JDialog;
import javax.swing.JFrame;

class LoaderDialog extends JDialog {
    private LoaderComponent loaderComponent;
    
    public LoaderDialog(JFrame parent) {
        super(parent, true);
        setUndecorated(true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        
        loaderComponent = new LoaderComponent();
        add(loaderComponent);
        
        // Agregar borde redondeado opcional
        setBackground(new Color(0, 0, 0, 0));
    }
    
    public void showLoader() {
        loaderComponent.startAnimation();
        setVisible(true);
    }
    
    public void hideLoader() {
        loaderComponent.stopAnimation();
        setVisible(false);
        dispose();
    }
    
    // Método para simular carga con tiempo determinado
}

