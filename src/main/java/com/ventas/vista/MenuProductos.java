package com.ventas.vista;

import com.ventas.util.RoundedBorder;
import com.ventas.controlador.ControladorProducto;
import com.ventas.modelo.Producto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MenuProductos extends JFrame {

    private ControladorProducto controladorProducto; // 
    private JTextField campoBusqueda;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JPanel panelPrincipal;
    private JPanel panelIzquierdo;
    private JPanel panelDerecho;

    // colores del programa
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

    public MenuProductos(MenuPrincipal menuPrincipal) {
        this.menuPrincipal = menuPrincipal;
        this.controladorProducto = new ControladorProducto(); 
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

        crearPanelIzquierdo();//menu de opciones
        crearPanelDerecho();//menu de tabla de productos

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

        //titulo
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

        // boton para volver al menu principal
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

        JLabel tituloMenu = new JLabel("Productos");
        tituloMenu.setFont(new Font("Arial", Font.BOLD, 28));
        tituloMenu.setForeground(COLOR_TEXTO);
        tituloMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        tituloMenu.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        //cantidad de productos
        JLabel lblTotal = new JLabel("Total: " + controladorProducto.listaProductos().size() + " productos");
        lblTotal.setForeground(COLOR_TEXTO);
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTotal.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Botones del menu
        JButton btnCrear = crearBotonMenu("Crear Producto");
        JButton btnEliminar = crearBotonMenu("Eliminar Producto");
        JButton btnEditar = crearBotonMenu("Editar Producto");

        // listeners para botones del menu
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

        // hover
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

        //barra de busqueda
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

        JPanel panelOrdenamiento = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelOrdenamiento.setOpaque(false);

        JLabel lblOrdenar = new JLabel("Ordenar por:");
        lblOrdenar.setForeground(COLOR_TEXTO);
        lblOrdenar.setFont(new Font("Arial", Font.BOLD, 12));

        JButton btnOrdenarNombre = new JButton("Nombre");
        JButton btnOrdenarPrecio = new JButton("Precio");
        JButton btnOrdenarStock = new JButton("Stock");

        //estilo de botones
        JButton[] botonesOrden = {btnOrdenarNombre, btnOrdenarPrecio, btnOrdenarStock};
        for (JButton btn : botonesOrden) {
            btn.setFont(new Font("Arial", Font.PLAIN, 11));
            btn.setForeground(COLOR_TEXTO);
            btn.setBackground(COLOR_BOTON);
            btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        //listeners para los botones de ordenamiento
        btnOrdenarNombre.addActionListener(e -> ordenarTabla("nombre"));
        btnOrdenarPrecio.addActionListener(e -> ordenarTabla("precio"));
        btnOrdenarStock.addActionListener(e -> ordenarTabla("stock"));

        panelOrdenamiento.add(lblOrdenar);
        panelOrdenamiento.add(btnOrdenarNombre);
        panelOrdenamiento.add(btnOrdenarPrecio);
        panelOrdenamiento.add(btnOrdenarStock);

        //
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(COLOR_BOTON);
        btnBuscar.setForeground(COLOR_TEXTO);
        btnBuscar.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnBuscar.addActionListener(e -> filtrarProductos());

        panelBusqueda.add(lblBuscar, BorderLayout.WEST);
        panelBusqueda.add(campoBusqueda, BorderLayout.CENTER);
        panelBusqueda.add(btnBuscar, BorderLayout.EAST);

        //tabla de productos
        String[] columnas = {"Nombre", "Precio", "Stock", "ID"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        tablaProductos = new JTable(modeloTabla) {

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);

                // resaltar con con un hover al seleccionar una fila/producto
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

       
        tablaProductos.setRowHeight(35);
        tablaProductos.getTableHeader().setBackground(COLOR_TABLA_HEADER);
        tablaProductos.getTableHeader().setForeground(COLOR_TEXTO);
        tablaProductos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaProductos.setSelectionBackground(COLOR_BOTON_HOVER);
        tablaProductos.setSelectionForeground(COLOR_TEXTO);
        tablaProductos.setGridColor(COLOR_BOTON);
        tablaProductos.setShowGrid(true);
        tablaProductos.setAutoCreateRowSorter(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaProductos.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // precio
        tablaProductos.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // cantidad
        tablaProductos.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // ID

        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(200); // nombre
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(100); // precio
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(80);  // cantidad
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(60);  // ID

        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.getViewport().setBackground(COLOR_PANEL);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BOTON, 1));

        panelDerecho.add(panelBusqueda, BorderLayout.NORTH);
        panelDerecho.add(scrollPane, BorderLayout.CENTER);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        panelSuperior.add(panelBusqueda, BorderLayout.NORTH);
        panelSuperior.add(panelOrdenamiento, BorderLayout.SOUTH);  

        panelDerecho.add(panelSuperior, BorderLayout.NORTH);  
        panelDerecho.add(scrollPane, BorderLayout.CENTER);
    }

    private void configurarVentana() {
        setTitle("Vicesar SA - Gestión de Productos");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void ordenarTabla(String criterio) {
    List<Producto> productos = controladorProducto.listaProductos();
    String filtro = campoBusqueda.getText().trim().toLowerCase();
    
    
    if (!filtro.isEmpty()) {
        productos = productos.stream()
            .filter(p -> p.getNombre().toLowerCase().contains(filtro) || 
                         String.valueOf(p.getId()).contains(filtro))
            .collect(Collectors.toList());
    }
    
    // ordenar
    switch (criterio) {
        case "nombre":
            productos.sort(Comparator.comparing(Producto::getNombre));
            break;
        case "precio":
            productos.sort(Comparator.comparing(Producto::getPrecio));
            break;
        case "stock":
            productos.sort(Comparator.comparing(Producto::getCantidad));
            break;
    }
    
    modeloTabla.setRowCount(0);
    for (Producto producto : productos) {
        agregarFilaTabla(producto);
    }
}

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        List<Producto> productos = controladorProducto.listaProductos(); 

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

        List<Producto> productos = controladorProducto.listaProductos(); 
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
        FormularioProductoDialog dialog = new FormularioProductoDialog(this, controladorProducto, null);
        dialog.setVisible(true);
        if (dialog.isGuardado()) {
            cargarDatos();
            actualizarContadorProductos(); 
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
                controladorProducto.eliminarProducto(nombre); 
                cargarDatos();
                actualizarContadorProductos(); 
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

        FormularioProductoDialog dialog = new FormularioProductoDialog(this, controladorProducto, nombre); 
        dialog.setVisible(true);
        if (dialog.isGuardado()) {
            cargarDatos();
        }
    }


    private void actualizarContadorProductos() {
        panelPrincipal.remove(panelIzquierdo.getParent()); 

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setOpaque(false);

        crearPanelIzquierdo();

        panelCentral.add(panelIzquierdo, BorderLayout.WEST);
        panelCentral.add(panelDerecho, BorderLayout.CENTER);

        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }

    // clase interna para agregar/editar un producto
    private static class FormularioProductoDialog extends JDialog {

        private final ControladorProducto controlador;
        private String nombreProductoOriginal; // para editar
        private boolean guardado = false;
        private boolean esEdicion = false;

        private JTextField txtNombre;
        private JTextField txtPrecio;
        private JTextField txtStock;

        
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

            txtNombre = new JTextField();
            txtPrecio = new JTextField();
            txtStock = new JTextField();

            // si ya hay datos se cargan los datos originales
            if (esEdicion) {
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

            // agregar al panel
            panel.add(new JLabel("Nombre del Producto:"));
            panel.add(txtNombre);
            panel.add(new JLabel("Precio:"));
            panel.add(txtPrecio);
            panel.add(new JLabel("Stock:"));
            panel.add(txtStock);

            //botones
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
                    if (!nombreProductoOriginal.equals(nombre)) {
                        controlador.editarNombre(nombreProductoOriginal, nombre);
                        nombreProductoOriginal = nombre;
                    }
                    controlador.editarPrecio(nombreProductoOriginal, precio);
                    controlador.editarStock(nombreProductoOriginal, stock);
                } else {
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
