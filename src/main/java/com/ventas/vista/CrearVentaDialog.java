// CrearVentaDialog.java
package com.ventas.vista;

import com.ventas.modelo.*;
import com.ventas.repositorio.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class CrearVentaDialog extends JDialog {
    private RepositorioVentas repoVentas;
    private RepositorioProductos repoProductos;
    private RepositorioCliente repoClientes;
    private boolean ventaCreada = false;
    
    private JComboBox<Cliente> cmbClientes;
    private DefaultTableModel modeloTablaProductos;
    private JTable tablaProductos;
    private JButton btnAgregarProducto;
    private JLabel lblTotal;
    private HashMap<Producto, Integer> productosSeleccionados = new HashMap<>();

    public CrearVentaDialog(JFrame parent, RepositorioVentas repoVentas) {
        super(parent, "Nueva Venta", true);
        this.repoVentas = repoVentas;
        this.repoProductos = new RepositorioProductos();
        this.repoClientes = new RepositorioCliente();
        setSize(800, 600);
        setLocationRelativeTo(parent);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(60, 63, 65));
        
        // Panel superior - Selección de cliente
        JPanel panelCliente = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelCliente.setBackground(new Color(70, 73, 75));
        panelCliente.setBorder(new TitledBorder("Seleccionar Cliente"));
        
        cmbClientes = new JComboBox<>();
        repoClientes.listaClientes().forEach(cmbClientes::addItem);
        cmbClientes.setRenderer(new ClienteRenderer());
        
        panelCliente.add(new JLabel("Cliente:"));
        panelCliente.add(cmbClientes);
        
        // Panel central - Productos
        JPanel panelProductos = new JPanel(new BorderLayout());
        panelProductos.setBackground(new Color(70, 73, 75));
        panelProductos.setBorder(new TitledBorder("Productos"));
        
        String[] columnas = {"Producto", "Precio", "Cantidad", "Subtotal"};
        modeloTablaProductos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Solo la columna de cantidad es editable
            }
        };
        
        tablaProductos = new JTable(modeloTablaProductos);
        JScrollPane scroll = new JScrollPane(tablaProductos);
        
        btnAgregarProducto = new JButton("+ Agregar Producto");
        btnAgregarProducto.addActionListener(e -> mostrarSelectorProductos());
        
        panelProductos.add(scroll, BorderLayout.CENTER);
        panelProductos.add(btnAgregarProducto, BorderLayout.SOUTH);
        
        // Panel inferior - Total y botones
        JPanel panelInferior = new JPanel(new BorderLayout());
        
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTotal.add(new JLabel("Total:"));
        lblTotal = new JLabel("$0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelTotal.add(lblTotal);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        
        JButton btnGuardar = new JButton("Guardar Venta");
        btnGuardar.addActionListener(e -> guardarVenta());
        
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);
        
        panelInferior.add(panelTotal, BorderLayout.CENTER);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);
        
        // Ensamblar
        panel.add(panelCliente, BorderLayout.NORTH);
        panel.add(panelProductos, BorderLayout.CENTER);
        panel.add(panelInferior, BorderLayout.SOUTH);
        
        add(panel);
    }
    
    private void mostrarSelectorProductos() {
        // Implementar selector de productos
    }
    
    private void actualizarTotal() {
        double total = productosSeleccionados.entrySet().stream()
            .mapToDouble(entry -> entry.getKey().getPrecio() * entry.getValue())
            .sum();
        lblTotal.setText(String.format("$%.2f", total));
    }
    
    private void guardarVenta() {
        Cliente cliente = (Cliente) cmbClientes.getSelectedItem();
        if (cliente == null || productosSeleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente y al menos un producto");
            return;
        }
        
        repoVentas.crearVenta(cliente, productosSeleccionados, LocalDate.now());
        ventaCreada = true;
        dispose();
    }
    
    public boolean isVentaCreada() {
        return ventaCreada;
    }
    
    // Renderer personalizado para mostrar clientes
    private static class ClienteRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Cliente cliente) {
                setText(cliente.getNombreCompleto() + " (" + cliente.getDocumento() + ")");
            }
            return this;
        }
    }
}