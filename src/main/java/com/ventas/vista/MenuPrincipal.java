package com.ventas.vista;

import com.ventas.auth.Usuario;
import com.ventas.controlador.ControladorCliente;
import com.ventas.controlador.ControladorProducto;
import com.ventas.controlador.ControladorVenta;
import com.ventas.repositorio.RepositorioCliente;
import com.ventas.repositorio.RepositorioProductos;
import com.ventas.repositorio.RepositorioVentas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class MenuPrincipal extends JFrame {

    private JPanel panelHeader;
    private JPanel panelNavegacion;
    private JPanel panelContenido;
    private JLabel lblTitulo;
    private JButton btnClientes;
    private JButton btnProductos;
    private JButton btnVentas;
    private JButton btnAboutUs;
    private CardLayout cardLayout;

    private JPanel panelBienvenida;
    private JPanel panelClientes;
    private JPanel panelProductos;
    private JPanel panelVentas;
    private JPanel panelAboutUs;
    private JPanel panelEstadisticas;
    private Usuario usuarioLogueado;

    // Controladores
    private RepositorioCliente repoClientes = new RepositorioCliente();
    private RepositorioProductos repoProductos = new RepositorioProductos();
    private RepositorioVentas repoVentas = new RepositorioVentas();

    private ControladorCliente controladorClientes;
    private ControladorProducto controladorProductos;
    private ControladorVenta controladorVentas;

    // Imagen de fondo para el panel de bienvenida
    private ImageIcon imagenFondo;

    public MenuPrincipal() {
        controladorClientes = new ControladorCliente(repoClientes);
        controladorProductos = new ControladorProducto(repoProductos);
        controladorVentas = new ControladorVenta(repoVentas, repoProductos, repoClientes);
        inicializarControladores();
        inicializarComponentes();
        configurarVentana();
        configurarEventos();
    }

    public MenuPrincipal(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
        controladorClientes = new ControladorCliente(repoClientes);
        controladorProductos = new ControladorProducto(repoProductos);
        controladorVentas = new ControladorVenta(repoVentas, repoProductos, repoClientes);
        inicializarControladores();
        inicializarComponentes();
        configurarVentana();
        configurarEventos();
        
    }

    private void inicializarControladores() {
        this.controladorClientes = new ControladorCliente();
        this.controladorProductos = new ControladorProducto();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        // 1. Panel de Cabecera
        panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(45, 45, 45));
        panelHeader.setPreferredSize(new Dimension(0, 80));

        lblTitulo = new JLabel("Vicesar SA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20));

        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelUsuario.setOpaque(false);
        JButton btnUsuario = new JButton();
        btnUsuario.setText((usuarioLogueado != null) ? "Usuario: " + usuarioLogueado.getNombre() : "Usuario: " + "nn");
        btnUsuario.setForeground(Color.WHITE);
        btnUsuario.setBackground(new Color(70, 70, 70));
        btnUsuario.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        panelUsuario.add(btnUsuario);

        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelHeader.add(panelUsuario, BorderLayout.EAST);

        // 2. Panel de Navegación - POSICIÓN ORIGINAL (debajo del header)
        panelNavegacion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 15));
        panelNavegacion.setBackground(new Color(60, 60, 60));
        panelNavegacion.setPreferredSize(new Dimension(0, 60));

        btnClientes = crearBotonNavegacion("Clientes");
        btnProductos = crearBotonNavegacion("Productos");
        btnVentas = crearBotonNavegacion("Ventas");
        btnAboutUs = crearBotonNavegacion("Sobre Nosotros");

        panelNavegacion.add(btnClientes);
        panelNavegacion.add(btnProductos);
        panelNavegacion.add(btnVentas);
        panelNavegacion.add(btnAboutUs);

        // 3. Panel de Contenido Principal
        panelContenido = new JPanel();
        cardLayout = new CardLayout();
        panelContenido.setLayout(cardLayout);

        // Crear paneles para cada sección
        panelBienvenida = crearPanelBienvenida();
        panelClientes = crearPanelSeccion("Clientes");
        panelProductos = crearPanelSeccion("Productos");
        panelVentas = crearPanelSeccion("Ventas");
        panelAboutUs = crearPanelSeccion("Sobre Nosotros");

        panelContenido.add(panelBienvenida, "bienvenida");
        panelContenido.add(panelClientes, "clientes");
        panelContenido.add(panelProductos, "productos");
        panelContenido.add(panelVentas, "ventas");
        panelContenido.add(panelAboutUs, "about");

        // 4. Crear panel contenedor para header y navegación
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelHeader, BorderLayout.NORTH);
        panelSuperior.add(panelNavegacion, BorderLayout.SOUTH);

        // Agregar componentes a la ventana principal
        add(panelSuperior, BorderLayout.NORTH);
        add(panelContenido, BorderLayout.CENTER);
    }

    private void abrirMenuClientes() {
        // Guardar el estado de la ventana actual
        int estadoActual = this.getExtendedState();
        Point ubicacion = this.getLocation();
        Dimension tamaño = this.getSize();

        this.setVisible(false);

        MenuClientes menuClientes = new MenuClientes(this);

        // Aplicar el mismo estado a la nueva ventana
        if ((estadoActual & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
            menuClientes.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            menuClientes.setSize(tamaño);
            menuClientes.setLocation(ubicacion);
        }

        menuClientes.setVisible(true);

    }

    private void abrirMenuProductos() {
        int estadoActual = this.getExtendedState();
        Point ubicacion = this.getLocation();
        Dimension tamaño = this.getSize();

        this.setVisible(false);
        MenuProductos menuProductos = new MenuProductos(this);

        if ((estadoActual & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
            menuProductos.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            menuProductos.setSize(tamaño);
            menuProductos.setLocation(ubicacion);
        }

        menuProductos.setVisible(true);

    }

    private void abrirMenuVentas() {
        int estadoActual = this.getExtendedState();
        Point ubicacion = this.getLocation();
        Dimension tamaño = this.getSize();

        this.setVisible(false);
        MenuVentas menuVentas = new MenuVentas(this, controladorClientes, controladorProductos, controladorVentas);

        if ((estadoActual & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
            menuVentas.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            menuVentas.setSize(tamaño);
            menuVentas.setLocation(ubicacion);
        }

        menuVentas.setVisible(true);

    }

    private JPanel crearPanelBienvenida() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel para la imagen de fondo
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenFondo != null) {
                    g.drawImage(imagenFondo.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Fondo por defecto si no hay imagen
                    g.setColor(new Color(70, 70, 70));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        panelFondo.setLayout(new BorderLayout());

        // Panel de texto superpuesto (ahora con texto significativo)
        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setOpaque(false);
        panelTexto.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel lblBienvenida = new JLabel("Bienvenido " + usuarioLogueado.getNombre() + "!!!");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 36));
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("20 Llevando Alegria a tu hogar");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 20));
        lblSubtitulo.setForeground(new Color(220, 220, 220));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubtitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Texto descriptivo
        JTextArea txtDescripcion = new JTextArea();
        txtDescripcion.setText("Con nuestro sistema podrá:\n\n"
                + "• Gestionar su base de clientes\n"
                + "• Controlar el inventario de productos\n"
                + "• Realizar seguimiento de ventas\n"
                + "• Generar informes detallados");
        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 18));
        txtDescripcion.setForeground(Color.WHITE);
        txtDescripcion.setOpaque(false);
        txtDescripcion.setEditable(false);
        txtDescripcion.setAlignmentX(Component.RIGHT_ALIGNMENT);
        txtDescripcion.setBorder(BorderFactory.createEmptyBorder(30, 50, 0, 0));

        // Panel de estadísticas
        JPanel panelStats = new JPanel(new GridLayout(1, 3, 20, 0));
        panelStats.setOpaque(false);
        panelStats.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        panelStats.setAlignmentX(Component.CENTER_ALIGNMENT);

        int i = 9;
        panelStats.add(crearTarjetaEstadistica("Clientes", controladorClientes.cantidadClientes()));
        panelStats.add(crearTarjetaEstadistica("Productos", controladorProductos.cantidadProductos()));
        panelStats.add(crearTarjetaEstadistica("Ventas", controladorVentas.cantidadVentas()));

        panelTexto.add(lblBienvenida);
        panelTexto.add(lblSubtitulo);
        panelTexto.add(txtDescripcion);
        panelTexto.add(panelStats);

        // Agregar todo al panel
        panelFondo.add(panelTexto, BorderLayout.CENTER);
        panel.add(panelFondo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearTarjetaEstadistica(String titulo, Integer valor) {
        panelEstadisticas = new JPanel();
        panelEstadisticas.setLayout(new BoxLayout(panelEstadisticas, BoxLayout.Y_AXIS));
        panelEstadisticas.setBackground(new Color(40, 40, 40, 180));
        panelEstadisticas.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelEstadisticas.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblValor = new JLabel(valor.toString());
        lblValor.setFont(new Font("Arial", Font.BOLD, 28));
        lblValor.setForeground(Color.WHITE);
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        lblTitulo.setForeground(new Color(200, 200, 200));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        panelEstadisticas.add(lblValor);
        panelEstadisticas.add(lblTitulo);

        return panelEstadisticas;
    }

    public void actualizarEstadisticas() {
        panelEstadisticas.removeAll();
        panelEstadisticas.add(crearTarjetaEstadistica("Clientes", controladorClientes.cantidadClientes()));
        panelEstadisticas.add(crearTarjetaEstadistica("Productos", controladorProductos.cantidadProductos()));
        panelEstadisticas.add(crearTarjetaEstadistica("Ventas", controladorVentas.cantidadVentas()));
        panelEstadisticas.revalidate();
        panelEstadisticas.repaint();
    }

    private JPanel crearPanelSeccion(String titulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));

        // Panel superior con título y botón de volver
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(60, 60, 60));
        panelSuperior.setPreferredSize(new Dimension(0, 50));

        JButton btnVolver = new JButton("← Volver al Inicio");
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setBackground(new Color(80, 80, 80));
        btnVolver.setFocusPainted(false);
        btnVolver.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnVolver.addActionListener(e -> cardLayout.show(panelContenido, "bienvenida"));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);

        panelSuperior.add(btnVolver, BorderLayout.WEST);
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);

        // Panel de contenido (aquí irán los componentes específicos de cada sección)
        JPanel panelContenidoSeccion = new JPanel();
        panelContenidoSeccion.setBackground(Color.WHITE);
        panelContenidoSeccion.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Ejemplo de contenido para la sección
        JLabel lblContenido = new JLabel("Contenido de " + titulo + " aparecerá aquí");
        lblContenido.setHorizontalAlignment(SwingConstants.CENTER);
        lblContenido.setFont(new Font("Arial", Font.PLAIN, 18));
        panelContenidoSeccion.add(lblContenido);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(panelContenidoSeccion, BorderLayout.CENTER);

        return panel;
    }

    private JButton crearBotonNavegacion(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setForeground(Color.WHITE);
        boton.setBackground(new Color(60, 60, 60));
        boton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(new Color(80, 80, 80));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(new Color(60, 60, 60));
            }
        });

        return boton;
    }

    private void configurarVentana() {
        setTitle("Sistema de Ventas - Vicesar SA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void configurarEventos() {
        btnClientes.addActionListener(e -> abrirMenuClientes());
        btnProductos.addActionListener(e -> abrirMenuProductos());
        btnVentas.addActionListener(e -> abrirMenuVentas());
        btnAboutUs.addActionListener(e -> cardLayout.show(panelContenido, "about"));
    }

    // Método para establecer la imagen de fondo
    public void setImagenFondo(String rutaImagen) {
        try {
            // Try to load from resources first
            URL imageURL = getClass().getResource("/util/mueble-caja-para-supermercado.jpg");
            if (imageURL != null) {
                imagenFondo = new ImageIcon(imageURL);
            } else {
                imagenFondo = new ImageIcon(rutaImagen);
            }
            panelBienvenida.repaint();
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
            // Set a default background color instead
        }
    }
}
