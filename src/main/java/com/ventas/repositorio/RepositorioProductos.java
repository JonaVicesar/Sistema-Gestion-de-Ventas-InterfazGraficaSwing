package com.ventas.repositorio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ventas.modelo.Producto;
import com.ventas.util.PathManager;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Clase que actúa como repositorio de productos con persistencia JSON
 * 
 * @author Jona Vicesar
 */
public class RepositorioProductos {

    private static final String FILE_NAME = "productos.json";
    private final HashMap<String, Producto> repositorio = new HashMap<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final String filePath;
    private int contadorId = 1;

    public RepositorioProductos() {
        this.filePath = PathManager.getDataPath(FILE_NAME);
        cargarDatos();
    }

    private void cargarDatos() {
        try (FileReader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<ArrayList<Producto>>(){}.getType();
            List<Producto> productos = gson.fromJson(reader, listType);
            
            if (productos != null) {
                int maxId = 0;
                for (Producto producto : productos) {
                    repositorio.put(producto.getNombre().toLowerCase(), producto);
                    // Encontrar el ID más alto para continuar la secuencia
                    if (producto.getId() > maxId) {
                        maxId = producto.getId();
                    }
                }
                // El próximo ID será el máximo + 1
                contadorId = maxId + 1;
            }
        } catch (IOException e) {
            // Si el archivo no existe, se inicia con repositorio vacío
            System.out.println("Iniciando nuevo repositorio de productos");
        }
    }

    private void guardarDatos() {
        try (FileWriter writer = new FileWriter(filePath)) {
            List<Producto> productos = new ArrayList<>(repositorio.values());
            gson.toJson(productos, writer);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar productos: " + e.getMessage(), e);
        }
    }

    /**
     * Agrega un nuevo producto al repositorio
     * El ID es asignado automáticamente
     * @param nombre Nombre del producto
     * @param precio Precio unitario
     * @param cantidad Stock disponible
     * @return true si se agredo correctamente
     */
    public boolean agregarProducto(String nombre, double precio, int cantidad) {
        Producto producto = new Producto(nombre, contadorId, precio, cantidad);
        repositorio.put(nombre.toLowerCase(), producto);
        contadorId += 1;
        guardarDatos();
        System.out.println("oiko");
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
        guardarDatos();
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
        guardarDatos();
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
        guardarDatos();
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
        guardarDatos();
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
        guardarDatos();
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
    
    /**
     * 
     * @return 
     */
    public int cantidadProductos(){
        return repositorio.size();
    }
}