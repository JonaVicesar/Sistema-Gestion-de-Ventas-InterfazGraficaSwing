package com.ventas.vista;

import com.ventas.controlador.ControladorCliente;
import com.ventas.modelo.Cliente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class MenuClientes extends JFrame {

    private ControladorCliente controladorCliente;
    private JTextField campoBusqueda;
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private JPanel panelPrincipal;
    private JPanel panelIzquierdo;
    private JPanel panelDerecho;
    private JLabel lblTotal; // Agregamos referencia para actualizar dinámicamente

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

    // Formateador de fechas
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final Random random = new Random();
    private final MenuPrincipal menuPrincipal;

    public MenuClientes(MenuPrincipal menuPrincipal) {
        this.menuPrincipal = menuPrincipal;
        this.controladorCliente = new ControladorCliente();
        inicializarComponentes();
        configurarVentana();
        cargarDatos();
    }

    private void inicializarComponentes() {
        // Panel principal con fondo
        panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Fondo degradado
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

        // Panel derecho - Tabla de clientes
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
        JButton btnVolver = new JButton("← Volver");
        btnVolver.setFont(new Font("Arial", Font.PLAIN, 14));
        btnVolver.setForeground(COLOR_TEXTO);
        btnVolver.setBackground(COLOR_BOTON);
        btnVolver.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
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
        JLabel tituloMenu = new JLabel("Gestión de Clientes");
        tituloMenu.setFont(new Font("Arial", Font.BOLD, 20));
        tituloMenu.setForeground(COLOR_TEXTO);
        tituloMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        tituloMenu.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Estadísticas - Usando controlador
        lblTotal = new JLabel("Total: " + controladorCliente.cantidadClientes() + " clientes");
        lblTotal.setForeground(COLOR_TEXTO);
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTotal.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Botones del menú
        JButton btnCrear = crearBotonMenu("Nuevo Cliente");
        JButton btnEditar = crearBotonMenu("Editar Cliente");
        JButton btnEliminar = crearBotonMenu("Eliminar Cliente");
        JButton btnExportar = crearBotonMenu("Exportar a Excel");

        // Eventos de botones
        btnCrear.addActionListener(e -> mostrarDialogoCrearCliente());
        btnEditar.addActionListener(e -> editarClienteSeleccionado());
        btnEliminar.addActionListener(e -> eliminarClienteSeleccionado());
        btnExportar.addActionListener(e -> exportarAExcel());

        panelIzquierdo.add(tituloMenu);
        panelIzquierdo.add(lblTotal);
        panelIzquierdo.add(btnCrear);
        panelIzquierdo.add(Box.createVerticalStrut(15));
        panelIzquierdo.add(btnEditar);
        panelIzquierdo.add(Box.createVerticalStrut(15));
        panelIzquierdo.add(btnEliminar);
        panelIzquierdo.add(Box.createVerticalStrut(15));
        panelIzquierdo.add(btnExportar);
        panelIzquierdo.add(Box.createVerticalGlue());
    }

    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setForeground(COLOR_TEXTO);
        boton.setBackground(COLOR_BOTON);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BOTON_HOVER, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        boton.setFocusPainted(false);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(200, 45));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(COLOR_BOTON_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(COLOR_BOTON);
            }
        });

        return boton;
    }

    private void crearPanelDerecho() {
        panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setOpaque(false);
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new BorderLayout(10, 0));
        panelBusqueda.setOpaque(false);

        JLabel lblBuscar = new JLabel("Buscar Cliente:");
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
                filtrarClientes();
            }
        });

        // Botón de búsqueda con ícono
        JButton btnBuscar = new JButton("🔍");
        btnBuscar.setBackground(COLOR_BOTON);
        btnBuscar.setForeground(COLOR_TEXTO);
        btnBuscar.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnBuscar.addActionListener(e -> filtrarClientes());

        panelBusqueda.add(lblBuscar, BorderLayout.WEST);
        panelBusqueda.add(campoBusqueda, BorderLayout.CENTER);
        panelBusqueda.add(btnBuscar, BorderLayout.EAST);

        // Tabla de clientes
        String[] columnas = {"Nombre", "Documento", "Edad", "Método de Pago", "Última Compra"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Todas las celdas no editables
            }
        };

        tablaClientes = new JTable(modeloTabla) {
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
        tablaClientes.setRowHeight(35);
        tablaClientes.getTableHeader().setBackground(COLOR_TABLA_HEADER);
        tablaClientes.getTableHeader().setForeground(COLOR_TEXTO);
        tablaClientes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaClientes.setSelectionBackground(COLOR_BOTON_HOVER);
        tablaClientes.setSelectionForeground(COLOR_TEXTO);
        tablaClientes.setGridColor(COLOR_BOTON);
        tablaClientes.setShowGrid(true);
        tablaClientes.setAutoCreateRowSorter(true);

        // Personalizar el renderizado de las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaClientes.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Documento
        tablaClientes.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Edad
        tablaClientes.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Última Compra

        JScrollPane scrollPane = new JScrollPane(tablaClientes);
        scrollPane.getViewport().setBackground(COLOR_PANEL);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BOTON, 1));

        panelDerecho.add(panelBusqueda, BorderLayout.NORTH);
        panelDerecho.add(scrollPane, BorderLayout.CENTER);
    }

    private void configurarVentana() {
        setTitle("Vicesar SA - Gestión de Clientes");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        // Usando controlador en lugar de repositorio directo
        List<Cliente> clientes = controladorCliente.listaClientes();

        for (Cliente cliente : clientes) {
            agregarFilaTabla(cliente);
        }
        
        // Actualizar contador
        actualizarContadorClientes();
    }

    private void filtrarClientes() {
        String filtro = campoBusqueda.getText().trim();
        modeloTabla.setRowCount(0);

        List<Cliente> clientes;
        if (filtro.isEmpty()) {
            // Obtener todos los clientes a través del controlador
            clientes = controladorCliente.listaClientes();
        } else {
            // Buscar por nombre usando el método específico del controlador
            clientes = controladorCliente.buscarClientesPorNombre(filtro);
            
            // También buscar por documento si el filtro es numérico
            try {
                int documento = Integer.parseInt(filtro);
                Cliente clientePorDocumento = controladorCliente.buscarClientePorDocumento(documento);
                if (clientePorDocumento != null) {
                    // Evitar duplicados
                    if (!clientes.contains(clientePorDocumento)) {
                        clientes.add(clientePorDocumento);
                    }
                }
            } catch (NumberFormatException e) {
                // No es un número, solo buscar por nombre
            }
        }

        for (Cliente cliente : clientes) {
            agregarFilaTabla(cliente);
        }
    }

    private void agregarFilaTabla(Cliente cliente) {
        // Generar fecha aleatoria para la última compra (simulación)
        LocalDate ultimaCompra = LocalDate.now().minusDays(random.nextInt(30));

        Object[] fila = {
            cliente.getNombreCompleto(),
            cliente.getDocumento(),
            cliente.getEdad(),
            cliente.getMetodoPago() != null ? cliente.getMetodoPago() : "No especificado",
            ultimaCompra.format(dateFormatter)
        };
        modeloTabla.addRow(fila);
    }

    private void mostrarDialogoCrearCliente() {
        FormularioClienteDialog dialog = new FormularioClienteDialog(this, controladorCliente, null);
        dialog.setVisible(true);
        if (dialog.isGuardado()) {
            cargarDatos(); // Recargar datos después de crear
        }
    }

    private void eliminarClienteSeleccionado() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para eliminar");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar este cliente?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            int documento = (Integer) modeloTabla.getValueAt(filaSeleccionada, 1);
            try {
                // Usar controlador para eliminar
                boolean eliminado = controladorCliente.eliminarCliente(documento);
                if (eliminado) {
                    cargarDatos(); // Recargar datos
                    JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el cliente", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarClienteSeleccionado() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para editar");
            return;
        }

        int documento = (Integer) modeloTabla.getValueAt(filaSeleccionada, 1);
        // Usar controlador para obtener el cliente
        Cliente cliente = controladorCliente.obtenerCliente(documento);

        if (cliente != null) {
            FormularioClienteDialog dialog = new FormularioClienteDialog(this, controladorCliente, cliente);
            dialog.setVisible(true);
            if (dialog.isGuardado()) {
                cargarDatos(); // Recargar datos después de editar
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo encontrar el cliente seleccionado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarContadorClientes() {
        // Actualizar el contador usando el controlador
        lblTotal.setText("Total: " + controladorCliente.cantidadClientes() + " clientes");
    }

    private void exportarAExcel() {
        // Implementación básica - Por completar
        JOptionPane.showMessageDialog(this, "Exportando datos a Excel...");
        // Aquí iría la lógica para exportar usando el controlador
    }

    // Clase interna para el formulario de cliente
    private static class FormularioClienteDialog extends JDialog {

        private final ControladorCliente controlador;
        private Cliente cliente;
        private boolean guardado = false;

        private JTextField txtNombre;
        private JTextField txtDocumento;
        private JTextField txtEdad;
        private JTextField txtTelefono;
        private JComboBox<String> cmbMetodoPago;
        private JTextField txtTarjeta;

        public FormularioClienteDialog(JFrame parent, ControladorCliente controlador, Cliente cliente) {
            super(parent, cliente == null ? "Nuevo Cliente" : "Editar Cliente", true);
            this.controlador = controlador;
            this.cliente = cliente;
            setSize(400, 400);
            setLocationRelativeTo(parent);
            inicializarComponentes();
        }

        private void inicializarComponentes() {
            JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
            panel.setBorder(new EmptyBorder(20, 20, 20, 20));

            // Campos del formulario
            txtNombre = new JTextField();
            txtDocumento = new JTextField();
            txtEdad = new JTextField();
            txtTelefono = new JTextField();
            cmbMetodoPago = new JComboBox<>(new String[]{"EFECTIVO", "TARJETA"});
            txtTarjeta = new JTextField();

            // Si estamos editando, cargar datos existentes
            if (cliente != null) {
                txtNombre.setText(cliente.getNombreCompleto());
                txtDocumento.setText(String.valueOf(cliente.getDocumento()));
                txtDocumento.setEditable(false); // No se puede cambiar documento
                txtEdad.setText(String.valueOf(cliente.getEdad()));
                txtTelefono.setText(cliente.getTelefono());
                cmbMetodoPago.setSelectedItem(cliente.getMetodoPago());
                txtTarjeta.setText(cliente.getTarjeta() != null ? cliente.getTarjeta() : "");
            }

            // Listener para método de pago
            cmbMetodoPago.addActionListener(e -> {
                txtTarjeta.setEnabled("TARJETA".equals(cmbMetodoPago.getSelectedItem()));
            });

            // Habilitar/deshabilitar tarjeta según método
            txtTarjeta.setEnabled("TARJETA".equals(cmbMetodoPago.getSelectedItem()));

            // Agregar campos al panel
            panel.add(new JLabel("Nombre Completo:"));
            panel.add(txtNombre);
            panel.add(new JLabel("Documento:"));
            panel.add(txtDocumento);
            panel.add(new JLabel("Edad:"));
            panel.add(txtEdad);
            panel.add(new JLabel("Teléfono:"));
            panel.add(txtTelefono);
            panel.add(new JLabel("Método de Pago:"));
            panel.add(cmbMetodoPago);
            panel.add(new JLabel("Número de Tarjeta:"));
            panel.add(txtTarjeta);

            // Botones
            JButton btnGuardar = new JButton("Guardar");
            btnGuardar.addActionListener(e -> guardarCliente());

            JButton btnCancelar = new JButton("Cancelar");
            btnCancelar.addActionListener(e -> dispose());

            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            panelBotones.add(btnCancelar);
            panelBotones.add(btnGuardar);

            add(panel, BorderLayout.CENTER);
            add(panelBotones, BorderLayout.SOUTH);
        }

        private void guardarCliente() {
            try {
                String nombre = txtNombre.getText().trim();
                int documento = Integer.parseInt(txtDocumento.getText().trim());
                int edad = Integer.parseInt(txtEdad.getText().trim());
                String telefono = txtTelefono.getText().trim();
                String metodoPago = (String) cmbMetodoPago.getSelectedItem();
                String tarjeta = txtTarjeta.getText().trim();

                if (cliente == null) {
                    // Crear nuevo cliente usando el controlador
                    if (metodoPago.equals("TARJETA")) {
                        controlador.agregarCliente(nombre, edad, documento, telefono, metodoPago, tarjeta);
                    } else {
                        controlador.agregarCliente(nombre, edad, documento, telefono, metodoPago);
                    }
                } else {
                    // Editar cliente existente usando métodos del controlador
                    boolean exitoso = true;
                    
                    // Editar nombre si cambió
                    if (!cliente.getNombreCompleto().equals(nombre)) {
                        exitoso = controlador.editarNombre(nombre, documento);
                    }
                    
                    // Editar teléfono si cambió
                    if (exitoso && !cliente.getTelefono().equals(telefono)) {
                        exitoso = controlador.editarTelefono(telefono, documento);
                    }
                    
                    // Editar método de pago si cambió
                    if (exitoso && (!metodoPago.equals(cliente.getMetodoPago()) || 
                        !tarjeta.equals(cliente.getTarjeta() != null ? cliente.getTarjeta() : ""))) {
                        exitoso = controlador.editarMetodoPago(documento, metodoPago, 
                                  metodoPago.equals("TARJETA") ? tarjeta : null);
                    }
                    
                    // Actualizar edad directamente en el objeto (si no hay método en el controlador)
                    if (exitoso && cliente.getEdad() != edad) {
                        cliente.setEdad(edad);
                    }
                    
                    if (!exitoso) {
                        throw new RuntimeException("Error al actualizar el cliente");
                    }
                }

                guardado = true;
                dispose();
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Edad y documento deben ser números válidos", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        public boolean isGuardado() {
            return guardado;
        }
    }
}