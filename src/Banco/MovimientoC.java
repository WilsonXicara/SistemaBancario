/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Banco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author SERGIO MALDONADO
 */
public class MovimientoC extends javax.swing.JFrame {

    /**
     * Creates new form MovimientoC
     */
    Connection conexion;
    ResultSet cuenta;
    float SaldoC;
    public MovimientoC() {
        initComponents();
    }
    public MovimientoC(Connection conex, ResultSet dcuen) {
        initComponents();
        conexion = conex;
        cuenta = dcuen;
        jLabel3.setVisible(false);
        NoCuentaC.setVisible(false);
        try {
            NoCuenta.setText(cuenta.getString(2));
            SaldoC = Float.parseFloat(cuenta.getString(5));
        } catch (SQLException ex) {
            Logger.getLogger(MovimientoC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void ChequeE(){
        String Instruccion = "",Instruccion2 = "",Instruccion3 = "";
        String monto = Monto.getText();
        String NoC = NoCheque.getText();
        PreparedStatement pst = null;
        if((monto.equals(""))&&(NoC.equals(""))){
            JOptionPane.showMessageDialog(null, "Campo Vacio");
        }
        else if (Float.parseFloat(monto)>SaldoC){
            JOptionPane.showMessageDialog(null, "La cuenta no tiene saldo suficiente para realizar esta operacion");
        }
        else{
            try {
                //Iniciamos Transaccion
                pst = conexion.prepareStatement("START TRANSACTION");
                int a = pst.executeUpdate();

                //Insert al movimiento
                Instruccion = "INSERT INTO movimiento (movimiento.Cuenta_Id,movimiento.Fecha,movimiento.Monto,movimiento.Tipo) VALUES (" +
                        cuenta.getString(1) + ",NOW()," + monto + "," + "4" + ");";

                pst = conexion.prepareStatement(Instruccion);
                a = pst.executeUpdate();

                //Update a la cuenta
                Instruccion2 = "UPDATE cuenta SET cuenta.Saldo = cuenta.Saldo - " + monto + " WHERE cuenta.Id = " + cuenta.getString(1) + ";";
                pst = conexion.prepareStatement(Instruccion2);
                a = pst.executeUpdate();
                
                //Insert Cheque de la cual se sacó dinero 
                Statement sentencia = conexion.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                ResultSet idMov = sentencia.executeQuery("SELECT M.Id FROM cuenta C INNER JOIN movimiento M ON C.Id = M.Cuenta_Id WHERE C.Id = " + cuenta.getString(1) + ";" );
                idMov.last();   //Nos movemos al ultimo
                idMov.previous();   //Retrocedemos
                Instruccion3 = "INSERT INTO cheque (cheque.Movimiento_Id,cheque.Numero,cheque.Fecha) VALUES (" +
                                idMov.getString(1) + ",'" + NoC + "',NOW());" ;
                pst = conexion.prepareStatement(Instruccion3);
                a = pst.executeUpdate();
                
                //Hacemos commit
                pst = conexion.prepareStatement("COMMIT");
                a = pst.executeUpdate();
                System.out.println("Se logró el commit");
                JOptionPane.showMessageDialog(null, "Se ha realizado el Movimiento a la cuenta " + cuenta.getString(2));
                SaldoC = SaldoC - Float.parseFloat(monto);
            } catch (SQLException ex) {
                System.err.println("ERROR: " + ex.getMessage());
                try {
                    pst = conexion.prepareStatement("ROLLBACK;");
                    int b = pst.executeUpdate();
                    System.out.println("Se logró el Rollback");
                    JOptionPane.showMessageDialog(null, "Error Inesperado");
                    //conexion.rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(MovimientoC.class.getName()).log(Level.SEVERE, null, ex1);
                    System.out.println("No se pudo realizar el Rollback");
                }
                Logger.getLogger(MovimientoC.class.getName()).log(Level.SEVERE, null, ex);
            }               
        }
    }
    public void ChequeD(){
        String Instruccion = "",Instruccion1 = "",Instruccion2 = "", Instruccion3 = "",Instruccion4 = "";
        String NoC = NoCheque.getText();
        String monto = Monto.getText();
        String Cuenta2 = NoCuentaC.getText();
        PreparedStatement pst = null;
        ResultSet noMov = null;
        if((monto.equals(""))&&(NoC.equals(""))){
            JOptionPane.showMessageDialog(null, "Campo Vacio");
        }
        else if (Float.parseFloat(monto)>SaldoC){
            JOptionPane.showMessageDialog(null, "La cuenta no tiene saldo suficiente para realizar esta operacion");
        }
        else{
            try {
                 //Iniciamos Transaccion
                pst = conexion.prepareStatement("START TRANSACTION");
                int a = pst.executeUpdate();
                
                //Insert al movimiento de retiro de cheque
                Instruccion = "INSERT INTO movimiento (movimiento.Cuenta_Id,movimiento.Fecha,movimiento.Monto,movimiento.Tipo) VALUES (" +
                                cuenta.getString(1) + ",NOW()," + monto + "," + "4" + ");";
                pst = conexion.prepareStatement(Instruccion);
                a = pst.executeUpdate();
                
                //Insert del movimiento de deposito de cheque
                Statement sentencia = conexion.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                noMov = sentencia.executeQuery("SELECT * FROM cuenta WHERE cuenta.Numero = '" + Cuenta2+ "';" );
                noMov.next();
                Instruccion1 = "INSERT INTO movimiento (movimiento.Cuenta_Id,movimiento.Fecha,movimiento.Monto,movimiento.Tipo) VALUES (" +
                                noMov.getString(1) + ",NOW()," + monto + "," + "5" + ");";
                pst = conexion.prepareStatement(Instruccion1);
                a = pst.executeUpdate();
                
                 //Update a la cuenta de retiro de cheque
                Instruccion2 = "UPDATE cuenta SET cuenta.Saldo = cuenta.Saldo - " + monto + " WHERE cuenta.Id = " + cuenta.getString(1) + ";";
                pst = conexion.prepareStatement(Instruccion2);
                a = pst.executeUpdate();
                
                //Update a la cuenta de deposito de cheque
               Instruccion3 = "UPDATE cuenta SET cuenta.Saldo = cuenta.Saldo + " + monto + " WHERE cuenta.Id = " + noMov.getString(1) + ";";
                pst = conexion.prepareStatement(Instruccion3);
                a = pst.executeUpdate();
                
                //Insert al del cheque Pendiente
                Statement sentencia2 = conexion.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                ResultSet idMov = sentencia2.executeQuery("SELECT M.Id FROM cuenta C INNER JOIN movimiento M ON C.Id = M.Cuenta_Id WHERE C.Id = " + cuenta.getString(1) + ";" );
                idMov.last();   //Nos movemos al ultimo
                idMov.previous();   //Retrocedemos
                Instruccion4 = "INSERT INTO cheque (cheque.Movimiento_Id,cheque.Numero,cheque.Fecha) VALUES (" +
                                idMov.getString(1) + ",'" + NoC + "',NOW());" ;
                pst = conexion.prepareStatement(Instruccion4);
                a = pst.executeUpdate();
                
                
                //Hacemos commit
                pst = conexion.prepareStatement("COMMIT");
                a = pst.executeUpdate();
                System.out.println("Se logró el commit");
                JOptionPane.showMessageDialog(null, "Se ha realizado el Movimiento a la cuenta " + cuenta.getString(2));
                SaldoC = SaldoC - Float.parseFloat(monto);
            } catch (SQLException ex) {
                System.err.println("ERROR: " + ex.getMessage());
                try {
                    pst = conexion.prepareStatement("ROLLBACK;");
                    int b = pst.executeUpdate();
                    System.out.println("Se logró el Rollback");
                    JOptionPane.showMessageDialog(null, "Error Inesperado");
                    //conexion.rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(MovimientoC.class.getName()).log(Level.SEVERE, null, ex1);
                    System.out.println("No se pudo realizar el Rollback");
                }
                Logger.getLogger(MovimientoC.class.getName()).log(Level.SEVERE, null, ex);
            }   
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Tipo = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        NoCuenta = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        NoCuentaC = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        NoCheque = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        Monto = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Cheque"));

        jLabel1.setText("Tipo");

        Tipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cheque en Efectivo", "Depositar Cheque" }));
        Tipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TipoActionPerformed(evt);
            }
        });

        jLabel2.setText("NoCuenta del Cheque");

        NoCuenta.setEditable(false);

        jLabel3.setText("NoCuenta a Depositar");

        jButton1.setText("Aceptar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setText("No.Cheque");

        jLabel5.setText("Monto");

        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(NoCuentaC)
                                    .addComponent(NoCheque)
                                    .addComponent(Monto, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Tipo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(NoCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))))
                .addContainerGap(228, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(NoCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel1)
                        .addGap(13, 13, 13))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(NoCuentaC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(NoCheque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(Monto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addContainerGap(73, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        new Cuenta(conexion,cuenta).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void TipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TipoActionPerformed
        // TODO add your handling code here:
        if(Tipo.getSelectedIndex()==1){
            jLabel3.setVisible(true);
            NoCuentaC.setVisible(true);
        }
        if(Tipo.getSelectedIndex()==0){
            jLabel3.setVisible(false);
            NoCuentaC.setVisible(false);
        }
    }//GEN-LAST:event_TipoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if(Tipo.getSelectedIndex()==0){
            ChequeE();
        }
        if(Tipo.getSelectedIndex()==1){
            ChequeD();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        NoCuentaC.setText("");
        NoCheque.setText("");
        Monto.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(MovimientoC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MovimientoC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MovimientoC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MovimientoC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MovimientoC().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Monto;
    private javax.swing.JTextField NoCheque;
    private javax.swing.JTextField NoCuenta;
    private javax.swing.JTextField NoCuentaC;
    private javax.swing.JComboBox<String> Tipo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
