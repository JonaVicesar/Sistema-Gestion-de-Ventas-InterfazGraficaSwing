package com.ventas.vista;

import com.ventas.controlador.ControladorProducto;
import com.ventas.modelo.Producto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MenuProductos extends JFrame {

    private ControladorProducto controladorProducto; // Cambiado de RepositorioProductos a ControladorProducto
    private JTextField campoBusqueda;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JPanel panelPrincipal;
    private JPanel panelIzquierdo;
    private JPanel panelDerecho;

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

    public MenuProductos(MenuPrincipal menuPrincipal) {
        this.menuPrincipal = menuPrincipal;
        this.controladorProducto = new ControladorProducto(); // Inicializar el controlador
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

        // Panel derecho - Tabla de productos
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
        JLabel tituloMenu = new JLabel("Productos");
        tituloMenu.setFont(new Font("Arial", Font.BOLD, 28));
        tituloMenu.setForeground(COLOR_TEXTO);
        tituloMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        tituloMenu.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Estadísticas - Cambio aquí: usar controlador en lugar de repositorio
        JLabel lblTotal = new JLabel("Total: " + controladorProducto.listaProductos().size() + " productos");
        lblTotal.setForeground(COLOR_TEXTO);
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTotal.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Botones del menú
        JButton btnCrear = crearBotonMenu("Crear Producto");
        JButton btnEliminar = crearBotonMenu("Eliminar Producto");
        JButton btnEditar = crearBotonMenu("Editar Producto");

        // Eventos de botones
        btnCrear.addActionListener(e -> mostrarDialogoCrearProducto());
        btnEliminar.addActionListener(e -> eliminarProductoSeleccionado());
        btnEditar.addActionListener(e -> editarProductoSeleccionado());

        panelIzquierdo.add(tituloMenu);
        panelIzquierdo.add(lblTotal);
        panelIzquierdo.add(btnCrear);
        panelIzquierdo.add(Box.createVerticalStrut(15));
        panelIzquierdo.add(btnEliminar);
        panelIzquierdo.add(Box.createVerticalStrut(15));
        panelIzquierdo.add(btnEditar);
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

        JLabel lblBuscar = new JLabel("Buscar Producto:");
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
                filtrarProductos();
            }
        });

        // Botón de búsqueda con ícono
        JButton btnBuscar = new JButton("🔍");
        btnBuscar.setBackground(COLOR_BOTON);
        btnBuscar.setForeground(COLOR_TEXTO);
        btnBuscar.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnBuscar.addActionListener(e -> filtrarProductos());

        panelBusqueda.add(lblBuscar, BorderLayout.WEST);
        panelBusqueda.add(campoBusqueda, BorderLayout.CENTER);
        panelBusqueda.add(btnBuscar, BorderLayout.EAST);

        // Tabla de productos
        String[] columnas = {"Nombre", "Precio", "Stock", "ID"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Todas las celdas no editables
            }
        };

        tablaProductos = new JTable(modeloTabla) {
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
        tablaProductos.setRowHeight(35);
        tablaProductos.getTableHeader().setBackground(COLOR_TABLA_HEADER);
        tablaProductos.getTableHeader().setForeground(COLOR_TEXTO);
        tablaProductos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaProductos.setSelectionBackground(COLOR_BOTON_HOVER);
        tablaProductos.setSelectionForeground(COLOR_TEXTO);
        tablaProductos.setGridColor(COLOR_BOTON);
        tablaProductos.setShowGrid(true);
        tablaProductos.setAutoCreateRowSorter(true);

        // Personalizar el renderizado de las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaProductos.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Precio
        tablaProductos.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Stock
        tablaProductos.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // ID

        // Configurar anchos de columnas
        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(200); // Nombre
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(100); // Precio
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(80);  // Stock
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(60);  // ID

        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.getViewport().setBackground(COLOR_PANEL);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BOTON, 1));

        panelDerecho.add(panelBusqueda, BorderLayout.NORTH);
        panelDerecho.add(scrollPane, BorderLayout.CENTER);
    }

    private void configurarVentana() {
        setTitle("Vicesar SA - Gestión de Productos");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        List<Producto> productos = controladorProducto.listaProductos(); // Usar controlador

        for (Producto producto : productos) {
            Object[] fila = {
                producto.getNombre(),
                String.format("$%.2f", producto.getPrecio()),
                producto.getCantidad(),
                producto.getId()
            };
            modeloTabla.addRow(fila);
        }
    }

    private void filtrarProductos() {
        String filtro = campoBusqueda.getText().toLowerCase().trim();
        modeloTabla.setRowCount(0);

        List<Producto> productos = controladorProducto.listaProductos(); // Usar controlador
        if (filtro.isEmpty()) {
            for (Producto producto : productos) {
                agregarFilaTabla(producto);
            }
            return;
        }

        for (Producto producto : productos) {
            if (producto.getNombre().toLowerCase().contains(filtro)
                    || String.valueOf(producto.getId()).contains(filtro)) {
                agregarFilaTabla(producto);
            }
        }
    }

    private void agregarFilaTabla(Producto producto) {
        Object[] fila = {
            producto.getNombre(),
            String.format("$%.2f", producto.getPrecio()),
            producto.getCantidad(),
            producto.getId()
        };
        modeloTabla.addRow(fila);
    }

    private void mostrarDialogoCrearProducto() {
        FormularioProductoDialog dialog = new FormularioProductoDialog(this, controladorProducto, null); // Pasar controlador
        dialog.setVisible(true);
        if (dialog.isGuardado()) {
            cargarDatos();
            actualizarContadorProductos(); // Actualizar contador después de crear
        }
    }

    private void eliminarProductoSeleccionado() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar este producto?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            String nombre = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
            try {
                controladorProducto.eliminarProducto(nombre); // Usar controlador
                cargarDatos();
                actualizarContadorProductos(); // Actualizar contador después de eliminar
                JOptionPane.showMessageDialog(this, "Producto eliminado correctamente");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarProductoSeleccionado() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para editar");
            return;
        }

        String nombre = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        // Necesitaremos obtener el producto a través del controlador
        // Pero el controlador actual no tiene un método getProducto()
        // Vamos a añadir esa funcionalidad al FormularioProductoDialog
        
        FormularioProductoDialog dialog = new FormularioProductoDialog(this, controladorProducto, nombre); // Pasar nombre en lugar de producto
        dialog.setVisible(true);
        if (dialog.isGuardado()) {
            cargarDatos();
        }
    }

    // Método para actualizar el contador de productos en el panel izquierdo
    private void actualizarContadorProductos() {
        // Reconstruir el panel izquierdo es una opción, pero es mejor tener una referencia al label
        // Por simplicidad, vamos a recrear el panel
        panelPrincipal.remove(panelIzquierdo.getParent()); // Remover el panel central
        
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setOpaque(false);
        
        crearPanelIzquierdo(); // Recrear panel izquierdo con contador actualizado
        
        panelCentral.add(panelIzquierdo, BorderLayout.WEST);
        panelCentral.add(panelDerecho, BorderLayout.CENTER);
        
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }

    // Clase interna para el formulario de producto - REFACTORIZADA
    private static class FormularioProductoDialog extends JDialog {

        private final ControladorProducto controlador; // Cambio: usar controlador en lugar de repositorio
        private String nombreProductoOriginal; // Para edición
        private boolean guardado = false;
        private boolean esEdicion = false;

        private JTextField txtNombre;
        private JTextField txtPrecio;
        private JTextField txtStock;

        // Constructor para crear nuevo producto
        public FormularioProductoDialog(JFrame parent, ControladorProducto controlador, String nombreProducto) {
            super(parent, nombreProducto == null ? "Nuevo Producto" : "Editar Producto", true);
            this.controlador = controlador;
            this.nombreProductoOriginal = nombreProducto;
            this.esEdicion = nombreProducto != null;
            setSize(400, 300);
            setLocationRelativeTo(parent);
            inicializarComponentes();
        }

        private void inicializarComponentes() {
            JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
            panel.setBorder(new EmptyBorder(20, 20, 20, 20));

            // Campos del formulario
            txtNombre = new JTextField();
            txtPrecio = new JTextField();
            txtStock = new JTextField();

            // Si estamos editando, cargar datos existentes
            if (esEdicion) {
                // Necesitamos obtener el producto por nombre
                // Como no tenemos getProducto en el controlador, necesitaremos buscarlo en la lista
                List<Producto> productos = controlador.listaProductos();
                Producto producto = productos.stream()
                    .filter(p -> p.getNombre().equals(nombreProductoOriginal))
                    .findFirst()
                    .orElse(null);
                    
                if (producto != null) {
                    txtNombre.setText(producto.getNombre());
                    txtPrecio.setText(String.valueOf(producto.getPrecio()));
                    txtStock.setText(String.valueOf(producto.getCantidad()));
                }
            }

            // Agregar campos al panel
            panel.add(new JLabel("Nombre del Producto:"));
            panel.add(txtNombre);
            panel.add(new JLabel("Precio:"));
            panel.add(txtPrecio);
            panel.add(new JLabel("Stock:"));
            panel.add(txtStock);

            // Botones
            JButton btnGuardar = new JButton("Guardar");
            btnGuardar.addActionListener(e -> guardarProducto());

            JButton btnCancelar = new JButton("Cancelar");
            btnCancelar.addActionListener(e -> dispose());

            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            panelBotones.add(btnCancelar);
            panelBotones.add(btnGuardar);

            add(panel, BorderLayout.CENTER);
            add(panelBotones, BorderLayout.SOUTH);
        }

        private void guardarProducto() {
            try {
                String nombre = txtNombre.getText().trim();
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                int stock = Integer.parseInt(txtStock.getText().trim());

                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío");
                    return;
                }

                if (esEdicion) {
                    // Editar producto existente usando el controlador
                    if (!nombreProductoOriginal.equals(nombre)) {
                        controlador.editarNombre(nombreProductoOriginal, nombre);
                        nombreProductoOriginal = nombre; // Actualizar referencia
                    }
                    controlador.editarPrecio(nombreProductoOriginal, precio);
                    controlador.editarStock(nombreProductoOriginal, stock);
                } else {
                    // Crear nuevo producto usando el controlador
                    controlador.agregarProducto(nombre, precio, stock);
                }

                guardado = true;
                dispose();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Precio y stock deben ser números válidos", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        public boolean isGuardado() {
            return guardado;
        }
    }
}