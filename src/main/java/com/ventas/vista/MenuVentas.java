package com.ventas.vista;

import com.ventas.util.RoundedBorder;
import com.ventas.controlador.ControladorCliente;
import com.ventas.controlador.ControladorProducto;
import com.ventas.controlador.ControladorVenta;
import com.ventas.modelo.Venta;
import com.ventas.modelo.Cliente;
import com.ventas.modelo.Producto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Locale;
import java.util.stream.Collectors;

public class MenuVentas extends JFrame {

    private ControladorVenta controladorVentas;
    private ControladorCliente controladorClientes;
    private ControladorProducto controladorProductos;
    private JTextField campoBusqueda;
    private JTable tablaVentas;
    private DefaultTableModel modeloTabla;
    private JPanel panelPrincipal;
    private JPanel panelIzquierdo;
    private JPanel panelDerecho;

    private final Color COLOR_ACENTO = new Color(255, 193, 7);
    private final Color COLOR_FONDO_CLARO = new Color(250, 250, 250);
    private final Color COLOR_FONDO = new Color(37, 37, 37);
    private final Color COLOR_PANEL = new Color(50, 50, 50);
    private final Color COLOR_BOTON = new Color(33, 150, 243);
    private final Color COLOR_BOTON_HOVER = new Color(66, 165, 245);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_BUSQUEDA = new Color(70, 70, 70);
    private final Color COLOR_TABLA_HEADER = new Color(25, 118, 210);
    private final Color COLOR_TABLA_ROW1 = new Color(45, 45, 45);
    private final Color COLOR_TABLA_ROW2 = new Color(40, 40, 40);

    private final MenuPrincipal menuPrincipal;

    private JComboBox<String> comboFiltroCliente;
    private JTextField campoFechaInicio;
    private JTextField campoFechaFin;

    public MenuVentas(MenuPrincipal menuPrincipal,
            ControladorCliente contClientes,
            ControladorProducto conProducto,
            ControladorVenta contVentas) {
        this.menuPrincipal = menuPrincipal;
        this.controladorClientes = contClientes;
        this.controladorProductos = conProducto;
        this.controladorVentas = contVentas;
        inicializarComponentes();
        configurarVentana();
        cargarDatos();
    }

    private void inicializarComponentes() {
        // panel principal
        panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, COLOR_FONDO,
                        getWidth(), getHeight(), COLOR_PANEL);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelPrincipal.setLayout(new BorderLayout());
        crearHeader();

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setOpaque(false);

        crearPanelIzquierdo();//panel de las opciones
        crearPanelDerecho();//panel de tabla de ventas

