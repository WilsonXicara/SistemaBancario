/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cajero;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author USUARIO
 */
public class PrincipalCajero extends javax.swing.JFrame {
    Connection conexion;
    int Id;
    /**
     * Creates new form Principal
     */
    public PrincipalCajero() {
        try {
            initComponents();
            this.setLocationRelativeTo(null);
            Class.forName("org.gjt.mm.mysql.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/sistemabancario" , "otro", "otro");
            IniciarSesion();
        } catch (SQLException ex) {
            Logger.getLogger(PrincipalCajero.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PrincipalCajero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Panel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout PanelLayout = new javax.swing.GroupLayout(Panel);
        Panel.setLayout(PanelLayout);
        PanelLayout.setHorizontalGroup(
            PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        PanelLayout.setVerticalGroup(
            PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PrincipalCajero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PrincipalCajero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PrincipalCajero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrincipalCajero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PrincipalCajero().setVisible(true);
                
            }
        });
        
    }
    
    public void cambio(int opcion) throws SQLException{
        switch(opcion){
            case 1: IniciarSesion();
                    break;
            case 2: opciones();
                    break;
            case 3: EstadoCuenta();
                    break;
            case 4: retiro();
                    break;
            default: break;
        }
    }
    
    //Crear ventana iniciar sesion
    public void IniciarSesion() throws SQLException{
        Panel.removeAll();
        InicioSesion nuevo = new InicioSesion(this,conexion);
        nuevo.setLocation(0,0);
        nuevo.setSize(400, 300);
        Panel.add(nuevo);
        Panel.revalidate();
        Panel.repaint();
    }
    
    //ventanas de mensaje
    public void mensaje(String mensaje,int cambio){
        Panel.removeAll();
        Mensaje nuevo = new Mensaje(mensaje,this,cambio);
        nuevo.setLocation(0,0);
        nuevo.setSize(400, 300);
        Panel.add(nuevo);
        Panel.revalidate();
        Panel.repaint();
    }
    
    //ventanas de mensaje al ingresar correctamente
    public void mensaje(String mensaje,int Id,int cambio){
        mensaje(mensaje,cambio);
        this.Id = Id;
    }
    
    //menu de opciones
    public void opciones(){
        Panel.removeAll();
        Opciones nuevo = new Opciones(this);
        nuevo.setLocation(0,0);
        nuevo.setSize(400, 300);
        Panel.add(nuevo);
        Panel.revalidate();
        Panel.repaint();
    }
    
    //Muesta saldo de cuenta
    public void EstadoCuenta(){
        Panel.removeAll();
        EstadoCuenta nuevo = new EstadoCuenta(this,conexion);
        nuevo.setLocation(0,0);
        nuevo.setSize(400, 300);
        Panel.add(nuevo);
        Panel.revalidate();
        Panel.repaint();
    }
    
    //Muestra menu retiro
    public void retiro(){
        Panel.removeAll();
        Retiro nuevo = new Retiro(this,conexion);
        nuevo.setLocation(0,0);
        nuevo.setSize(400, 300);
        Panel.add(nuevo);
        Panel.revalidate();
        Panel.repaint();
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JPanel Panel;
    // End of variables declaration//GEN-END:variables
}
