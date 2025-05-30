package com.ventas.repositorio;

import com.ventas.modelo.Producto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Clase que actúa como repositorio de productos
 * 
 * @author Jona Vicesar
 */
public class RepositorioProductos {

    
    public static final HashMap<String, Producto> repositorio = new HashMap<>();

    private int contadorId = 1;

    public RepositorioProductos() {
    }

    /**
     * Agrega un nuevo producto al repositorio
     * El ID es asignado automáticamente
     * @param nombre Nombre del producto
     * @param precio Precio unitario
     * @param cantidad Stock disponible
     * @return true si se agrego correctamente
     */
    public boolean agregarProducto(String nombre, double precio, int cantidad) {
        Producto producto = new Producto(nombre, contadorId, precio, cantidad);
        repositorio.put(nombre.toLowerCase(), producto);
        contadorId += 1;
        return true;
    }

    /**
     * Elimina un producto del repositorio por su nombre
     * 
     * @param nombre Nombre del producto a eliminar
     * @return true si fue eliminado exitosamente
     */
    public boolean eliminarProducto(String nombre) {
        repositorio.remove(nombre.toLowerCase());
        return true;
    }

    /**
     * Cambia el nombre de un producto existente
     * 
     * @param nombreProducto Nombre actual
     * @param nuevoNombre Nuevo nombre
     * @return true si se edita correctamente
     */
    public boolean editarNombre(String nombreProducto, String nuevoNombre) {
        Producto producto = repositorio.remove(nombreProducto.toLowerCase());
        producto.setNombre(nuevoNombre);
        repositorio.put(nuevoNombre.toLowerCase(), producto);
        return true;
    }

    /**
     * Cambia el precio de un producto existente
     * 
     * @param nombreProducto Nombre del producto
     * @param nuevoPrecio Nuevo precio
     * @return true si se actualizó correctamente
     */
    public boolean editarPrecio(String nombreProducto, double nuevoPrecio) {
        Producto productoAEditar = repositorio.get(nombreProducto.toLowerCase());
        productoAEditar.setPrecio(nuevoPrecio);
        repositorio.replace(nombreProducto.toLowerCase(), productoAEditar);
        return true;
    }

    /**
     * Cambia el stock de un producto existente
     * 
     * @param nombreProducto Nombre del producto
     * @param nuevoStock Nuevo valor de stock
     * @return true si se actualizó correctamente
     */
    public boolean editarStock(String nombreProducto, int nuevoStock) {
        Producto productoAEditar = repositorio.get(nombreProducto.toLowerCase());
        productoAEditar.setCantidad(nuevoStock);
        repositorio.replace(nombreProducto.toLowerCase(), productoAEditar);
        return true;
    }

    /**
     * Verifica si existe un producto con el nombre dado
     * 
     * @param nombre Nombre del producto
     * @return true si existe
     */
    public boolean existeProducto(String nombre) {
        return repositorio.containsKey(nombre.toLowerCase());
    }

    /**
     * Devuelve un producto específico por nombre.
     * 
     * @param nombre Nombre del producto
     * @return El objeto Producto, o null si no existe
     */
    public Producto getProducto(String nombre) {
        return repositorio.get(nombre.toLowerCase());
    }

    /**
     * Actualiza el stock de un producto existente
     * Rechaza valores negativos y productos inexistentes
     * 
     * @param nombreProducto Nombre del producto
     * @param nuevoStock Nuevo valor de stock
     * @return true si se actualizó correctamente, false si el producto no existe o el stock es negativo
     */
    public boolean actualizarStock(String nombreProducto, int nuevoStock) {
        if (nuevoStock < 0) return false;

        Producto producto = repositorio.get(nombreProducto.toLowerCase());
        if (producto == null) return false;

        producto.setCantidad(nuevoStock);
        return true;
    }

    /**
     * Devuelve una lista con todos los productos registrados
     * 
     * @return Lista de objetos Producto
     */
    public List<Producto> listaProductos() {
        return new ArrayList<>(repositorio.values());
    }
}
