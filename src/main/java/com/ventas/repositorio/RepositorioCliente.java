package com.ventas.repositorio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ventas.modelo.Cliente;
import com.ventas.util.PathManager;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repositorio encargado de manejar la persistencia de clientes usando JSON
 * 
 * @author Jona Vicesar
 */
public class RepositorioCliente {

    private static final String FILE_NAME = "clientes.json";
    private final Map<Integer, Cliente> repositorio = new HashMap<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final String filePath;

    public RepositorioCliente() {
        this.filePath = PathManager.getDataPath(FILE_NAME);
        cargarDatos();
    }

    private void cargarDatos() {
        try (FileReader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<ArrayList<Cliente>>(){}.getType();
            List<Cliente> clientes = gson.fromJson(reader, listType);
            
            if (clientes != null) {
                for (Cliente cliente : clientes) {
                    repositorio.put(cliente.getDocumento(), cliente);
                }
            }
        } catch (IOException e) {
            // Si el archivo no existe, se inicia con repositorio vacío
            System.out.println("Iniciando nuevo repositorio de clientes");
        }
    }

    private void guardarDatos() {
        try (FileWriter writer = new FileWriter(filePath)) {
            List<Cliente> clientes = new ArrayList<>(repositorio.values());
            gson.toJson(clientes, writer);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar clientes: " + e.getMessage(), e);
        }
    }

    public boolean agregarCliente(String nombre, int edad, int documento, String telefono, String metodo) {
        Cliente cliente = new Cliente(nombre, edad, documento, telefono, metodo);
        repositorio.put(documento, cliente);
        System.out.print("oikooooo");
        guardarDatos();
        return true;
    }

    public boolean agregarCliente(String nombre, int edad, int documento, String telefono, String metodo, String tarjeta) {
        Cliente cliente = new Cliente(nombre, edad, documento, telefono, metodo, tarjeta);
        repositorio.put(documento, cliente);
        guardarDatos();
        System.out.print("oikooooo");
        return true;
    }

    public boolean eliminarCliente(int documento) {
        if (repositorio.remove(documento) != null) {
            guardarDatos();
            return true;
        }
        return false;
    }

    public boolean editarNombreCliente(String nuevoNombre, int documento) {
        Cliente cliente = repositorio.get(documento);
        if (cliente != null) {
            cliente.setNombreCompleto(nuevoNombre);
            guardarDatos();
            return true;
        }
        return false;
    }

    public boolean editarTelefonoCliente(String nuevoTelefono, int documento) {
        Cliente cliente = repositorio.get(documento);
        if (cliente != null) {
            cliente.setTelefono(nuevoTelefono);
            guardarDatos();
            return true;
        }
        return false;
    }

    public boolean editarMetodoPago(int documento, String metodo, String tarjeta) {
        Cliente cliente = repositorio.get(documento);
        if (cliente != null) {
            cliente.setMetodoPago(metodo);
            cliente.setTarjeta(tarjeta);
            guardarDatos();
            return true;
        }
        return false;
    }

    public List<Cliente> listaClientes() {
        return new ArrayList<>(repositorio.values());
    }

    public boolean existeCliente(int documento) {
        return repositorio.containsKey(documento);
    }

    public Cliente getCliente(int documento) {
        return repositorio.get(documento);
    }
    
    public int cantidadClientes(){
        return repositorio.size();
    }
}