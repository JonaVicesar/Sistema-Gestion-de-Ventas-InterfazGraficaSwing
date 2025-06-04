package com.ventas.util;

import java.io.File;

/**
 * Gestor de rutas para el proyecto
 * @author Jona Vicesar
 */
public class PathManager {
    private static final String DIRECTORIO_PRINCIPAL = "data";
    private static final String DIRECTORIO_UTIL = "src/main/java/com/ventas/util";
    
    /**
     * Obtiene la ruta completa para un archivo en el directorio data
     * @param filename nombre del archivo
     * @return ruta completa del archivo
     */
    public static String getDataPath(String filename) {
        File dataDir = new File(DIRECTORIO_PRINCIPAL);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        return new File(dataDir, filename).getAbsolutePath();
    }
    
    /**
     * Obtiene la ruta completa para un recurso en el directorio util
     * @param filename nombre del archivo de recurso
     * @return ruta completa del recurso
     */
    public static String getResourcePath(String filename) {
        return new File(DIRECTORIO_UTIL, filename).getAbsolutePath();
    }
    
    /**
     * Verifica si un archivo existe en el directorio data
     * @param filename nombre del archivo
     * @return true si existe, false si no
     */
    public static boolean dataFileExists(String filename) {
        return new File(getDataPath(filename)).exists();
    }
    
    /**
     * Verifica si un recurso existe
     * @param filename nombre del recurso
     * @return true si existe, false si no
     */
    public static boolean resourceExists(String filename) {
        return new File(getResourcePath(filename)).exists();
    }
    
    /**
     * Crea el directorio data si no existe
     */
    public static void ensureDataDirectoryExists() {
        File dataDir = new File(DIRECTORIO_PRINCIPAL);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }
}