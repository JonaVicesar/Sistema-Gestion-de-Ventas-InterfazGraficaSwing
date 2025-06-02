package com.ventas.auth;

/**
 *
 * @author Jona Vicesar
 */
public class Usuario {
    private String username;
    private String password;
    private String nombre;
    private String rol;
    

    public Usuario() {}
    
    public Usuario(String username, String password, String nombre, String rol) {
        setUsername(username);
        setPassword(password);
        setNombre(nombre);
        setRol(rol);
    }
    
    // Getters y Setters
    public String getUsername() {
        return username; 
    }
    
    private void setUsername(String username) { 
        this.username = username; 
    }
    
    public String getPassword() { 
        return password; 
    }
    
    private void setPassword(String password) { 
        this.password = password; 
    }
    
    public String getNombre() { 
        return nombre; 
    }
    
    private void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
    
    public String getRol() { 
        return rol; 
    }
    
    private void setRol(String rol) { 
        this.rol = rol; 
        }
}
