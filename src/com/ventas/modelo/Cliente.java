package com.ventas.modelo;

/**
 * Clase que representa a un Cliente dentro del sistema de ventas.
 * Contiene datos personales y de pago.
 * 
 * @author Jona Vicesar
 */
public class Cliente {

    // Atributos privados del cliente
    private String nombreCompleto;
    private String metodoPago;
    private String tarjeta;
    private int edad;
    private int documento;
    private String telefono;

    /**
     * Constructor por defecto inicializa el nombre como "No identificado"
     */
    public Cliente() {
        this.nombreCompleto = "No identificado";
    }

    /**
     * Constructor para clientes sin tarjeta asociada.
     *
     * @param nombreCompleto Nombre completo del cliente
     * @param edad Edad del cliente
     * @param documento Número de documento del cliente
     * @param telefono Teléfono del cliente
     * @param metodoPago Método de pago (por ejemplo: "Efectivo", "Tarjeta", etc.)
     */
    public Cliente(String nombreCompleto, int edad, int documento, String telefono, String metodoPago) {
        this.nombreCompleto = nombreCompleto;
        this.edad = edad;
        this.documento = documento;
        this.telefono = telefono;
        this.metodoPago = metodoPago;
    }

    /**
     * Constructor para clientes que pagan con tarjeta
     *
     * @param nombreCompleto Nombre completo del cliente
     * @param edad Edad del cliente
     * @param documento Número de documento del cliente
     * @param telefono Telefono del cliente
     * @param metodoPago Método de pago
     * @param tarjeta Numero de tarjeta del cliente
     */
    public Cliente(String nombreCompleto, int edad, int documento, String telefono, String metodoPago, String tarjeta) {
        this.nombreCompleto = nombreCompleto;
        this.edad = edad;
        this.documento = documento;
        this.telefono = telefono;
        this.metodoPago = metodoPago;
        this.tarjeta = tarjeta;
    }

    // Getters y setters

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getDocumento() {
        return documento;
    }

    public void setDocumento(int documento) {
        this.documento = documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Devuelve una representación en texto del cliente
     * 
     */

}
