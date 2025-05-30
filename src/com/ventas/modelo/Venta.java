package com.ventas.modelo;

import java.time.LocalDate;
import java.util.HashMap;

/**
 * Clase que representa una Venta realizada por un cliente
 * Contiene un identificador único, el cliente asociado, los productos comprados,
 * la fecha de la venta y el total calculado
 * 
 * @author Jona
 */
public class Venta {

    private int idVenta; 
    private Cliente cliente; 
    private HashMap<Producto, Integer> listaCompras; 
    private LocalDate fecha; 
    private double total; 

    /**
     * Constructor de la clase venta
     *
     * @param idVenta ID único de la venta
     * @param cliente Cliente que realiza la compra
     * @param listaCompras Mapa de productos con la cantidad comprada
     * @param fecha Fecha de la venta
     */
    public Venta(int idVenta, Cliente cliente, HashMap<Producto, Integer> listaCompras, LocalDate fecha) {
        this.idVenta = idVenta;
        this.cliente = cliente;
        this.listaCompras = listaCompras;
        this.fecha = fecha;
        calcularTotal(); // Calcula el total automáticamente al crear la venta
    }

    /**
     * Calcula el total de la venta multiplicando el precio de cada producto por su cantidad
     */
    public void calcularTotal() {
        this.total = listaCompras.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrecio() * entry.getValue())
                .sum();
    }

    // Getters y Setters

    public int getIdVenta() {
        return idVenta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public HashMap<Producto, Integer> getListaCompras() {
        return listaCompras;
    }

    public double getTotal() {
        return total;
    }
}
