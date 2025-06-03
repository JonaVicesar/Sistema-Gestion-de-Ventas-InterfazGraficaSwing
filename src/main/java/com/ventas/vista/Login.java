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
    
    public Login() {
        this.repositorioUsuarios = new RepositorioUsuarios();
        initComponents();
    }
    
    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Login - Sistema de Ventas");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Panel principal con imagen de fondo
        JPanel mainPanel = new JPanel() {
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
        JPanel loginPanel = createLoginPanel();
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        
        add(mainPanel);
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
    
    /**
     * dialogo para registro usuarios
     */
    private static class RegistroDialog extends JDialog {
        private JTextField usernameField;
        private JPasswordField passwordField;
        private JPasswordField confirmPasswordField;
        private JTextField nombreField;
        private JComboBox<String> rolComboBox;
        private RepositorioUsuarios repositorioUsuarios;
        
        public RegistroDialog(JFrame parent, RepositorioUsuarios repositorio) {
            super(parent, "Registro de Usuario", true);
            this.repositorioUsuarios = repositorio;
            initRegistroComponents();
        }
        
        private void initRegistroComponents() {
            setSize(400, 450);
            setLocationRelativeTo(getParent());
            setResizable(false);
            
            JPanel mainPanel = new JPanel();
            mainPanel.setBackground(Color.WHITE);
            mainPanel.setLayout(new GridBagLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
            
            GridBagConstraints gbc = new GridBagConstraints();
            
            // titulo
            JLabel titleLabel = new JLabel("Registro de Usuario");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            titleLabel.setForeground(new Color(70, 70, 70));
            
            gbc.gridx = 0; gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(0, 0, 25, 0);
            gbc.anchor = GridBagConstraints.CENTER;
            mainPanel.add(titleLabel, gbc);
            
            // campo para username
            JLabel usernameLabel = new JLabel("Nombre de Usuario:");
            usernameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            gbc.gridy = 1; gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(0, 0, 5, 0);
            mainPanel.add(usernameLabel, gbc);
            
            usernameField = new JTextField(20);
            usernameField.setFont(new Font("Arial", Font.PLAIN, 12));
            usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
            gbc.gridy = 2; gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(0, 0, 15, 0);
            mainPanel.add(usernameField, gbc);
            
            // campo para nombre
            JLabel nombreLabel = new JLabel("Nombre Completo:");
            nombreLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            gbc.gridy = 3; gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.NONE;
            gbc.insets = new Insets(0, 0, 5, 0);
            mainPanel.add(nombreLabel, gbc);
            
            nombreField = new JTextField(20);
            nombreField.setFont(new Font("Arial", Font.PLAIN, 12));
            nombreField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
            gbc.gridy = 4; gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(0, 0, 15, 0);
            mainPanel.add(nombreField, gbc);
            
            //campo para el
            JLabel rolLabel = new JLabel("Rol:");
            rolLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            gbc.gridy = 5; gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.NONE;
            gbc.insets = new Insets(0, 0, 5, 0);
            mainPanel.add(rolLabel, gbc);
            
            String[] roles = {"VENDEDOR", "CAJERO", "ADMIN"};
            rolComboBox = new JComboBox<>(roles);
            rolComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
            gbc.gridy = 6; gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(0, 0, 15, 0);
            mainPanel.add(rolComboBox, gbc);
            
            // campo para password
            JLabel passwordLabel = new JLabel("Contraseña:");
            passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            gbc.gridy = 7; gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.NONE;
            gbc.insets = new Insets(0, 0, 5, 0);
            mainPanel.add(passwordLabel, gbc);
            
            passwordField = new JPasswordField(20);
            passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
            passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
            gbc.gridy = 8; gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(0, 0, 15, 0);
            mainPanel.add(passwordField, gbc);
            
            //campo para confirmar password
            JLabel confirmPasswordLabel = new JLabel("Confirmar Contraseña:");
            confirmPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            gbc.gridy = 9; gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.NONE;
            gbc.insets = new Insets(0, 0, 5, 0);
            mainPanel.add(confirmPasswordLabel, gbc);
            
            confirmPasswordField = new JPasswordField(20);
            confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 12));
            confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
            gbc.gridy = 10; gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(0, 0, 20, 0);
            mainPanel.add(confirmPasswordField, gbc);
            
            // botones
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.setBackground(Color.WHITE);
            
            JButton registrarButton = new JButton("Registrar");
            registrarButton.setFont(new Font("Arial", Font.BOLD, 12));
            registrarButton.setForeground(Color.WHITE);
            registrarButton.setBackground(new Color(70, 130, 180));
            registrarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            registrarButton.setFocusPainted(false);
            registrarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            JButton cancelarButton = new JButton("Cancelar");
            cancelarButton.setFont(new Font("Arial", Font.PLAIN, 12));
            cancelarButton.setForeground(new Color(100, 100, 100));
            cancelarButton.setBackground(Color.WHITE);
            cancelarButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
            ));
            cancelarButton.setFocusPainted(false);
            cancelarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Eventos de botones
            registrarButton.addActionListener(e -> realizarRegistro());
            cancelarButton.addActionListener(e -> dispose());
            
            buttonPanel.add(registrarButton);
            buttonPanel.add(cancelarButton);
            
            gbc.gridy = 11; gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(0, 0, 0, 0);
            mainPanel.add(buttonPanel, gbc);
            
            add(mainPanel);
        }
        
        private void realizarRegistro() {
            String username = usernameField.getText().trim();
            String nombre = nombreField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String rol = (String) rolComboBox.getSelectedItem();
            
            // validar
            if (username.isEmpty() || nombre.isEmpty() || password.isEmpty()) {
                mostrarError("Todos los campos son obligatorios");
                return;
            }
            
            if (username.length() < 3) {
                mostrarError("El nombre de usuario debe tener al menos 3 caracteres");
                return;
            }
            
            if (password.length() < 4) {
                mostrarError("La contraseña debe tener al menos 4 caracteres");
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                mostrarError("Las contraseñas no coinciden");
                return;
            }
            
            // verificar si el usuario ya existe
            if (repositorioUsuarios.buscarPorUsername(username) != null) {
                mostrarError("El nombre de usuario ya existe");
                return;
            }
            
            // crear y guardar usuario
            Usuario nuevoUsuario = new Usuario(username, password, nombre, rol);
            boolean exito = repositorioUsuarios.agregarUsuario(nuevoUsuario);
            
            if (exito) {
                JOptionPane.showMessageDialog(this, 
                    "Usuario registrado exitosamente", 
                    "Registro Exitoso", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                mostrarError("Error al registrar el usuario");
            }
        }
        
        private void mostrarError(String mensaje) {
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}