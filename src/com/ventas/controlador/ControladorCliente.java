package com.ventas.controlador;

import com.ventas.modelo.Cliente;
import com.ventas.repositorio.RepositorioCliente;
import java.util.List;
import java.util.Scanner;

/**
 * Clase encargada de manejar la logica y validacion relacionada con
 * clientes
 *
 * @author Jona Vicesar
 */
public class ControladorCliente {

    private static final RepositorioCliente repositorio = new RepositorioCliente();
    private Scanner entrada = new Scanner(System.in);

    // Constantes para validaciones
    private final int EDAD_MINIMA = 13;
    private final int DOCUMENTO_MINIMO = 100000;

    public ControladorCliente() {
    }

    /**
     * Agrega un nuevo cliente (sin tarjeta) después de validar los datos
     *
     * @param nombre Nombre del cliente
     * @param edad Edad del cliente
     * @param documento Documento único del cliente
     * @param telefono Telefono del cliente
     * @param metodo Método de pago
     * @return true si se agrega correctamente
     */
    public boolean agregarCliente(String nombre, int edad, int documento, String telefono, String metodo) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (edad <= EDAD_MINIMA) {
            throw new IllegalArgumentException("El cliente debe ser mayor de 13");
        }
        if (documento < DOCUMENTO_MINIMO) {
            throw new IllegalArgumentException("Número de documento incorrecto");
        }
        if (repositorio.existeCliente(documento)) {
            throw new IllegalArgumentException("Ya existe un cliente con el mismo documento");
        }

        return repositorio.agregarCliente(nombre, edad, documento, telefono, metodo);
    }

    /**
     * Agrega un nuevo cliente (con tarjeta) después de validar los datos
     *
     * @param nombre Nombre del cliente
     * @param edad Edad
     * @param documento Documento
     * @param telefono Telefono
     * @param metodo Método de pago
     * @param tarjeta Número de tarjeta
     * @return true si se agrega correctamente
     */
    public boolean agregarCliente(String nombre, int edad, int documento, String telefono, String metodo, String tarjeta) {
        String validacionDeTarjeta = "^\\d{14}$";
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (edad <= EDAD_MINIMA) {
            throw new IllegalArgumentException("El cliente debe ser mayor de 13");
        }
        if (documento < DOCUMENTO_MINIMO) {
            throw new IllegalArgumentException("Número de documento incorrecto");
        }

        if (!tarjeta.matches(validacionDeTarjeta)) {
            throw new IllegalArgumentException("No es una tarjeta valida");
        }
        if (repositorio.existeCliente(documento)) {
            throw new IllegalArgumentException("Ya existe un cliente con el mismo documento");
        }

        return repositorio.agregarCliente(nombre, edad, documento, telefono, metodo, tarjeta);
    }

    /**
     * Elimina un cliente a partir de su documento.
     *
     * @param documento Documento del cliente a eliminar
     * @return true si se elimina correctamente
     */
    public boolean eliminarCliente(int documento) {
        if (documento < DOCUMENTO_MINIMO) {
            throw new IllegalArgumentException("Número de documento incorrecto");
        }
        if (!repositorio.existeCliente(documento)) {
            throw new IllegalArgumentException("No existe el cliente");
        }

        return repositorio.eliminarCliente(documento);
    }

    /**
     * Edita el nombre de un cliente existente
     *
     * @param nuevoNombre Nuevo nombre
     * @param documento Documento del cliente
     * @return true si se edita correctamente
     */
    public boolean editarNombre(String nuevoNombre, int documento) {
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (!repositorio.existeCliente(documento)) {
            throw new IllegalArgumentException("No existe el cliente");
        }

        return repositorio.editarNombreCliente(nuevoNombre, documento);
    }

    /**
     * Edita el numero de telefono de un cliente
     *
     * @param nuevoTelefono Nuevo número
     * @param documento Documento del cliente
     * @return true si se edita correctamente
     */
    public boolean editarTelefono(String nuevoTelefono, int documento) {
        String regex = "^(09\\d{8})$";//validacion para numeros de telefono
        if (!nuevoTelefono.matches(regex)) {
            throw new IllegalArgumentException("El número de teléfono es incorrecto");
        }
        if (!repositorio.existeCliente(documento)) {
            throw new IllegalArgumentException("No existe el cliente");
        }

        return repositorio.editarTelefonoCliente(nuevoTelefono, documento);
    }

    /**
     * Devuelve la lista completa de clientes registrados
     *
     * @return Lista de objetos Cliente
     */
    public List<Cliente> listaClientes() {
        return repositorio.listaClientes();
    }
}
