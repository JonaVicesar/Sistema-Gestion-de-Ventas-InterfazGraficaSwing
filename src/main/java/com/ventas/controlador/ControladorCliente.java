package com.ventas.controlador;

import com.ventas.modelo.Cliente;
import com.ventas.repositorio.RepositorioCliente;
import java.util.List;

/**
 * Clase encargada de manejar la lógica y validación relacionada con clientes
 * 
 * @author Jona Vicesar
 */
public class ControladorCliente {

    private final RepositorioCliente repositorio;

    // Constantes para validaciones
    private final int EDAD_MINIMA = 13;
    private final int DOCUMENTO_MINIMO = 100000;

    public ControladorCliente(RepositorioCliente repositorio) {
        this.repositorio = repositorio;
    }
    
    public ControladorCliente() {
        this.repositorio = new RepositorioCliente();
    }

    /**
     * Agrega un nuevo cliente (sin tarjeta) después de validar los datos
     *
     * @param nombre Nombre del cliente
     * @param edad Edad del cliente
     * @param documento Documento único del cliente
     * @param telefono Teléfono del cliente
     * @param metodo Método de pago
     * @return true si se agrega correctamente
     * @throws IllegalArgumentException si los datos no son válidos
     */
    public boolean agregarCliente(String nombre, int edad, int documento, String telefono, String metodo) {
        validarDatosBasicos(nombre, edad, documento);
        
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
     * @param telefono Teléfono
     * @param metodo Método de pago
     * @param tarjeta Número de tarjeta
     * @return true si se agrega correctamente
     * @throws IllegalArgumentException si los datos no son válidos
     */
    public boolean agregarCliente(String nombre, int edad, int documento, String telefono, String metodo, String tarjeta) {
        validarDatosBasicos(nombre, edad, documento);
        validarTarjeta(tarjeta);
        
        if (repositorio.existeCliente(documento)) {
            throw new IllegalArgumentException("Ya existe un cliente con el mismo documento");
        }

        return repositorio.agregarCliente(nombre, edad, documento, telefono, metodo, tarjeta);
    }

    /**
     * Elimina un cliente a partir de su documento
     *
     * @param documento Documento del cliente a eliminar
     * @return true si se elimina correctamente
     * @throws IllegalArgumentException si el documento es inválido o no existe
     */
    public boolean eliminarCliente(int documento) {
        if (documento < DOCUMENTO_MINIMO) {
            throw new IllegalArgumentException("Número de documento incorrecto");
        }
        if (!repositorio.existeCliente(documento)) {
            throw new IllegalArgumentException("No existe el cliente con documento: " + documento);
        }

        return repositorio.eliminarCliente(documento);
    }

    /**
     * Edita el nombre de un cliente existente
     *
     * @param nuevoNombre Nuevo nombre
     * @param documento Documento del cliente
     * @return true si se edita correctamente
     * @throws IllegalArgumentException si los datos no son válidos
     */
    public boolean editarNombre(String nuevoNombre, int documento) {
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (!repositorio.existeCliente(documento)) {
            throw new IllegalArgumentException("No existe el cliente con documento: " + documento);
        }

        return repositorio.editarNombreCliente(nuevoNombre, documento);
    }

    /**
     * Edita el número de teléfono de un cliente
     *
     * @param nuevoTelefono Nuevo número
     * @param documento Documento del cliente
     * @return true si se edita correctamente
     * @throws IllegalArgumentException si los datos no son válidos
     */
    public boolean editarTelefono(String nuevoTelefono, int documento) {
        validarTelefono(nuevoTelefono);
        
        if (!repositorio.existeCliente(documento)) {
            throw new IllegalArgumentException("No existe el cliente con documento: " + documento);
        }

        return repositorio.editarTelefonoCliente(nuevoTelefono, documento);
    }

    /**
     * Edita el método de pago de un cliente
     *
     * @param documento Documento del cliente
     * @param metodo Nuevo método de pago
     * @param tarjeta Número de tarjeta (puede ser null si no aplica)
     * @return true si se edita correctamente
     * @throws IllegalArgumentException si los datos no son válidos
     */
    public boolean editarMetodoPago(int documento, String metodo, String tarjeta) {
        if (!repositorio.existeCliente(documento)) {
            throw new IllegalArgumentException("No existe el cliente con documento: " + documento);
        }
        
        if (tarjeta != null && !tarjeta.trim().isEmpty()) {
            validarTarjeta(tarjeta);
        }

        return repositorio.editarMetodoPago(documento, metodo, tarjeta);
    }

    /**
     * Obtiene un cliente por su documento
     *
     * @param documento Documento del cliente
     * @return Cliente encontrado o null si no existe
     */
    public Cliente obtenerCliente(int documento) {
        return repositorio.getCliente(documento);
    }

    /**
     * Verifica si existe un cliente con el documento dado
     *
     * @param documento Documento a verificar
     * @return true si existe el cliente
     */
    public boolean existeCliente(int documento) {
        return repositorio.existeCliente(documento);
    }

    /**
     * Devuelve la lista completa de clientes registrados
     *
     * @return Lista de objetos Cliente
     */
    public List<Cliente> listaClientes() {
        return repositorio.listaClientes();
    }
    
    public int cantidadClientes(){
        return repositorio.cantidadClientes();
    }

    /**
     * Busca clientes por nombre (búsqueda parcial, case-insensitive)
     *
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de clientes que coinciden con la búsqueda
     */
    public List<Cliente> buscarClientesPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return listaClientes();
        }
        
        return listaClientes().stream()
                .filter(cliente -> cliente.getNombreCompleto().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }

    /**
     * Busca un cliente por documento
     *
     * @param documento Documento a buscar
     * @return Cliente encontrado o null si no existe
     */
    public Cliente buscarClientePorDocumento(int documento) {
        return repositorio.getCliente(documento);
    }

    // Métodos privados de validación
    private void validarDatosBasicos(String nombre, int edad, int documento) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (edad <= EDAD_MINIMA) {
            throw new IllegalArgumentException("El cliente debe ser mayor de " + EDAD_MINIMA + " años");
        }
        if (documento < DOCUMENTO_MINIMO) {
            throw new IllegalArgumentException("Número de documento incorrecto (debe ser mayor a " + DOCUMENTO_MINIMO + ")");
        }
    }

    private void validarTarjeta(String tarjeta) {
        String validacionDeTarjeta = "^\\d{14}$";
        if (tarjeta == null || !tarjeta.matches(validacionDeTarjeta)) {
            throw new IllegalArgumentException("El número de tarjeta debe tener exactamente 14 dígitos");
        }
    }

    private void validarTelefono(String telefono) {
        String regex = "^(09\\d{8})$"; // Validación para números de teléfono
        if (telefono == null || !telefono.matches(regex)) {
            throw new IllegalArgumentException("El número de teléfono debe comenzar con 09 y tener 10 dígitos");
        }
    }
}