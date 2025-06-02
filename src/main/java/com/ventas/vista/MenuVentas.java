package com.ventas.vista;

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
import java.util.Locale;
import java.util.ArrayList;

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
    private JComboBox<String> comboOrdenar;

    // Colores del tema
    private final Color COLOR_FONDO = new Color(45, 45, 45);
    private final Color COLOR_PANEL = new Color(60, 60, 60);
    private final Color COLOR_BOTON = new Color(80, 80, 80);
    private final Color COLOR_BOTON_HOVER = new Color(100, 100, 100);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_BUSQUEDA = new Color(70, 70, 70);
    private final Color COLOR_TABLA_HEADER = new Color(70, 70, 90);
    private final Color COLOR_TABLA_ROW1 = new Color(50, 50, 60);
    private final Color COLOR_TABLA_ROW2 = new Color(45, 45, 55);
    private final MenuPrincipal menuPrincipal;

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
        // Panel principal con fondo degradado
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

        // Header con título y logo
        crearHeader();

        // Panel central
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setOpaque(false);

        // Panel izquierdo - Menú de opciones
        crearPanelIzquierdo();

        // Panel derecho - Tabla de ventas
        crearPanelDerecho();

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

        // Logo y título
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

        // Botón para volver al menú principal
        JButton btnVolver = new JButton("← Volver al Menú");
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnVolver.setForeground(COLOR_TEXTO);
        btnVolver.setBackground(new Color(70, 70, 75));
        btnVolver.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(90, 90, 95), 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        btnVolver.addActionListener(e -> volverAlMenuPrincipal());

        header.add(btnVolver, BorderLayout.EAST);

        panelPrincipal.add(header, BorderLayout.NORTH);
    }

    private void crearPanelIzquierdo() {
        panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBackground(COLOR_PANEL);
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelIzquierdo.setPreferredSize(new Dimension(250, 0));

        // Título del menú
        JLabel tituloMenu = new JLabel("Ventas");
        tituloMenu.setFont(new Font("Arial", Font.BOLD, 28));
        tituloMenu.setForeground(COLOR_TEXTO);
        tituloMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        tituloMenu.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Estadísticas
        JLabel lblTotal = new JLabel("Total: " + controladorVentas.listarVentas().size() + " ventas");
        lblTotal.setForeground(COLOR_TEXTO);
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTotal.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Botones del menú
        JButton btnCrearVenta = crearBotonMenu("Crear Venta");
        JButton btnAnularVenta = crearBotonMenu("Anular Venta");
        JButton btnEditarVenta = crearBotonMenu("Editar Venta");

        // Eventos de botones
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

        // Efecto hover mejorado
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

        // Panel superior con búsqueda y ordenamiento
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 0));
        panelSuperior.setOpaque(false);

        // Panel de búsqueda
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

        // Panel de ordenamiento
        JPanel panelOrdenar = new JPanel(new BorderLayout(10, 0));
        panelOrdenar.setOpaque(false);

        JLabel lblOrdenar = new JLabel("Ordenar por:");
        lblOrdenar.setForeground(COLOR_TEXTO);
        lblOrdenar.setFont(new Font("Arial", Font.BOLD, 14));

        String[] opcionesOrden = {"ID", "Cliente", "Total", "Fecha"};
        comboOrdenar = new JComboBox<>(opcionesOrden);
        comboOrdenar.setBackground(COLOR_BUSQUEDA);
        comboOrdenar.setForeground(COLOR_TEXTO);
        comboOrdenar.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    setBackground(COLOR_BOTON_HOVER);
                    setForeground(COLOR_TEXTO);
                } else {
                    setBackground(COLOR_BUSQUEDA);
                    setForeground(COLOR_TEXTO);
                }
                return this;
            }
        });
        comboOrdenar.addActionListener(e -> ordenarVentas());

        panelOrdenar.add(lblOrdenar, BorderLayout.WEST);
        panelOrdenar.add(comboOrdenar, BorderLayout.CENTER);

        panelSuperior.add(panelBusqueda, BorderLayout.CENTER);
        panelSuperior.add(panelOrdenar, BorderLayout.EAST);

        // Tabla de ventas
        String[] columnas = {"ID", "Cliente", "Total", "Método de Pago", "Fecha"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Todas las celdas no editables
            }
        };

        tablaVentas = new JTable(modeloTabla) {
            // Renderizado personalizado para la tabla
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);

                // Resaltar fila seleccionada
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

        // Configuración adicional de la tabla
        tablaVentas.setRowHeight(35);
        tablaVentas.getTableHeader().setBackground(COLOR_TABLA_HEADER);
        tablaVentas.getTableHeader().setForeground(COLOR_TEXTO);
        tablaVentas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaVentas.setSelectionBackground(COLOR_BOTON_HOVER);
        tablaVentas.setSelectionForeground(COLOR_TEXTO);
        tablaVentas.setGridColor(COLOR_BOTON);
        tablaVentas.setShowGrid(true);
        tablaVentas.setAutoCreateRowSorter(true);

        // Personalizar el renderizado de las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaVentas.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        tablaVentas.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Total
        tablaVentas.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Método de Pago
        tablaVentas.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Fecha

        // Configurar anchos de columnas
        tablaVentas.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        tablaVentas.getColumnModel().getColumn(1).setPreferredWidth(200); // Cliente
        tablaVentas.getColumnModel().getColumn(2).setPreferredWidth(100); // Total
        tablaVentas.getColumnModel().getColumn(3).setPreferredWidth(150); // Método de Pago
        tablaVentas.getColumnModel().getColumn(4).setPreferredWidth(120); // Fecha

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
        String filtro = campoBusqueda.getText().toLowerCase().trim();
        modeloTabla.setRowCount(0);

        List<Venta> ventas = controladorVentas.listarVentas();
        if (filtro.isEmpty()) {
            for (Venta venta : ventas) {
                agregarFilaTabla(venta);
            }
            return;
        }

        for (Venta venta : ventas) {
            if (String.valueOf(venta.getIdVenta()).contains(filtro)
                    || venta.getCliente().getNombreCompleto().toLowerCase().contains(filtro)) {
                agregarFilaTabla(venta);
            }
        }
    }

    private void ordenarVentas() {
        // Esta funcionalidad se puede implementar ordenando la lista antes de mostrarla
        cargarDatos(); // Por ahora solo recarga los datos
        // TODO: Implementar ordenamiento según la opción seleccionada
    }

    private void agregarFilaTabla(Venta venta) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Object[] fila = {
            venta.getIdVenta(),
            venta.getCliente().getNombreCompleto(), // Mostrar nombre en lugar del objeto
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
            cargarDatos(); // Refrescar tabla
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

    // Clase interna para el formulario de venta
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

            // Panel superior: Cliente
            JPanel panelCliente = new JPanel(new GridLayout(0, 2, 5, 5));
            panelCliente.add(new JLabel("Cliente:"));
            comboClientes = new JComboBox<>();
            // Cargar clientes
            controladorClientes.listaClientes().forEach(comboClientes::addItem);
            panelCliente.add(comboClientes);

            panel.add(panelCliente, BorderLayout.NORTH);

            // Panel central: Productos
            JPanel panelProductos = new JPanel(new BorderLayout(10, 10));

            // Tabla de productos
            String[] columnas = {"Producto", "Precio Unitario", "Cantidad", "Subtotal"};
            modeloTablaProductos = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            tablaProductos = new JTable(modeloTablaProductos);
            JScrollPane scrollTabla = new JScrollPane(tablaProductos);

            // Panel para agregar productos
            JPanel panelAgregar = new JPanel(new GridLayout(0, 4, 5, 5));
            panelAgregar.add(new JLabel("Producto:"));
            comboProductos = new JComboBox<>();
            // Cargar productos
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

            // Panel inferior: Total y botones
            JPanel panelInferior = new JPanel(new BorderLayout());

            JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            panelTotal.add(new JLabel("Total:"));
            lblTotal = new JLabel("$0.00");
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

            // Si estamos editando, cargar datos de la venta existente
            if (venta != null) {
                comboClientes.setSelectedItem(venta.getCliente());
                // Cargar productos de la venta
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

            // Verificar si ya está en la tabla para actualizar cantidad
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

            // Si no existe, agregar nueva fila
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

            // Construir el mapa de productos y cantidades
            HashMap<Producto, Integer> productosVendidos = new HashMap<>();
            for (int i = 0; i < modeloTablaProductos.getRowCount(); i++) {
                String nombreProducto = (String) modeloTablaProductos.getValueAt(i, 0);
                int cantidad = (Integer) modeloTablaProductos.getValueAt(i, 2);

                // Buscar el producto por nombre
                Producto producto = controladorProductos.listaProductos().stream()
                        .filter(p -> p.getNombre().equals(nombreProducto))
                        .findFirst()
                        .orElse(null);
                if (producto != null) {
                    productosVendidos.put(producto, cantidad);
                }
            }

            if (venta == null) {
                // Crear nueva venta usando ControladorVenta
                controladorVentas.limpiarCarrito();
                for (Map.Entry<Producto, Integer> entry : productosVendidos.entrySet()) {
                    controladorVentas.agregarProductoAlCarrito(
                        entry.getKey().getNombre(),
                        entry.getValue()
                    );
                }
                controladorVentas.crearVenta(cliente.getDocumento(), LocalDate.now());
            } else {
                // Actualizar venta existente
                venta.getListaCompras().clear();
                venta.getListaCompras().putAll(productosVendidos);
                venta.calcularTotal();
                // Guardar cambios
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