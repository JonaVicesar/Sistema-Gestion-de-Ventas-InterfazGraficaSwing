package com.ventas.controlador;

import com.ventas.modelo.Cliente;
import com.ventas.modelo.Producto;
import com.ventas.modelo.Venta;
import com.ventas.repositorio.RepositorioCliente;
import com.ventas.repositorio.RepositorioProductos;
import com.ventas.repositorio.RepositorioVentas;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador encargado de manejar la lógica de ventas, incluyendo el uso de un carrito,
 * validaciones, actualizaciones de stock y operaciones relacionadas a las ventas
 * 
 * 
 * @author Jona Vicesar
 */
public class ControladorVenta {

    // Carrito donde se agregan productos antes de confirmar la venta
    private static final HashMap<Producto, Integer> carritoProductos = new HashMap<>();
    
    private final RepositorioVentas repositorioVentas;
    private final RepositorioProductos repositorioProductos;
    private final RepositorioCliente repositorioClientes;


    public LocalDate fecha = LocalDate.now();

    public ControladorVenta() {
        this.repositorioVentas = new RepositorioVentas();
        this.repositorioClientes = new RepositorioCliente();
        this.repositorioProductos = new RepositorioProductos();
    }

    /**
     * Crea una venta utilizando los productos del carrito y un cliente existente
     *
     * @param documentoCliente Número de documento del cliente
     * @param fecha Fecha de la venta
     * @return ID de la venta generada
     */
    public int crearVenta(int documentoCliente, LocalDate fecha) {
        if (!repositorioClientes.existeCliente(documentoCliente)) {
            throw new IllegalArgumentException("El cliente no existe, verifique el documento o cree uno nuevo!");
        }

        if (carritoProductos.isEmpty()) {
            throw new IllegalArgumentException("No hay productos en el carrito.");
        }

        Cliente cliente = repositorioClientes.getCliente(documentoCliente);
        
        
        for (Map.Entry<Producto, Integer> entry : carritoProductos.entrySet()) {
            Producto producto = entry.getKey();
            int cantidad = entry.getValue();
            
            if (!repositorioProductos.actualizarStock(producto.getNombre(), producto.getCantidad() - cantidad)) {
                throw new IllegalArgumentException("No se pudo actualizar el stock del producto: " + producto.getNombre());
            }
        }
        
        int idVenta = repositorioVentas.crearVenta(cliente, new HashMap<>(carritoProductos), fecha);
        limpiarCarrito(); // vacia el carrito luego de completar la venta
        return idVenta;
    }

    /**
     * Agrega un producto al carrito, validando existencia y stock
     *
     * @param nombreProducto Nombre del producto
     * @param cantidad Cantidad deseada
     * @return true si se agregó correctamente
     */
    public boolean agregarProductoAlCarrito(String nombreProducto, int cantidad) {
        if (nombreProducto == null || nombreProducto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }
        if (!repositorioProductos.existeProducto(nombreProducto)) {
            throw new IllegalArgumentException("El producto no existe.");
        }

        Producto producto = repositorioProductos.getProducto(nombreProducto);

        if (producto.getCantidad() < cantidad) {
            throw new IllegalArgumentException("No hay suficiente stock del producto.");
        }

        // Suma o inicializa la cantidad del producto en el carrito
        carritoProductos.merge(producto, cantidad, Integer::sum);
        return true;
    }

    /**
     * Elimina un producto del carrito
     *
     * @param nombreProducto Nombre del producto a quitar
     * @return true si fue eliminado correctamente
     */
    public boolean quitarProductoDelCarrito(String nombreProducto) {
        if (nombreProducto == null || nombreProducto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }

        Producto producto = obtenerProducto(nombreProducto);
        if (producto == null || !carritoProductos.containsKey(producto)) {
            throw new IllegalArgumentException("El producto no está en el carrito.");
        }

        carritoProductos.remove(producto);
        return true;
    }

    /**
     * Disminuye la cantidad de un producto en el carrito
     * Si la cantidad queda en 0, se elimina del carrito
     *
     * @param nombreProducto Nombre del producto
     * @param cantidad Cantidad a disminuir
     * @return true si se realizó el cambio
     */
    public boolean disminuirCantidadEnCarrito(String nombreProducto, int cantidad) {
        if (nombreProducto == null || nombreProducto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }

        Producto producto = obtenerProducto(nombreProducto);
        if (producto == null || !carritoProductos.containsKey(producto)) {
            throw new IllegalArgumentException("El producto no está en el carrito.");
        }

        int cantidadActual = carritoProductos.get(producto);
        if (cantidadActual <= cantidad) {
            carritoProductos.remove(producto);
        } else {
            carritoProductos.put(producto, cantidadActual - cantidad);
        }

        return true;
    }

    /**
     * Obtiene un producto del repositorio por su nombre
     *
     * @param nombreProducto Nombre del producto
     * @return Objeto Producto si existe, null si no
     */
    public Producto obtenerProducto(String nombreProducto) {
        if (!repositorioProductos.existeProducto(nombreProducto)) {
            return null;
        }
        return repositorioProductos.getProducto(nombreProducto);
    }

    /**
     * Devuelve una copia del carrito de productos actual
     *
     * @return Mapa con productos y cantidades en el carrito
     */
    public HashMap<Producto, Integer> getCarritoProductos() {
        return new HashMap<>(carritoProductos);
    }

