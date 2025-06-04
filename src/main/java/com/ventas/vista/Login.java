package com.ventas.vista;

import com.ventas.auth.Usuario;
import com.ventas.repositorio.RepositorioUsuarios;
import com.ventas.util.PathManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 * Ventana de Login del sistema de ventas
 * @author Jona Vicesar
 */
public class Login extends JFrame {
    private JTextField usuarioField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private RepositorioUsuarios repositorioUsuarios;
    private Usuario usuarioLogueado;
    private Loader loader;
    private JPanel mainPanel;
    private JPanel loginPanel;
    
    public Login() {
        this.repositorioUsuarios = new RepositorioUsuarios();
        initComponents();
        mostrarLoader();
    }
    
    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Login - Sistema de Ventas");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);
    
        mainPanel = new JPanel() {
            private BufferedImage backgroundImage;
            
            {
                try {
                    // cargar la imagen de fondo
                    File imageFile = new File(PathManager.getResourcePath("fondo.jpg"));
                    if (imageFile.exists()) {
                        backgroundImage = ImageIO.read(imageFile);
                    }
                } catch (IOException e) {
                    System.err.println("No se pudo cargar la imagen de fondo: " + e.getMessage());
                }
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                   
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    g.setColor(new Color(0, 0, 0, 100));
                    g.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    // sino se encuentra la imagen se agrega este fondo 
                    g.setColor(new Color(45, 45, 45));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        
        mainPanel.setLayout(new BorderLayout());

        // panel central con el formulario de login
        loginPanel = createLoginPanel();
        
        // crear loader
        loader = new Loader("Cargando...");
        
        add(mainPanel);
    }
    
    private void mostrarLoader() {
        // mostrar loader primero
        mainPanel.add(loader, BorderLayout.CENTER);
        loader.startAnimation();
        
        // después de 2 segundos, cambiar al login
        Timer timer = new Timer(2000, e -> {
            loader.stopAnimation();
            mainPanel.remove(loader);
            mainPanel.add(loginPanel, BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private JPanel createLoginPanel() {
        JPanel containerPanel = new JPanel();
        containerPanel.setOpaque(false);
        containerPanel.setLayout(new GridBagLayout());
        
        // Panel del formulario
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(240, 240, 240, 200));
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Hacer el panel redondeado
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        // logo
        JLabel logoLabel = new JLabel("V");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 48));
        logoLabel.setForeground(new Color(70, 70, 70));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(logoLabel, gbc);
        
        // subtitulo
        JLabel subtitleLabel = new JLabel("Vicesar SA");
        subtitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(subtitleLabel, gbc);
        
        // campo usuario
        JLabel userLabel = new JLabel("Usuario");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setForeground(new Color(100, 100, 100));
        
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 8, 0);
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(userLabel, gbc);
        
        usuarioField = new JTextField(20);
        usuarioField.setFont(new Font("Arial", Font.PLAIN, 14));
        usuarioField.setBackground(new Color(200, 200, 200));
        usuarioField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        formPanel.add(usuarioField, gbc);
        
        // campo password
        JLabel passwordLabel = new JLabel("Contrasena");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(100, 100, 100));
        
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 8, 0);
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(passwordLabel, gbc);
        
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBackground(new Color(200, 200, 200));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 25, 0);
        formPanel.add(passwordField, gbc);
        
        //boton iniciar sesion
        loginButton = new JButton("Iniciar Sesión");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(150, 150, 150));
        loginButton.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // efecto hover cuando se presiona el iniciar sesion
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(120, 120, 120));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(150, 150, 150));
            }
        });
        
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 0);
        formPanel.add(loginButton, gbc);
        
        // boton para ir al menu de registro
        registerButton = new JButton("Registrarse");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 12));
        registerButton.setForeground(new Color(100, 100, 100));
        registerButton.setBackground(Color.WHITE);
        registerButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // hover
        registerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registerButton.setBackground(new Color(245, 245, 245));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registerButton.setBackground(Color.WHITE);
            }
        });
        
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        formPanel.add(registerButton, gbc);
        
        // listener para agregar funcion la boton de inicio sesion
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
        
        // listener para agregar funcion la boton de registrar
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirDialogoRegistro();
            }
        });
        
        // ingresar al presionar enter
        getRootPane().setDefaultButton(loginButton);
        
        // agregar el panel del formulario al contenedor
        GridBagConstraints containerGbc = new GridBagConstraints();
        containerGbc.gridx = 0;
        containerGbc.gridy = 0;
        containerGbc.anchor = GridBagConstraints.CENTER;
        containerPanel.add(formPanel, containerGbc);
        
        return containerPanel;
    }
    
    private void realizarLogin() {
        String username = usuarioField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            mostrarError("Por favor ingrese usuario y contraseña");
            return;
        }
        
        // mostrar loader
        loginButton.setText("Iniciando...");
        loginButton.setEnabled(false);
        
    
        SwingUtilities.invokeLater(() -> {
            Usuario usuario = repositorioUsuarios.autenticar(username, password);
            
            if (usuario != null) {
                usuarioLogueado = usuario;
                // cerrar ventana de login pra abrir menu principal
                abrirMenuPrincipal();
            } else {
                mostrarError("Usuario o contraseña incorrectos");
                loginButton.setText("Iniciar Sesión");
                loginButton.setEnabled(true);
                passwordField.setText("");
            }
        });
    }
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
    }
    
    private void abrirMenuPrincipal() {
        // cerrar login
        dispose();
        
        // abrir principal
        SwingUtilities.invokeLater(() -> {
            MenuPrincipal menuPrincipal = new MenuPrincipal(usuarioLogueado);
            menuPrincipal.setVisible(true);
        });
    }
    
    private void abrirDialogoRegistro() {
        RegistroDialog dialog = new RegistroDialog(this, repositorioUsuarios);
        dialog.setVisible(true);
    }
    
    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }
    
}