        panelCentral.add(panelIzquierdo, BorderLayout.WEST);
        panelCentral.add(panelDerecho, BorderLayout.CENTER);

        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        add(panelPrincipal);
    }

    private void volverAlMenuPrincipal() {
        this.dispose();
        menuPrincipal.setVisible(true);
    }

    private void crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(35, 35, 35));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel tituloPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tituloPanel.setOpaque(false);

        JLabel logo = new JLabel("V");
        logo.setFont(new Font("Arial", Font.BOLD, 36));
        logo.setForeground(COLOR_TEXTO);
        logo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        JLabel titulo = new JLabel("Vicesar SA");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(COLOR_TEXTO);

        tituloPanel.add(logo);
        tituloPanel.add(titulo);

        header.add(tituloPanel, BorderLayout.WEST);

        //boton para volver al menu principal
        JButton btnVolver = new JButton("Volver al Inicio");
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnVolver.setForeground(COLOR_TEXTO);
        btnVolver.setBackground(COLOR_BOTON);
        btnVolver.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(6),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btnVolver.addActionListener(e -> volverAlMenuPrincipal());

        header.add(btnVolver, BorderLayout.EAST);

        panelPrincipal.add(header, BorderLayout.NORTH);

        header.add(btnVolver, BorderLayout.EAST);

        panelPrincipal.add(header, BorderLayout.NORTH);
    }

    private void crearPanelIzquierdo() {
        panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBackground(COLOR_PANEL);
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelIzquierdo.setPreferredSize(new Dimension(250, 0));

        JLabel tituloMenu = new JLabel("Ventas");
        tituloMenu.setFont(new Font("Arial", Font.BOLD, 28));
        tituloMenu.setForeground(COLOR_TEXTO);
        tituloMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        tituloMenu.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        //cantidad de ventas
        JLabel lblTotal = new JLabel("Total: " + controladorVentas.listarVentas().size() + " ventas");
        lblTotal.setForeground(COLOR_TEXTO);
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTotal.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        //botones del menu
        JButton btnCrearVenta = crearBotonMenu("Crear Venta");
        JButton btnAnularVenta = crearBotonMenu("Anular Venta");
        JButton btnEditarVenta = crearBotonMenu("Editar Venta");

        // listeners para los botenes del menu
        btnCrearVenta.addActionListener(e -> mostrarDialogoCrearVenta());
        btnAnularVenta.addActionListener(e -> anularVentaSeleccionada());
        btnEditarVenta.addActionListener(e -> editarVentaSeleccionada());

        panelIzquierdo.add(tituloMenu);
        panelIzquierdo.add(lblTotal);
        panelIzquierdo.add(btnCrearVenta);
        panelIzquierdo.add(Box.createVerticalStrut(15));
        panelIzquierdo.add(btnAnularVenta);
        panelIzquierdo.add(Box.createVerticalStrut(15));
        panelIzquierdo.add(btnEditarVenta);
        panelIzquierdo.add(Box.createVerticalGlue());
    }

    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setForeground(COLOR_TEXTO);
        boton.setBackground(COLOR_BOTON);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BOTON.darker(), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        boton.setFocusPainted(false);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(200, 45));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //hover al presionar
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(COLOR_BOTON_HOVER);
                boton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BOTON_HOVER.darker(), 1),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(COLOR_BOTON);
                boton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BOTON.darker(), 1),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }
        });

        return boton;
    }

    private void crearPanelDerecho() {
        panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setOpaque(false);
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);

        panelDerecho.add(panelSuperior, BorderLayout.NORTH);

        // barra de busqueda
        JPanel panelBusqueda = new JPanel(new BorderLayout(10, 0));
        panelBusqueda.setOpaque(false);

        JLabel lblBuscar = new JLabel("Buscar Venta Por ID:");
        lblBuscar.setForeground(COLOR_TEXTO);
        lblBuscar.setFont(new Font("Arial", Font.BOLD, 14));

        campoBusqueda = new JTextField();
        campoBusqueda.setFont(new Font("Arial", Font.PLAIN, 14));
        campoBusqueda.setBackground(COLOR_BUSQUEDA);
        campoBusqueda.setForeground(COLOR_TEXTO);
        campoBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BOTON, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        campoBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarVentas();
            }
        });

        panelBusqueda.add(lblBuscar, BorderLayout.WEST);
        panelBusqueda.add(campoBusqueda, BorderLayout.CENTER);

        JPanel panelFiltros = new JPanel(new GridLayout(2, 1, 5, 5));
        panelFiltros.setOpaque(false);
        panelFiltros.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_BOTON, 1),
                "Filtros Avanzados",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 12),
                COLOR_TEXTO
        ));

// Filtro por cliente
        JPanel panelFiltroCliente = new JPanel(new BorderLayout(5, 0));
        panelFiltroCliente.setOpaque(false);
        JLabel lblFiltroCliente = new JLabel("Cliente:");
        lblFiltroCliente.setForeground(COLOR_TEXTO);
        lblFiltroCliente.setFont(new Font("Arial", Font.BOLD, 12));

        comboFiltroCliente = new JComboBox<>();
        comboFiltroCliente.addItem("Todos los clientes");
// Cargar clientes disponibles
        controladorClientes.listaClientes().forEach(cliente
                -> comboFiltroCliente.addItem(cliente.getNombreCompleto())
        );
        comboFiltroCliente.setBackground(COLOR_BUSQUEDA);
        comboFiltroCliente.setForeground(COLOR_TEXTO);
        comboFiltroCliente.addActionListener(e -> aplicarFiltros());

        panelFiltroCliente.add(lblFiltroCliente, BorderLayout.WEST);
        panelFiltroCliente.add(comboFiltroCliente, BorderLayout.CENTER);

