package com.ventas.vista;

import com.ventas.util.RoundedBorder;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.ventas.auth.Usuario;
import com.ventas.controlador.ControladorCliente;
import com.ventas.controlador.ControladorProducto;
import com.ventas.controlador.ControladorVenta;
import com.ventas.modelo.Venta;
import com.ventas.repositorio.RepositorioCliente;
import com.ventas.repositorio.RepositorioProductos;
import com.ventas.repositorio.RepositorioVentas;
import com.ventas.util.PathManager;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MenuPrincipal extends JFrame {

    private JPanel panelHeader;
    private JPanel panelNavegacion;
    private JPanel panelContenido;
    private JLabel lblTitulo;
    private JButton btnClientes;
    private JButton btnProductos;
    private JButton btnVentas;
    private JButton btnAboutUs;
    private JButton btnReportes;
    private CardLayout cardLayout;

    private JPanel panelBienvenida;
    private JPanel panelClientes;
    private JPanel panelProductos;
    private JPanel panelVentas;
    private JPanel panelAboutUs;
    private JPanel panelReportes;
    private Usuario usuarioLogueado;

    private RepositorioCliente repoClientes = new RepositorioCliente();
    private RepositorioProductos repoProductos = new RepositorioProductos();
    private RepositorioVentas repoVentas = new RepositorioVentas();

    private ControladorCliente controladorClientes;
    private ControladorProducto controladorProductos;
    private ControladorVenta controladorVentas;

    //imagen de fondo
    private ImageIcon imagenFondo;

    //colores del programa
    private final Color COLOR_PRIMARIO = new Color(33, 150, 243);
    private final Color COLOR_SECUNDARIO = new Color(25, 118, 210);
    private final Color COLOR_ACENTO = new Color(255, 193, 7);
    private final Color COLOR_FONDO_OSCURO = new Color(37, 37, 37);
    private final Color COLOR_FONDO_CLARO = new Color(250, 250, 250);
    private final Color COLOR_TEXTO_CLARO = Color.WHITE;
    private final Color COLOR_HOVER = new Color(66, 165, 245);

    public MenuPrincipal() {
        controladorClientes = new ControladorCliente(repoClientes);
        controladorProductos = new ControladorProducto(repoProductos);
        controladorVentas = new ControladorVenta(repoVentas, repoProductos, repoClientes);
        inicializarComponentes();
        configurarVentana();
        configurarEventos();
    }

    public MenuPrincipal(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
        controladorClientes = new ControladorCliente(repoClientes);
        controladorProductos = new ControladorProducto(repoProductos);
        controladorVentas = new ControladorVenta(repoVentas, repoProductos, repoClientes);
        inicializarComponentes();
        configurarVentana();
        configurarEventos();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        crearPanelHeader();

        crearPanelNavegacion();

        crearPanelContenido();

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelHeader, BorderLayout.NORTH);
        panelSuperior.add(panelNavegacion, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelContenido, BorderLayout.CENTER);

        setImagenFondo("fondo.jpg");
    }

    private void crearPanelHeader() {
        panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_FONDO_OSCURO);
        panelHeader.setPreferredSize(new Dimension(0, 90));
        panelHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_ACENTO),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        //titulo y nombre de la empresa
        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelIzquierdo.setOpaque(false);

        JLabel lblLogo = new JLabel("V");
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        lblLogo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        lblTitulo = new JLabel("Vicesar Market");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(COLOR_TEXTO_CLARO);

        JLabel lblSubtitulo = new JLabel("Sistema de Gestión Comercial");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(170, 170, 170));

        JPanel panelTitulos = new JPanel();
        panelTitulos.setLayout(new BoxLayout(panelTitulos, BoxLayout.Y_AXIS));
        panelTitulos.setOpaque(false);
        panelTitulos.add(lblTitulo);
        panelTitulos.add(lblSubtitulo);

        panelIzquierdo.add(lblLogo);
        panelIzquierdo.add(panelTitulos);

        //cerrar sesion y usuario
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setOpaque(false);
        panelDerecho.setAlignmentY(Component.CENTER_ALIGNMENT);

        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelUsuario.setOpaque(false);

        JButton btnUsuario = crearBotonUsuario();
        JButton btnCerrarSesion = crearBotonCerrarSesion();

        panelUsuario.add(btnUsuario);
        panelUsuario.add(btnCerrarSesion);

        panelDerecho.add(Box.createVerticalStrut(5));
        panelDerecho.add(panelUsuario);

        panelHeader.add(panelIzquierdo, BorderLayout.WEST);
        panelHeader.add(panelDerecho, BorderLayout.EAST);
    }

    private void crearPanelNavegacion() {
        panelNavegacion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        panelNavegacion.setBackground(new Color(50, 50, 50));
        panelNavegacion.setPreferredSize(new Dimension(0, 70));
        panelNavegacion.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(80, 80, 80)));

        btnClientes = crearBotonNavegacion("Clientes", "Gestionar clientes");
        btnProductos = crearBotonNavegacion("Productos", "Gestionar inventario");
        btnVentas = crearBotonNavegacion("Ventas", "Realizar ventas");
        btnReportes = crearBotonNavegacion("Reportes", "Ver estadísticas");
        btnAboutUs = crearBotonNavegacion("Acerca de", "Información del sistema");

        panelNavegacion.add(btnClientes);
        panelNavegacion.add(btnProductos);
        panelNavegacion.add(btnVentas);
        panelNavegacion.add(btnReportes);
        panelNavegacion.add(btnAboutUs);
    }

    private void crearPanelContenido() {
        panelContenido = new JPanel();
        cardLayout = new CardLayout();
        panelContenido.setLayout(cardLayout);

        // Crear paneles para cada sección
        panelBienvenida = crearPanelBienvenida();
        panelClientes = crearPanelSeccion("Clientes", "👥");
        panelProductos = crearPanelSeccion("Productos", "📦");
        panelVentas = crearPanelSeccion("Ventas", "💰");
        panelReportes = crearPanelReportes();
        panelAboutUs = crearPanelAcercaDe();

        panelContenido.add(panelBienvenida, "bienvenida");
        panelContenido.add(panelClientes, "clientes");
        panelContenido.add(panelProductos, "productos");
        panelContenido.add(panelVentas, "ventas");
        panelContenido.add(panelReportes, "reportes");
        panelContenido.add(panelAboutUs, "about");
    }

    private JButton crearBotonUsuario() {
        String nombreUsuario = (usuarioLogueado != null) ? usuarioLogueado.getNombre() : "Usaurio";
        JButton btnUsuario = new JButton(nombreUsuario);
        btnUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnUsuario.setForeground(COLOR_TEXTO_CLARO);
        btnUsuario.setBackground(COLOR_PRIMARIO);
        btnUsuario.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(8),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btnUsuario.setFocusPainted(false);
        btnUsuario.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnUsuario.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnUsuario.setBackground(COLOR_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnUsuario.setBackground(COLOR_PRIMARIO);
            }
        });

        return btnUsuario;
    }

    private JButton crearBotonCerrarSesion() {
        JButton btnCerrar = new JButton("Cerrar Sesion");
        btnCerrar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCerrar.setForeground(COLOR_TEXTO_CLARO);
        btnCerrar.setBackground(new Color(244, 67, 54));
        btnCerrar.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(8),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnCerrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCerrar.setBackground(new Color(229, 57, 53));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnCerrar.setBackground(new Color(244, 67, 54));
            }
        });

        btnCerrar.addActionListener(e -> confirmarCierreSesion());

        return btnCerrar;
    }

    private void confirmarCierreSesion() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea cerrar sesión?",
                "Confirmar cierre de sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private JPanel crearPanelBienvenida() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel para la imagen de fondo
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (imagenFondo != null) {
                    // Aplicar overlay oscuro para mejor legibilidad
                    g2d.drawImage(imagenFondo.getImage(), 0, 0, getWidth(), getHeight(), this);
                    g2d.setColor(new Color(0, 0, 0, 120));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    // Gradiente por defecto
                    GradientPaint gradient = new GradientPaint(
                            0, 0, COLOR_PRIMARIO,
                            getWidth(), getHeight(), COLOR_ACENTO
                    );
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
                g2d.dispose();
            }
        };
        panelFondo.setLayout(new BorderLayout());

        // panel de contenido principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setOpaque(false);

        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));


        JPanel panelBienvenidaContent = crearPanelBienvenidaContent();
        panelIzquierdo.add(panelBienvenidaContent);
        panelIzquierdo.add(Box.createVerticalStrut(30));

     
        JPanel panelStats = crearPanelEstadisticasMejorado();
        panelIzquierdo.add(panelStats);
        panelIzquierdo.add(Box.createVerticalStrut(30));


        JPanel panelAccesosRapidos = crearPanelAccesosRapidos();
        panelIzquierdo.add(panelAccesosRapidos);

        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setOpaque(false);
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panelDerecho.setPreferredSize(new Dimension(400, 0)); // Ancho preferido

        // tiutlo
        JLabel lblTituloEmpresa = new JLabel("Vicesar SA");
        lblTituloEmpresa.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTituloEmpresa.setForeground(Color.WHITE);
        lblTituloEmpresa.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextArea txtDescripcion = new JTextArea();
        txtDescripcion.setText(
                "Acerca de Vicesar CO\n\n"
                + "Vicesar Market forma parte del imperio transnacional Vicesar CO. & Associates, un megaconglomerado con inversiones en los sectores más estratégicos del universo conocido.\n\n"
                + "Desde tecnología cuántica hasta ganadería interplanetaria, pasando por minería de datos, extracción de kriptonita y cultivo de yerba mate en Marte, Vicesar CO no conoce límites.\n\n"
                + "Entre sus principales divisiones se encuentran:\n"
                + "- Vicesar Bank™:.\n\n"
                + "- Vicesar Tech™: Pioneros en IA que predice el futuro (con un 3% de margen de error).\n"
                + "- Vicesar Beef™: Líder mundial en carne clonada de vaca feliz.\n"
                + "- Vicesar Space™: Transporte interdimensional con precios accesibles.\n"
                + "- Vicesar Agro™: Cultivamos, cosechamos y controlamos el clima. Todo desde una app.\n"
                + "- Vicesar Media™: Propietarios ahora tambíen de Disney.\n\n"
        );
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtDescripcion.setForeground(COLOR_TEXTO_CLARO);
        txtDescripcion.setOpaque(false);
        txtDescripcion.setEditable(false);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtDescripcion.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelTextoEmpresa = new JPanel();
        //panelDerecho.add(lblTituloEmpresa);
        panelDerecho.add(Box.createVerticalStrut(10));
        panelDerecho.add(txtDescripcion);

        panelDerecho.add(Box.createVerticalStrut(20));
        panelDerecho.add(panelTextoEmpresa);

        panelPrincipal.add(panelIzquierdo, BorderLayout.WEST);
        panelPrincipal.add(panelDerecho, BorderLayout.CENTER); // Panel derecho en el centro

        panelFondo.add(panelPrincipal, BorderLayout.CENTER);
        panel.add(panelFondo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelBienvenidaContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);

        String nombreUsuario = (usuarioLogueado != null) ? usuarioLogueado.getNombre() : "Usuario";

        JLabel lblBienvenida = new JLabel("¡Bienvenido, " + nombreUsuario + "!");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblBienvenida.setForeground(Color.WHITE);

        panel.add(lblBienvenida);
        panel.add(Box.createVerticalStrut(10));

        return panel;
    }

    private JPanel crearPanelEstadisticasMejorado() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 30, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(800, 120));

        panel.add(crearTarjetaEstadisticaMejorada("👥", "Clientes", controladorClientes.cantidadClientes(), COLOR_PRIMARIO));
        panel.add(crearTarjetaEstadisticaMejorada("📦", "Productos", controladorProductos.cantidadProductos(), COLOR_ACENTO));
        panel.add(crearTarjetaEstadisticaMejorada("💰", "Ventas", controladorVentas.cantidadVentas(), new Color(76, 175, 80)));

        return panel;
    }

    private JPanel crearTarjetaEstadisticaMejorada(String icono, String titulo, Integer valor, Color colorAccento) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 255, 255, 240));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblValor = new JLabel(valor.toString());
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblValor.setForeground(colorAccento);
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTitulo.setForeground(new Color(80, 80, 80));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblIcono);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblValor);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblTitulo);

        return panel;
    }

    private JPanel crearPanelAccesosRapidos() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        panel.setOpaque(false);

        JButton btnAccesoClientes = crearBotonAccesoRapido("👥", "Gestionar Clientes");
        JButton btnAccesoProductos = crearBotonAccesoRapido("📦", "Ver Productos");
        JButton btnAccesoVentas = crearBotonAccesoRapido("💰", "Nueva Venta");

        btnAccesoClientes.addActionListener(e -> abrirMenuClientes());
        btnAccesoProductos.addActionListener(e -> abrirMenuProductos());
        btnAccesoVentas.addActionListener(e -> abrirMenuVentas());

        panel.add(btnAccesoClientes);
        panel.add(btnAccesoProductos);
        panel.add(btnAccesoVentas);

        return panel;
    }

    private JButton crearBotonAccesoRapido(String icono, String texto) {
        JButton boton = new JButton();
        boton.setLayout(new BoxLayout(boton, BoxLayout.Y_AXIS));
        boton.setBackground(new Color(255, 255, 255, 200));
        boton.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(12),
                BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTexto = new JLabel(texto);
        lblTexto.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTexto.setForeground(new Color(60, 60, 60));
        lblTexto.setAlignmentX(Component.CENTER_ALIGNMENT);

        boton.add(lblIcono);
        boton.add(Box.createVerticalStrut(5));
        boton.add(lblTexto);

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(new Color(255, 255, 255, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(new Color(255, 255, 255, 200));
            }
        });

        return boton;
    }

    private JPanel crearPanelReportes() {
        JPanel panel = crearPanelSeccionBase("Informes");

        JPanel contenido = new JPanel(new GridBagLayout());
        contenido.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        String[] reportes = {
            "Ventas Anuladas",
        };

        for (int i = 0; i < reportes.length; i++) {
            final int indice = i;
            JButton btnReporte = crearBotonReporte(reportes[i]);
            btnReporte.addActionListener(e -> {
                mostrarReporte(reportes[indice]);
            });
            gbc.gridx = i % 2;
            gbc.gridy = i / 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            contenido.add(btnReporte, gbc);
        }

        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }

    private JButton crearBotonReporte(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        boton.setBackground(COLOR_FONDO_CLARO);
        boton.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(8),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(250, 80));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(new Color(240, 240, 240));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(COLOR_FONDO_CLARO);
            }
        });

        return boton;
    }

    private void mostrarReporte(String tipoReporte) {
        switch (tipoReporte) {
            case "Ventas Anuladas":
                mostrarVentasAnuladas();
                break;
                
                //no pude implementar los demas reportes tdv
        }
    }

    private void mostrarVentasAnuladas() {
        List<Venta> ventasAnuladas = controladorVentas.listarVentasAnuladas();

        // Crear modelo de tabla
        String[] columnas = {"ID", "Cliente", "Total", "Fecha", "Estado"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Venta venta : ventasAnuladas) {
            Object[] fila = {
                venta.getIdVenta(),
                venta.getCliente().getNombreCompleto(),
                String.format("$%.2f", venta.getTotal()),
                venta.getFecha().format(formatter),
                venta.isAnulada() ? "ANULADA" : "ACTIVA"
            };
            model.addRow(fila);
        }


        JTable tabla = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabla);

        JDialog dialog = new JDialog(this, "Ventas Anuladas", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialog.dispose());
        dialog.add(btnCerrar, BorderLayout.SOUTH);

        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel crearPanelAcercaDe() {
        JPanel panel = crearPanelSeccionBase("Acerca del Sistema");

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(Color.WHITE);
        contenido.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel lblTitulo = new JLabel("Sistema de Gestión de Ventas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblVersion = new JLabel("Versión 1.0");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblVersion.setForeground(new Color(120, 120, 120));
        lblVersion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea txtDescripcion = new JTextArea();
        txtDescripcion.setText("Desarrollado por Jona Vicesar\n\n"
                + "Este sistema permite la gestión de:\n"
                + "• Clientes\n"
                + "• Inventario de productos\n"
                + "• Proceso de ventas\n"
                + "• Reportes y estadísticas\n"
                + "© 2025 Vicesar CO & Associates . Todos los derechos reservados.");
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescripcion.setEditable(false);
        txtDescripcion.setOpaque(false);
        txtDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);

        contenido.add(lblTitulo);
        contenido.add(Box.createVerticalStrut(10));
        contenido.add(lblVersion);
        contenido.add(Box.createVerticalStrut(30));
        contenido.add(txtDescripcion);

        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelSeccionBase(String titulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO_CLARO);

        // Panel superior con título y botón de volver
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(COLOR_FONDO_OSCURO);
        panelSuperior.setPreferredSize(new Dimension(0, 80));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTituloSeccion = new JLabel(titulo);
        lblTituloSeccion.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTituloSeccion.setForeground(COLOR_TEXTO_CLARO);

        JButton btnVolver = new JButton("Volver al Inicio");
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnVolver.setForeground(COLOR_TEXTO_CLARO);
        btnVolver.setBackground(COLOR_PRIMARIO);
        btnVolver.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(6),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnVolver.addActionListener(e -> cardLayout.show(panelContenido, "bienvenida"));

        btnVolver.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnVolver.setBackground(COLOR_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnVolver.setBackground(COLOR_PRIMARIO);
            }
        });

        panelSuperior.add(lblTituloSeccion, BorderLayout.WEST);
        panelSuperior.add(btnVolver, BorderLayout.EAST);

        panel.add(panelSuperior, BorderLayout.NORTH);

        return panel;
    }

    private JPanel crearPanelSeccion(String titulo, String icono) {
        JPanel panel = crearPanelSeccionBase(icono + " " + titulo);

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(Color.WHITE);
        contenido.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblMensaje = new JLabel("no funciona aun");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        lblMensaje.setForeground(new Color(120, 120, 120));

        contenido.add(lblMensaje, BorderLayout.CENTER);
        panel.add(contenido, BorderLayout.CENTER);

        return panel;
    }

    private JButton crearBotonNavegacion(String texto, String tooltip) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setForeground(COLOR_TEXTO_CLARO);
        boton.setBackground(new Color(70, 70, 70));
        boton.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(8),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setToolTipText(tooltip);

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(COLOR_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(new Color(70, 70, 70));
            }
        });

        return boton;
    }

    private void configurarVentana() {
        setTitle("Vicesar CO - Sistema de Gestión Comercial");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 800));
        setLocationRelativeTo(null);

        // Icono de la aplicación
        try {
            URL iconURL = getClass().getResource("/resources/icon.png");
            if (iconURL != null) {
                setIconImage(new ImageIcon(iconURL).getImage());
            }
        } catch (Exception e) {
            // Usar icono por defecto si no se encuentra el archivo
        }
    }

    private void configurarEventos() {
        btnClientes.addActionListener(e -> abrirMenuClientes());
        btnProductos.addActionListener(e -> abrirMenuProductos());
        btnVentas.addActionListener(e -> abrirMenuVentas());
        btnReportes.addActionListener(e -> cardLayout.show(panelContenido, "reportes"));
        btnAboutUs.addActionListener(e -> cardLayout.show(panelContenido, "about"));
    }

    private void abrirMenuClientes() {
        //guardar tamano de la ventana
        int estadoActual = this.getExtendedState();
        Point ubicacion = this.getLocation();
        Dimension tamaño = this.getSize();

        this.setVisible(false);

        MenuClientes menuClientes = new MenuClientes(this);

        //abrir maximizada si el menu principal lo esta
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

        //abrir maximizada si el menu principal lo esta
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

        //abrir maximizada si el menu principal lo esta
        if ((estadoActual & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
            menuVentas.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            menuVentas.setSize(tamaño);
            menuVentas.setLocation(ubicacion);
        }

        menuVentas.setVisible(true);

    }

    private void setImagenFondo(String nombreImagen) {
        try {
            if (nombreImagen != null && !nombreImagen.isEmpty()) {
                String ruta = PathManager.getResourcePath(nombreImagen);
                File imageFile = new File(ruta);

                if (imageFile.exists()) {
                    imagenFondo = new ImageIcon(ImageIO.read(imageFile));
                    System.out.println("carga");
                } else {
                    System.err.println("no carga la imagen");
                    imagenFondo = null;
                }
            }
        } catch (Exception e) {
            System.err.println("no cargo" + e.getMessage());
            imagenFondo = null;
        }
    }
}
