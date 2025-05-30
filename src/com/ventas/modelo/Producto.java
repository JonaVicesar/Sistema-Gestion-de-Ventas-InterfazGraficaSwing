package com.ventas.modelo;

/**
 * Clase que representa un producto en el sistema de ventas
 * Cada producto tiene un nombre, un código único (id), un precio y una cantidad (stock)
 * 
 * @author Jona Vicesar
 */
public class Producto {
  
    private String nombre;
    private int id;
    private double precio;
    private int cantidad;

    /**
     * Constructor por defecto
     */
    public Producto() {
    }

    /**
     * Constructor con todos los atributos
     * 
     * @param nombre Nombre del producto
     * @param id Código unico
     * @param precio Precio unitario
     * @param cantidad Cantidad disponible en stock
     */
    public Producto(String nombre, int id, double precio, int cantidad) {
        this.nombre = nombre;
        this.id = id;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    // Getters y setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