// Filtro por fecha
        JPanel panelFiltroFecha = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltroFecha.setOpaque(false);

        JLabel lblFechaDesde = new JLabel("Desde:");
        lblFechaDesde.setForeground(COLOR_TEXTO);
        lblFechaDesde.setFont(new Font("Arial", Font.BOLD, 12));

        campoFechaInicio = new JTextField(10);
        campoFechaInicio.setBackground(COLOR_BUSQUEDA);
        campoFechaInicio.setForeground(COLOR_TEXTO);
        campoFechaInicio.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BOTON, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        campoFechaInicio.setToolTipText("Formato: dd/MM/yyyy");
        campoFechaInicio.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                aplicarFiltros();
            }
        });

        JLabel lblFechaHasta = new JLabel("Hasta:");
        lblFechaHasta.setForeground(COLOR_TEXTO);
        lblFechaHasta.setFont(new Font("Arial", Font.BOLD, 12));

        campoFechaFin = new JTextField(10);
        campoFechaFin.setBackground(COLOR_BUSQUEDA);
        campoFechaFin.setForeground(COLOR_TEXTO);
        campoFechaFin.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BOTON, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        campoFechaFin.setToolTipText("Formato: dd/MM/yyyy");
        campoFechaFin.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                aplicarFiltros();
            }
        });

        JButton btnLimpiarFiltros = new JButton("Limpiar");
        btnLimpiarFiltros.setFont(new Font("Arial", Font.PLAIN, 11));
        btnLimpiarFiltros.setForeground(COLOR_TEXTO);
        btnLimpiarFiltros.setBackground(COLOR_BOTON);
        btnLimpiarFiltros.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btnLimpiarFiltros.setFocusPainted(false);
        btnLimpiarFiltros.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpiarFiltros.addActionListener(e -> limpiarFiltros());

        panelFiltroFecha.add(lblFechaDesde);
        panelFiltroFecha.add(campoFechaInicio);
        panelFiltroFecha.add(lblFechaHasta);
        panelFiltroFecha.add(campoFechaFin);
        panelFiltroFecha.add(btnLimpiarFiltros);

        panelFiltros.add(panelFiltroCliente);
        panelFiltros.add(panelFiltroFecha);

        // opciones de ordenamiento
        JPanel panelOrdenamiento = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelOrdenamiento.setOpaque(false);

        JLabel lblOrdenar = new JLabel("Ordenar por:");
        lblOrdenar.setForeground(COLOR_TEXTO);
        lblOrdenar.setFont(new Font("Arial", Font.BOLD, 12));

        JButton btnOrdenarCliente = new JButton("Cliente");
        JButton btnOrdenarTotal = new JButton("Total");
        JButton btnOrdenarFecha = new JButton("Fecha");

        JButton[] botonesOrden = {btnOrdenarCliente, btnOrdenarTotal, btnOrdenarFecha};
        for (JButton btn : botonesOrden) {
            btn.setFont(new Font("Arial", Font.PLAIN, 11));
            btn.setForeground(COLOR_TEXTO);
            btn.setBackground(COLOR_BOTON);
            btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        btnOrdenarCliente.addActionListener(e -> ordenarTabla("cliente"));
        btnOrdenarTotal.addActionListener(e -> ordenarTabla("total"));
        btnOrdenarFecha.addActionListener(e -> ordenarTabla("fecha"));

        panelOrdenamiento.add(lblOrdenar);
        panelOrdenamiento.add(btnOrdenarCliente);
        panelOrdenamiento.add(btnOrdenarTotal);
        panelOrdenamiento.add(btnOrdenarFecha);

        panelSuperior.add(panelBusqueda, BorderLayout.NORTH);
        panelSuperior.add(panelOrdenamiento, BorderLayout.SOUTH);
        panelSuperior.add(panelFiltros, BorderLayout.CENTER);

        //tabla de las ventas
        String[] columnas = {"ID", "Cliente", "Total", "Método de Pago", "Fecha"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaVentas = new JTable(modeloTabla) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);

                if (isRowSelected(row)) {
                    comp.setBackground(COLOR_BOTON_HOVER);
                    comp.setForeground(COLOR_TEXTO);
                } else {
                    comp.setBackground(row % 2 == 0 ? COLOR_TABLA_ROW1 : COLOR_TABLA_ROW2);
                    comp.setForeground(COLOR_TEXTO);
                }

                return comp;
            }
        };

        tablaVentas.setRowHeight(35);
        tablaVentas.getTableHeader().setBackground(COLOR_TABLA_HEADER);
        tablaVentas.getTableHeader().setForeground(COLOR_TEXTO);
        tablaVentas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaVentas.setSelectionBackground(COLOR_BOTON_HOVER);
        tablaVentas.setSelectionForeground(COLOR_TEXTO);
        tablaVentas.setGridColor(COLOR_BOTON);
        tablaVentas.setShowGrid(true);
        tablaVentas.setAutoCreateRowSorter(true);
        tablaVentas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tablaVentas.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    int idVenta = (int) modeloTabla.getValueAt(row, 0);
                    mostrarDetallesVenta(idVenta);
                }
            }
        });

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaVentas.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // el ID
        tablaVentas.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // total
        tablaVentas.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); //pago
        tablaVentas.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // fecha

        tablaVentas.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        tablaVentas.getColumnModel().getColumn(1).setPreferredWidth(200); //cliente
        tablaVentas.getColumnModel().getColumn(2).setPreferredWidth(100); // total
        tablaVentas.getColumnModel().getColumn(3).setPreferredWidth(150); // metodo de pago
        tablaVentas.getColumnModel().getColumn(4).setPreferredWidth(120); // fecha

        JScrollPane scrollPane = new JScrollPane(tablaVentas);
        scrollPane.getViewport().setBackground(COLOR_PANEL);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BOTON, 1));

        panelDerecho.add(panelSuperior, BorderLayout.NORTH);
        panelDerecho.add(scrollPane, BorderLayout.CENTER);
    }

    private void configurarVentana() {
        setTitle("Vicesar SA - Gestión de Ventas");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        List<Venta> ventas = controladorVentas.listarVentas();

        for (Venta venta : ventas) {
            agregarFilaTabla(venta);
        }
    }

    private void filtrarVentas() {
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        String filtroBusqueda = campoBusqueda.getText().toLowerCase().trim();
        String filtroCliente = (String) comboFiltroCliente.getSelectedItem();
        String fechaInicio = campoFechaInicio.getText().trim();
        String fechaFin = campoFechaFin.getText().trim();

        modeloTabla.setRowCount(0);
        List<Venta> ventas = controladorVentas.listarVentas();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Venta venta : ventas) {
            boolean cumpleFiltros = true;

            // Filtro por búsqueda (ID o nombre)
            if (!filtroBusqueda.isEmpty()) {
                if (!String.valueOf(venta.getIdVenta()).contains(filtroBusqueda)
                        && !venta.getCliente().getNombreCompleto().toLowerCase().contains(filtroBusqueda)) {
                    cumpleFiltros = false;
                }
            }

            // Filtro por cliente
            if (cumpleFiltros && filtroCliente != null && !filtroCliente.equals("Todos los clientes")) {
                if (!venta.getCliente().getNombreCompleto().equals(filtroCliente)) {
                    cumpleFiltros = false;
                }
            }

            // Filtro por fecha
            if (cumpleFiltros && (!fechaInicio.isEmpty() || !fechaFin.isEmpty())) {
                try {
                    LocalDate fechaVenta = venta.getFecha();

                    if (!fechaInicio.isEmpty()) {
                        LocalDate fechaInicioDate = LocalDate.parse(fechaInicio, formatter);
                        if (fechaVenta.isBefore(fechaInicioDate)) {
                            cumpleFiltros = false;
                        }
                    }

                    if (cumpleFiltros && !fechaFin.isEmpty()) {
                        LocalDate fechaFinDate = LocalDate.parse(fechaFin, formatter);
                        if (fechaVenta.isAfter(fechaFinDate)) {
                            cumpleFiltros = false;
                        }
                    }
                } catch (Exception e) {
                }
            }

            if (cumpleFiltros) {
                agregarFilaTabla(venta);
            }
        }
    }

    private void limpiarFiltros() {
        campoBusqueda.setText("");
        comboFiltroCliente.setSelectedIndex(0);
        campoFechaInicio.setText("");
        campoFechaFin.setText("");
        aplicarFiltros(); // Recargar datos sin filtros
    }

    private void ordenarTabla(String criterio) {
        List<Venta> ventas = controladorVentas.listarVentas();
        String filtroBusqueda = campoBusqueda.getText().trim().toLowerCase();
        String filtroCliente = (String) comboFiltroCliente.getSelectedItem();
        String fechaInicio = campoFechaInicio.getText().trim();
        String fechaFin = campoFechaFin.getText().trim();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Aplicar todos los filtros
        ventas = ventas.stream().filter(venta -> {
            boolean cumple = true;

            // Filtro búsqueda
            if (!filtroBusqueda.isEmpty()) {
                cumple = String.valueOf(venta.getIdVenta()).contains(filtroBusqueda)
                        || venta.getCliente().getNombreCompleto().toLowerCase().contains(filtroBusqueda);
            }

            // Filtro cliente
            if (cumple && filtroCliente != null && !filtroCliente.equals("Todos los clientes")) {
                cumple = venta.getCliente().getNombreCompleto().equals(filtroCliente);
            }

            // Filtro fecha
            if (cumple && (!fechaInicio.isEmpty() || !fechaFin.isEmpty())) {
                try {
                    LocalDate fechaVenta = venta.getFecha();

                    if (!fechaInicio.isEmpty()) {
                        LocalDate fechaInicioDate = LocalDate.parse(fechaInicio, formatter);
                        cumple = !fechaVenta.isBefore(fechaInicioDate);
                    }

                    if (cumple && !fechaFin.isEmpty()) {
                        LocalDate fechaFinDate = LocalDate.parse(fechaFin, formatter);
                        cumple = !fechaVenta.isAfter(fechaFinDate);
                    }
                } catch (Exception e) {
                    // Formato de fecha inválido, ignorar filtro
                }
            }

            return cumple;
        }).collect(Collectors.toList());

        // Ordenar según criterio
        switch (criterio) {
            case "cliente":
                ventas.sort(Comparator.comparing(v -> v.getCliente().getNombreCompleto()));
                break;
            case "total":
                ventas.sort(Comparator.comparing(Venta::getTotal));
                break;
            case "fecha":
                ventas.sort(Comparator.comparing(Venta::getFecha));
                break;
        }

        modeloTabla.setRowCount(0);
        for (Venta venta : ventas) {
            agregarFilaTabla(venta);
        }
    }

    private void agregarFilaTabla(Venta venta) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Object[] fila = {
            venta.getIdVenta(),
            venta.getCliente().getNombreCompleto(),
            String.format("$%.2f", venta.getTotal()),
            venta.getCliente().getMetodoPago(),
            venta.getFecha().format(formatter)
        };
        modeloTabla.addRow(fila);
    }

    private void mostrarDialogoCrearVenta() {
        FormularioVentaDialog dialog = new FormularioVentaDialog(this, null);
        dialog.setVisible(true);
        if (dialog.isGuardado()) {
            cargarDatos();
        }
    }

    private void anularVentaSeleccionada() {
        int filaSeleccionada = tablaVentas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una venta para anular");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de anular esta venta?",
                "Confirmar anulación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            int id = (Integer) modeloTabla.getValueAt(filaSeleccionada, 0);
            try {
                controladorVentas.anularVenta(id);
                cargarDatos();
                JOptionPane.showMessageDialog(this, "Venta anulada correctamente");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarVentaSeleccionada() {
        int filaSeleccionada = tablaVentas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una venta para editar");
            return;
        }

        int idVenta = (Integer) modeloTabla.getValueAt(filaSeleccionada, 0);
        Venta venta = controladorVentas.obtenerVentaPorId(idVenta);
        if (venta == null) {
            JOptionPane.showMessageDialog(this, "No se encontró la venta seleccionada", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        FormularioVentaDialog dialog = new FormularioVentaDialog(this, venta);
        dialog.setVisible(true);
        if (dialog.isGuardado()) {
            cargarDatos(); // Refrescar tabla
        }
    }

    private void mostrarDetallesVenta(int idVenta) {
        Venta venta = controladorVentas.obtenerVentaPorId(idVenta);
        if (venta != null) {
            StringBuilder detalles = new StringBuilder();
            detalles.append("=== DETALLE DE VENTA ===\n\n");
            detalles.append("ID: ").append(venta.getIdVenta()).append("\n");
            detalles.append("Cliente: ").append(venta.getCliente().getNombreCompleto()).append("\n");
            detalles.append("Fecha: ").append(venta.getFecha()).append("\n");
            detalles.append("Total: $").append(String.format("%.2f", venta.getTotal())).append("\n");
            detalles.append("\nProductos:\n");

            for (Map.Entry<Producto, Integer> entry : venta.getListaCompras().entrySet()) {
                Producto p = entry.getKey();
                detalles.append("- ").append(p.getNombre())
                        .append(": ").append(entry.getValue())
                        .append(" x $").append(String.format("%.2f", p.getPrecio()))
                        .append("\n");
            }

            JOptionPane.showMessageDialog(this, detalles.toString(),
                    "Detalles de Venta",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //clase interna para crear ventas
    private class FormularioVentaDialog extends JDialog {

        private Venta venta;
        private boolean guardado = false;

        private JComboBox<Cliente> comboClientes;
        private DefaultTableModel modeloTablaProductos;
        private JTable tablaProductos;
        private JComboBox<Producto> comboProductos;
        private JSpinner spinnerCantidad;
        private JLabel lblTotal;

        public FormularioVentaDialog(JFrame parent, Venta ventaExistente) {
            super(parent, ventaExistente == null ? "Nueva Venta" : "Editar Venta", true);
            this.venta = ventaExistente;
            setSize(600, 500);
            setLocationRelativeTo(parent);
            inicializarComponentes();
        }

        private void inicializarComponentes() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBorder(new EmptyBorder(10, 10, 10, 10));

            // seleccion de clientes
            JPanel panelCliente = new JPanel(new GridLayout(0, 2, 5, 5));
            panelCliente.add(new JLabel("Cliente:"));
            comboClientes = new JComboBox<>();
            controladorClientes.listaClientes().forEach(comboClientes::addItem);
            panelCliente.add(comboClientes);

            panel.add(panelCliente, BorderLayout.NORTH);

            // seleccionar productos
            JPanel panelProductos = new JPanel(new BorderLayout(10, 10));

            String[] columnas = {"Producto", "Precio Unitario", "Cantidad", "Subtotal"};
            modeloTablaProductos = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            tablaProductos = new JTable(modeloTablaProductos);
            JScrollPane scrollTabla = new JScrollPane(tablaProductos);

            JPanel panelAgregar = new JPanel(new GridLayout(0, 4, 5, 5));
            panelAgregar.add(new JLabel("Producto:"));
            comboProductos = new JComboBox<>();
            controladorProductos.listaProductos().forEach(comboProductos::addItem);
            panelAgregar.add(comboProductos);

            panelAgregar.add(new JLabel("Cantidad:"));
            spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
            panelAgregar.add(spinnerCantidad);

            JButton btnAgregar = new JButton("Agregar");
            btnAgregar.addActionListener(e -> agregarProducto());
            panelAgregar.add(btnAgregar);

            JButton btnEliminar = new JButton("Eliminar Seleccionado");
            btnEliminar.addActionListener(e -> eliminarProductoSeleccionado());
            panelAgregar.add(btnEliminar);

            panelProductos.add(scrollTabla, BorderLayout.CENTER);
            panelProductos.add(panelAgregar, BorderLayout.SOUTH);

            panel.add(panelProductos, BorderLayout.CENTER);

            //panel inferior para el total de la venta
            JPanel panelInferior = new JPanel(new BorderLayout());

            JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            panelTotal.add(new JLabel("Total:"));
            lblTotal = new JLabel("$0");
            lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 16f));
            panelTotal.add(lblTotal);
            panelInferior.add(panelTotal, BorderLayout.NORTH);

            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnGuardar = new JButton("Guardar");
            btnGuardar.addActionListener(e -> guardarVenta());

            JButton btnCancelar = new JButton("Cancelar");
            btnCancelar.addActionListener(e -> dispose());

            panelBotones.add(btnCancelar);
            panelBotones.add(btnGuardar);
            panelInferior.add(panelBotones, BorderLayout.SOUTH);

            panel.add(panelInferior, BorderLayout.SOUTH);

            if (venta != null) {
                comboClientes.setSelectedItem(venta.getCliente());
                // compra original
                venta.getListaCompras().forEach((producto, cantidad) -> {
                    Object[] fila = {
                        producto.getNombre(),
                        NumberFormat.getCurrencyInstance(Locale.US).format(producto.getPrecio()),
                        cantidad,
                        NumberFormat.getCurrencyInstance(Locale.US).format(producto.getPrecio() * cantidad)
                    };
                    modeloTablaProductos.addRow(fila);
                });
                actualizarTotal();
            }

            add(panel);
        }

        private void agregarProducto() {
            Producto producto = (Producto) comboProductos.getSelectedItem();
            int cantidad = (Integer) spinnerCantidad.getValue();

            if (producto == null) {
                return;
            }

            for (int i = 0; i < modeloTablaProductos.getRowCount(); i++) {
                String nombreProducto = (String) modeloTablaProductos.getValueAt(i, 0);
                if (nombreProducto.equals(producto.getNombre())) {
                    int cantidadActual = (Integer) modeloTablaProductos.getValueAt(i, 2);
                    modeloTablaProductos.setValueAt(cantidadActual + cantidad, i, 2);
                    double precio = producto.getPrecio();
                    double subtotal = precio * (cantidadActual + cantidad);
                    modeloTablaProductos.setValueAt(NumberFormat.getCurrencyInstance(Locale.US).format(subtotal), i, 3);
                    actualizarTotal();
                    return;
                }
            }

            Object[] fila = {
                producto.getNombre(),
                NumberFormat.getCurrencyInstance(Locale.US).format(producto.getPrecio()),
                cantidad,
                NumberFormat.getCurrencyInstance(Locale.US).format(producto.getPrecio() * cantidad)
            };
            modeloTablaProductos.addRow(fila);
            actualizarTotal();
        }

        private void eliminarProductoSeleccionado() {
            int filaSeleccionada = tablaProductos.getSelectedRow();
            if (filaSeleccionada != -1) {
                modeloTablaProductos.removeRow(filaSeleccionada);
                actualizarTotal();
            }
        }

        private void actualizarTotal() {
            double total = 0;
            for (int i = 0; i < modeloTablaProductos.getRowCount(); i++) {
                String subtotalStr = (String) modeloTablaProductos.getValueAt(i, 3);
                double subtotal = Double.parseDouble(subtotalStr.replaceAll("[^\\d.]", ""));
                total += subtotal;
            }
            lblTotal.setText(NumberFormat.getCurrencyInstance(Locale.US).format(total));
        }

        private void guardarVenta() {
            Cliente cliente = (Cliente) comboClientes.getSelectedItem();
            if (cliente == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un cliente", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (modeloTablaProductos.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Agregue al menos un producto", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            HashMap<Producto, Integer> productosVendidos = new HashMap<>();
            for (int i = 0; i < modeloTablaProductos.getRowCount(); i++) {
                String nombreProducto = (String) modeloTablaProductos.getValueAt(i, 0);
                int cantidad = (Integer) modeloTablaProductos.getValueAt(i, 2);

                //buscar producto por nombre
                Producto producto = controladorProductos.listaProductos().stream()
                        .filter(p -> p.getNombre().equals(nombreProducto))
                        .findFirst()
                        .orElse(null);
                if (producto != null) {
                    productosVendidos.put(producto, cantidad);
                }
            }

            if (venta == null) {
                controladorVentas.limpiarCarrito();
                for (Map.Entry<Producto, Integer> entry : productosVendidos.entrySet()) {
                    controladorVentas.agregarProductoAlCarrito(
                            entry.getKey().getNombre(),
                            entry.getValue()
                    );
                }
                controladorVentas.crearVenta(cliente.getDocumento(), LocalDate.now());
            } else {
                venta.getListaCompras().clear();
                venta.getListaCompras().putAll(productosVendidos);
                venta.calcularTotal();
                controladorVentas.guardarDatos();
            }

            guardado = true;
            dispose();
        }

        public boolean isGuardado() {
            return guardado;
        }
    }
}
