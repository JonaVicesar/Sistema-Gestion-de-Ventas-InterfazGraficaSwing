package com.ventas.repositorio;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ventas.auth.Usuario;
import com.ventas.util.PathManager;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para manejar usuarios del sistema
 * @author Jona Vicesar
 */
public class RepositorioUsuarios {
    private static final String ARCHIVO_USUARIOS = "usuarios.json";
    private final Gson gson;
    private final String rutaArchivo;
    
    public RepositorioUsuarios() {
        this.gson = new Gson();
        this.rutaArchivo = PathManager.getDataPath(ARCHIVO_USUARIOS);
        inicializarArchivoSiNoExiste();
    }
    
    /**
     * Crea el archivo de usuarios con datos por defecto si no existe
     */
    private void inicializarArchivoSiNoExiste() {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            List<Usuario> usuariosPorDefecto = new ArrayList<>();
            // Usuario administrador por defecto
            usuariosPorDefecto.add(new Usuario("admin", "admin123", "Administrador", "ADMIN"));
            usuariosPorDefecto.add(new Usuario("vendedor", "venta123", "Vendedor", "VENDEDOR"));
            
            guardarUsuarios(usuariosPorDefecto);
        }
    }
    
    /**
     * Carga todos los usuarios desde el archivo JSON
     */
    private List<Usuario> cargarUsuarios() {
        try (FileReader reader = new FileReader(rutaArchivo)) {
            Type tipoLista = new TypeToken<List<Usuario>>(){}.getType();
            List<Usuario> usuarios = gson.fromJson(reader, tipoLista);
            return usuarios != null ? usuarios : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Guarda la lista de usuarios en el archivo JSON
     */
    private void guardarUsuarios(List<Usuario> usuarios) {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            gson.toJson(usuarios, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
        }
    }
    
    /**
     * Autentica un usuario con username y password
     * @param username nombre de usuario
     * @param password contraseña
     * @return Usuario si las credenciales son correctas, null si no
     */
    public Usuario autenticar(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            return null;
        }
        
        List<Usuario> usuarios = cargarUsuarios();
        return usuarios.stream()
                .filter(u -> u.getUsername().equals(username.trim()) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Busca un usuario por su username
     * @param username nombre de usuario
     * @return Usuario encontrado o null
     */
    public Usuario buscarPorUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        
        List<Usuario> usuarios = cargarUsuarios();
        return usuarios.stream()
                .filter(u -> u.getUsername().equals(username.trim()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Obtiene todos los usuarios
     * @return Lista de todos los usuarios
     */
    public List<Usuario> obtenerTodos() {
        return cargarUsuarios();
    }
    
    /**
     * Agrega un nuevo usuario
     * @param usuario Usuario a agregar
     * @return true si se agregó correctamente, false si el username ya existe
     */
    public boolean agregarUsuario(Usuario usuario) {
        if (usuario == null || buscarPorUsername(usuario.getUsername()) != null) {
            return false;
        }
        
        List<Usuario> usuarios = cargarUsuarios();
        usuarios.add(usuario);
        guardarUsuarios(usuarios);
        return true;
    }
    
    /**
     * Verifica si existe al menos un usuario en el sistema
     * @return true si hay usuarios registrados
     */
    public boolean hayUsuarios() {
        return !cargarUsuarios().isEmpty();
    }
}