package com.ventas.controlador;

import com.ventas.modelo.Producto;
import com.ventas.repositorio.RepositorioProductos;
import java.util.List;

/**
 * 
 * Contiene la logica de validación antes de delegar las acciones al repositorio
 * 
 * @author Jona Vicesar
 */
public class ControladorProducto {

    // Repositorio que almacena los productos
    RepositorioProductos repositorio = new RepositorioProductos();

    public ControladorProducto() {
    }

    /**
     * Agrega un nuevo producto al sistema después de validar sus datos.
     *
     * @param nombre Nombre del producto
     * @param precio Precio unitario 
     * @param cantidad Stock inicial 
     * @return true si el producto fue agregado correctamente
     */
    public boolean agregarProducto(String nombre, double precio, int cantidad) {
        if (repositorio.existeProducto(nombre.trim().toLowerCase())) {
            throw new IllegalArgumentException("¡El producto ya existe!");
        }
        if (precio <= 0 || cantidad <= 0) {
            throw new IllegalArgumentException("El precio y el stock deben ser mayores a cero.");
        }

        repositorio.agregarProducto(nombre, precio, cantidad);
        return true;
    }

    /**
     * Elimina un producto del sistema
     *
     * @param nombreProducto Nombre del producto a eliminar
     * @return true si fue eliminado correctamente
     */
    public boolean eliminarProducto(String nombreProducto) {
        if (nombreProducto == null || nombreProducto.isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        if (!repositorio.existeProducto(nombreProducto)) {
            throw new IllegalArgumentException("El producto no existe ￣へ￣");
        }

        return repositorio.eliminarProducto(nombreProducto);
    }

    /**
     * Edita el nombre de un producto existente
     *
     * @param nombreProducto Nombre actual del producto
     * @param nuevoNombre Nuevo nombre deseado
     * @return true si se actualiza correctamente
     */
    public boolean editarNombre(String nombreProducto, String nuevoNombre) {
        if (nombreProducto == null || nombreProducto.isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        if (nuevoNombre == null || nuevoNombre.isEmpty()) {
            throw new IllegalArgumentException("El nuevo nombre no puede estar vacío");
        }
        if (!repositorio.existeProducto(nombreProducto)) {
            throw new IllegalArgumentException("El producto no existe ￣へ￣");
        }

        return repositorio.editarNombre(nombreProducto, nuevoNombre);
    }

    /**
     * Cambia el precio de un producto existente
     *
     * @param nombreProducto Nombre del producto
     * @param nuevoPrecio Nuevo precio 
     * @return true si fue actualizado correctamente
     */
    public boolean editarPrecio(String nombreProducto, double nuevoPrecio) {
        if (nombreProducto == null || nombreProducto.isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }

        if (nuevoPrecio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }

        if (!repositorio.existeProducto(nombreProducto)) {
            throw new IllegalArgumentException("El producto no existe ￣へ￣");
        }

        return repositorio.editarPrecio(nombreProducto, nuevoPrecio);
    }

    /**
     * Modifica el stock de un producto
     * Si el nuevo stock es cero, se elimina automáticamente el producto
     *
     * @param nombreProducto Nombre del producto
     * @param nuevoStock Nuevo stock
     * @return true si se actualiza correctamente
     */
    public boolean editarStock(String nombreProducto, int nuevoStock) {
        if (nombreProducto == null || nombreProducto.isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }

        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El nuevo stock no puede ser negativo");
        }

        if (!repositorio.existeProducto(nombreProducto)) {
            throw new IllegalArgumentException("El producto no existe ￣へ￣");
        }

        // Eliminar automáticamente si el stock llega a 0
        if (nuevoStock == 0) {
            eliminarProducto(nombreProducto);
        }

        return repositorio.editarStock(nombreProducto, nuevoStock);
    }

    /**
     * Devuelve la lista de todos los productos registrados
     *
     * @return Lista de productos
     */
    public List<Producto> listaProductos() {
        return repositorio.listaProductos();
    }
}
