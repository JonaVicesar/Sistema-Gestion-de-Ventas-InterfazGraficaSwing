package com.ventas.vista;

import com.ventas.auth.Usuario;
import com.ventas.repositorio.RepositorioUsuarios;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Jona Vicesar
 */
public class RegistroDialog extends JDialog {

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

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setLayout(new GridBagLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();

        // titulo
        JLabel titleLabel = new JLabel("Registro de Usuario");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(70, 70, 70));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 25, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        panelPrincipal.add(titleLabel, gbc);

        // campo para username
        JLabel usernameLabel = new JLabel("Nombre de Usuario:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        panelPrincipal.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 12));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        panelPrincipal.add(usernameField, gbc);

        // campo para nombre
        JLabel nombreLabel = new JLabel("Nombre Completo:");
        nombreLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 5, 0);
        panelPrincipal.add(nombreLabel, gbc);

        nombreField = new JTextField(20);
        nombreField.setFont(new Font("Arial", Font.PLAIN, 12));
        nombreField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        panelPrincipal.add(nombreField, gbc);

        //campo para el
        JLabel rolLabel = new JLabel("Rol:");
        rolLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 5, 0);
        panelPrincipal.add(rolLabel, gbc);

        String[] roles = {"VENDEDOR", "CAJERO", "ADMIN"};
        rolComboBox = new JComboBox<>(roles);
        rolComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        panelPrincipal.add(rolComboBox, gbc);

        // campo para password
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 5, 0);
        panelPrincipal.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        panelPrincipal.add(passwordField, gbc);

        //campo para confirmar password
        JLabel confirmPasswordLabel = new JLabel("Confirmar Contraseña:");
        confirmPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 5, 0);
        panelPrincipal.add(confirmPasswordLabel, gbc);

        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 12));
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 0);
        panelPrincipal.add(confirmPasswordField, gbc);

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

        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        panelPrincipal.add(buttonPanel, gbc);

        add(panelPrincipal);
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
