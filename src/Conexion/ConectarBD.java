/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import Banco.AccederCuenta;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JFrame;

/**
 *
 * @author SERGIO MALDONADO
 */
public class ConectarBD {
    public Connection conexion;
    public void conectar(){
        try{
            System.out.println("Intentando conectar");
            Class.forName("org.gjt.mm.mysql.Driver");
            //Class.forName("com.mysql.jbdc.Driver");
            //conexion = DriverManager.getConnection("jdbc:mysql:" + Url + "/" + Nom ,User,contra);
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/sistemabancario","otro","otro");
            //System.out.println("Conexcion Exitosa");
            //mostrar(conexion);
        }
        catch(SQLException e){
            System.out.println("Error de SQL");
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        catch(Exception e){
            System.out.println("Error " + e.getMessage());
        }
    }
   
}