    /**
     * Limpia completamente el carrito de compras.
     */
    public void limpiarCarrito() {
        carritoProductos.clear();
    }

    /**
     * Agrega un producto a una venta ya realizada
     *
     * @param idVenta ID de la venta
     * @param nombreProducto Nombre del producto
     * @param cantidad Cantidad a agregar
     * @return true si fue agregado correctamente
     */
    public boolean agregarProductoAVenta(int idVenta, String nombreProducto, int cantidad) {
        if (nombreProducto == null || nombreProducto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }

        Producto producto = obtenerProducto(nombreProducto);
        if (producto == null) {
            throw new IllegalArgumentException("El producto no existe.");
        }

        if (producto.getCantidad() < cantidad) {
            throw new IllegalArgumentException("No hay suficiente stock del producto.");
        }

        if (!repositorioProductos.actualizarStock(nombreProducto, producto.getCantidad() - cantidad)) {
            throw new IllegalArgumentException("No se pudo actualizar el stock del producto.");
        }

        return repositorioVentas.agregarProducto(idVenta, producto, cantidad);
    }

    /**
     * Elimina un producto de una venta existente y  vuelve a aumentar el stock
     *
     * @param idVenta ID de la venta
     * @param nombreProducto Nombre del producto
     * @return true si fue eliminado correctamente
     */
    public boolean eliminarProductoDeVenta(int idVenta, String nombreProducto) {
        if (nombreProducto == null || nombreProducto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }

        Producto producto = obtenerProducto(nombreProducto);
        if (producto == null) {
            throw new IllegalArgumentException("El producto no existe.");
        }

        Venta venta = repositorioVentas.obtenerVentaPorId(idVenta);
        if (venta == null) {
            throw new IllegalArgumentException("La venta no existe.");
        }

        HashMap<Producto, Integer> listaCompras = venta.getListaCompras();
        if (!listaCompras.containsKey(producto)) {
            throw new IllegalArgumentException("El producto no está en esta venta.");
        }

        int cantidadRestaurar = listaCompras.get(producto);
        boolean eliminado = repositorioVentas.eliminarProducto(idVenta, producto);

        if (eliminado) {
            repositorioProductos.actualizarStock(nombreProducto, producto.getCantidad() + cantidadRestaurar);
        }

        return eliminado;
    }

    /**
     * Disminuye la cantidad de un producto en una venta existente
     *
     * @param idVenta ID de la venta
     * @param nombreProducto Nombre del producto
     * @param cantidad Cantidad a disminuir
     * @return true si fue disminuido correctamente
     */
    public boolean disminuirCantidadProductoEnVenta(int idVenta, String nombreProducto, int cantidad) {
        if (nombreProducto == null || nombreProducto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }

        Producto producto = obtenerProducto(nombreProducto);
        if (producto == null) {
            throw new IllegalArgumentException("El producto no existe.");
        }

        Venta venta = repositorioVentas.obtenerVentaPorId(idVenta);
        if (venta == null) {
            throw new IllegalArgumentException("La venta no existe.");
        }

        HashMap<Producto, Integer> listaCompras = venta.getListaCompras();
        if (!listaCompras.containsKey(producto)) {
            throw new IllegalArgumentException("El producto no está en esta venta.");
        }

        int cantidadActual = listaCompras.get(producto);
        if (cantidad > cantidadActual) {
            throw new IllegalArgumentException("La cantidad a disminuir no puede ser mayor que la cantidad en la venta.");
        }

        boolean actualizado = repositorioVentas.disminuirCantidadProducto(idVenta, producto, cantidad);

        if (actualizado) {
            repositorioProductos.actualizarStock(nombreProducto, producto.getCantidad() - cantidad);
        }

        return actualizado;
    }

    /**
     * 
     *
     * @param idVenta ID de la venta
     * @return true si fue eliminada correctamente
     */
    public boolean eliminarVenta(int idVenta) {
        Venta venta = repositorioVentas.obtenerVentaPorId(idVenta);
        if (venta == null) {
            return false;
        }

        for (Map.Entry<Producto, Integer> entry : venta.getListaCompras().entrySet()) {
            Producto producto = entry.getKey();
            int cantidad = entry.getValue();
            repositorioProductos.actualizarStock(producto.getNombre(), producto.getCantidad() + cantidad);
        }

        return repositorioVentas.eliminarVenta(idVenta);
    }

    /**
     * Obtiene una venta segun su ID
     *
     * @param idVenta ID de la venta
     * @return Objeto Venta si existe
     */
    public Venta obtenerVentaPorId(int idVenta) {
        return repositorioVentas.obtenerVentaPorId(idVenta);
    }

    /**
     * Devuelve una lista con todas las ventas registradas.
     *
     * @return Lista de ventas
     */
    public List<Venta> listarVentas() {
        return repositorioVentas.listarVentas();
    }

    /**
     * Genera un informe de productos vendidos entre dos fechas
     *
     * @param desde Fecha inicial
     * @param hasta Fecha final
     * @return Mapa de productos vendidos con su cantidad
     */
    public Map<Producto, Integer> generarInformeProductosVendidos(LocalDate desde, LocalDate hasta) {
        return repositorioVentas.generarInformeProductosVendidos(desde, hasta);
    }
}
