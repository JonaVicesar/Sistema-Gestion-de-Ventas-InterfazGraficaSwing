package com.ventas.repositorio;

import com.ventas.modelo.Cliente;
import com.ventas.modelo.Producto;
import com.ventas.modelo.Venta;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Repositorio encargado de gestionar las ventas realizadas
 * Almacena las ventas en la memoria y tiene metodos para crear, editar, eliminar y consultar ventas
 */
public class RepositorioVentas {

    // Mapa que almacena las ventas con su ID como clave
    private static final HashMap<Integer, Venta> repositorio = new HashMap<>();
    
    // El ID aumenta para cada venta
    private static int idVenta = 1;

    /**
     * Crea una nueva venta con un cliente, una lista de compras y una fecha determinada
     *
     * @param cliente Cliente que realiza la compra
     * @param listaCompras Lista de productos con sus cantidades
     * @param fecha Fecha de la venta
     * @return ID asignado a la nueva venta
     */
    public int crearVenta(Cliente cliente, HashMap<Producto, Integer> listaCompras, LocalDate fecha) {
        Venta nuevaVenta = new Venta(idVenta, cliente, listaCompras, fecha);
        repositorio.put(idVenta, nuevaVenta);
        return idVenta++;
    }

    /**
     * Agrega un producto a una venta existente, sumando la cantidad si ya estaba presente
     *
     * @param idVenta ID de la venta
     * @param producto Producto a agregar
     * @param cantidad Cantidad a agregar 
     * @return true si se agregó correctamente
     */
    public boolean agregarProducto(int idVenta, Producto producto, int cantidad) {
        Venta venta = repositorio.get(idVenta);
        if (venta == null || producto == null || cantidad <= 0) return false;

        HashMap<Producto, Integer> listaCompras = venta.getListaCompras();
        listaCompras.put(producto, listaCompras.getOrDefault(producto, 0) + cantidad);
        venta.calcularTotal(); 
        return true;
    }

    /**
     * Disminuye la cantidad de un producto en una venta.
     * Si la cantidad queda en cero o menos, el producto se elimina de la venta.
     *
     * @param idVenta ID de la venta
     * @param producto Producto a modificar
     * @param cantidad Cantidad a disminuir (> 0)
     * @return true si se modificó correctamente
     */
    public boolean disminuirCantidadProducto(int idVenta, Producto producto, int cantidad) {
        Venta venta = repositorio.get(idVenta);
        if (venta == null || producto == null || cantidad <= 0) return false;

        HashMap<Producto, Integer> listaCompras = venta.getListaCompras();
        if (!listaCompras.containsKey(producto)) return false;

        int cantidadActual = listaCompras.get(producto);
        if (cantidadActual <= cantidad) {
            listaCompras.remove(producto);
        } else {
            listaCompras.put(producto, cantidadActual - cantidad);
        }
        venta.calcularTotal(); // Recalcula el total tras el cambio
        return true;
    }

    /**
     * Elimina un producto de una venta
     *
     * @param idVenta ID de la venta
     * @param producto Producto a eliminar
     * @return true si fue eliminado correctamente
     */
    public boolean eliminarProducto(int idVenta, Producto producto) {
        Venta venta = repositorio.get(idVenta);
        if (venta == null || producto == null) return false;

        HashMap<Producto, Integer> listaCompras = venta.getListaCompras();
        if (listaCompras.remove(producto) != null) {
            venta.calcularTotal();
            return true;
        }
        return false;
    }

    /**
     * Elimina una venta del repositorio
     *
     * @param idVenta ID de la venta a eliminar
     * @return true si fue eliminada correctamente
     */
    public boolean eliminarVenta(int idVenta) {
        return repositorio.remove(idVenta) != null;
    }

    /**
     * Devuelve una lista de las ventas realizadas entre dos fechas 
     *
     * @param desde Fecha de inicio
     * @param hasta Fecha de fin
     * @return Lista de ventas dentro del rango de fechas
     */
    public List<Venta> obtenerVentasEntreFechas(LocalDate desde, LocalDate hasta) {
        return repositorio.values().stream()
            .filter(venta -> !venta.getFecha().isBefore(desde) && !venta.getFecha().isAfter(hasta))
            .collect(Collectors.toList());
    }

    /**
     * Obtiene una venta específica según su ID
     *
     * @param idVenta ID de la venta
     * @return Objeto Venta si existe, null en caso contrario
     */
    public Venta obtenerVentaPorId(int idVenta) {
        return repositorio.get(idVenta);
    }

    /**
     * Devuelve una lista con todas las ventas registradas
     *
     * @return Lista de ventas
     */
    public List<Venta> listarVentas() {
        return new ArrayList<>(repositorio.values());
    }

    /**
     * Genera un informe de productos vendidos y sus cantidades entre dos fechas
     *
     * @param desde Fecha de inicio
     * @param hasta Fecha de fin
     * @return Mapa con productos como clave y cantidad total vendida como valor
     */
    public Map<Producto, Integer> generarInformeProductosVendidos(LocalDate desde, LocalDate hasta) {
        Map<Producto, Integer> productosVendidos = new HashMap<>();
        
        obtenerVentasEntreFechas(desde, hasta).forEach(venta -> {
            venta.getListaCompras().forEach((producto, cantidad) -> {
                productosVendidos.put(producto, productosVendidos.getOrDefault(producto, 0) + cantidad);
            });
        });
        
        return productosVendidos;
    }
}
