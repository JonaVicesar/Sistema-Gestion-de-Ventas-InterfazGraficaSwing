package com.ventas;

import com.ventas.modelo.Cliente;
import com.ventas.modelo.Producto;
import java.io.IOException;

import com.ventas.repositorio.RepositorioCliente;
import com.ventas.repositorio.RepositorioProductos;
import com.ventas.repositorio.RepositorioVentas;
import com.ventas.vista.LoaderComponent;
import com.ventas.vista.Login;
import com.ventas.vista.MenuClientes;
import com.ventas.vista.MenuPrincipal;
import com.ventas.vista.MenuProductos;
import com.ventas.vista.MenuVentas;
import java.time.LocalDate;
import java.util.HashMap;
import javax.swing.JFrame;

/**
 *
 * @author Jona Vicesar
 */
public class VentasApp {

    public static void main(String[] xyz) throws IOException {
        /*JFrame j = new JFrame("cd");
        j.setSize(1920, 1080);
        LoaderComponent l = new LoaderComponent();
        j.add(l);
        j.setVisible(true);
        l.setBackgroundImage("srcd\\com\\ventas\\vista\\mueble-caja-para-supermercado.jpg");
        l.startAnimation();
        */
        
       /* Cliente yo = new Cliente("Lionel", 38, 53356832, "0987676767" ,"efectivo");
        HashMap<Producto, Integer> prod = new HashMap<>();
        Producto tomate = new Producto("Pringles", 14, 1000, 15);
        prod.put(tomate, 5);*/
        
        RepositorioCliente g = new RepositorioCliente();
        RepositorioProductos k = new RepositorioProductos();
        RepositorioVentas h = new RepositorioVentas();
        //h.crearVenta(yo, prod, LocalDate.EPOCH);
        
        //k.agregarProducto("Pringles", 14, 1000);
        //g.agregarCliente("Lionel", 38, 53356832, "0987676767" ,"efectivo");
        //System.out.println("Hola mundo");
        //System.out.println(g.getCliente(5756832).getEdad());
        
     //  MenuPrincipal menu = new MenuPrincipal();
      // menu.setVisible(true);
       //MenuClientes cliente = new MenuClientes();
       //cliente.setVisible(true);
       //MenuProductos p = new MenuProductos();
       //p.setVisible(true);
       
    Login nuevo = new Login();
   nuevo.setVisible(true);   
    }
}