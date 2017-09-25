/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Banco;

import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
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
    float SaldoC;
    JFrame papa;
    public MovimientoC() {
        initComponents();
    }
    public MovimientoC(Connection conex,JFrame dad) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setMaximumSize(new Dimension(602,530));
        this.setMinimumSize(new Dimension(602,530));
        papa = dad;
        conexion = conex;
        jLabel3.setVisible(false);
        NoCuentaC.setVisible(false);
        //PanelC.setVisible(false);
        //PanelE.setVisible(false);
        
        
    }
    public void DepositoE(){
        String Instruccion = "",Instruccion2 = "";
        String Cuenta1 = NoCuentaE.getText();
        String monto = MontoE.getText();
        PreparedStatement pst = null;
        Statement sentenciaC;
        ResultSet cuenta = null;
        try {
            sentenciaC = conexion.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            cuenta = sentenciaC.executeQuery("SELECT * FROM  cuenta WHERE cuenta.Numero = '" + Cuenta1 + "';" );
            if((monto.equals(""))||(Cuenta1.equals(""))){
                JOptionPane.showMessageDialog(null, "Campos Vacios");
            }
            else if(cuenta.next()==false){
                JOptionPane.showMessageDialog(null, "La Cuenta del cheque no existe");
            }
            else{
                try {
                 //Iniciamos Transaccion
                    pst = conexion.prepareStatement("START TRANSACTION");
                    int a = pst.executeUpdate();

                    //Insert al movimiento
                    Instruccion = "INSERT INTO movimiento (movimiento.Cuenta_Id,movimiento.Fecha,movimiento.Monto,movimiento.Tipo) VALUES (" +
                                    cuenta.getString(1) + ",NOW()," + monto + "," + "3" + ");";
                    pst = conexion.prepareStatement(Instruccion);
                    a = pst.executeUpdate();

                     //Update a la cuenta
                    Instruccion2 = "UPDATE cuenta SET cuenta.Saldo = cuenta.Saldo + " + monto + " WHERE cuenta.Id = " + cuenta.getString(1) + ";";
                    pst = conexion.prepareStatement(Instruccion2);
                    a = pst.executeUpdate();

                    //Hacemos commit
                    pst = conexion.prepareStatement("COMMIT");
                    a = pst.executeUpdate();
                    System.out.println("Se logró el commit");
                    JOptionPane.showMessageDialog(null, "Se ha realizado el Movimiento a la cuenta " + cuenta.getString(2));
                    SaldoC = SaldoC + Float.parseFloat(monto);
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
        } catch (SQLException ex) {
            Logger.getLogger(MovimientoC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void RetiroE(){
        String Instruccion = "",Instruccion2 = "";
        String Cuenta1 = NoCuentaE.getText();
        String monto = MontoE.getText();
        PreparedStatement pst = null;
        Statement sentenciaC;
        ResultSet cuenta = null;
        try {
            sentenciaC = conexion.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            cuenta = sentenciaC.executeQuery("SELECT * FROM  cuenta WHERE cuenta.Numero = '" + Cuenta1 + "';" );
            if((monto.equals(""))||(Cuenta1.equals(""))){
                JOptionPane.showMessageDialog(null, "Campos Vacios");
            }
            else if(cuenta.next()==false){
                JOptionPane.showMessageDialog(null, "La Cuenta del cheque no existe");
            }
            else{
                SaldoC = 0;
                SaldoC = Float.parseFloat(cuenta.getString(5));
                if (Float.parseFloat(monto)>SaldoC){
                    JOptionPane.showMessageDialog(null, "La cuenta " +  Cuenta1 +" no tiene saldo suficiente para realizar esta operacion");
                }
                else{
                    try {
                    //Iniciamos Transaccion
                        pst = conexion.prepareStatement("START TRANSACTION");
                        int a = pst.executeUpdate();

                        //Insert al movimiento
                        Instruccion = "INSERT INTO movimiento (movimiento.Cuenta_Id,movimiento.Fecha,movimiento.Monto,movimiento.Tipo) VALUES (" +
                                cuenta.getString(1) + ",NOW()," + monto + "," + "2" + ");";

                        pst = conexion.prepareStatement(Instruccion);
                        a = pst.executeUpdate();

                        //Update a la cuenta
                        Instruccion2 = "UPDATE cuenta SET cuenta.Saldo = cuenta.Saldo - " + monto + " WHERE cuenta.Id = " + cuenta.getString(1) + ";";
                        pst = conexion.prepareStatement(Instruccion2);
                        a = pst.executeUpdate();

                        //Hacemos commit
                        pst = conexion.prepareStatement("COMMIT");
                        a = pst.executeUpdate();
                        System.out.println("Se logró el commit");
                        JOptionPane.showMessageDialog(null, "Se ha realizado el Movimiento a la cuenta " + cuenta.getString(2));
                        //SaldoC = SaldoC - Float.parseFloat(monto);
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
        } catch (SQLException ex) {
            Logger.getLogger(MovimientoC.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    public void ChequeE(){
        String Instruccion = "",Instruccion2 = "",Instruccion3 = "";
        String Cuenta1 = NoCuenta.getText();
        String monto = Monto.getText();
        String NoC = NoCheque.getText();
        PreparedStatement pst = null;
        ResultSet cuenta = null;
        Statement sentenciaC;
        try {
            sentenciaC = conexion.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            cuenta = sentenciaC.executeQuery("SELECT * FROM  cuenta WHERE cuenta.Numero = '" + Cuenta1 + "';" );
            if((monto.equals(""))||(NoC.equals(""))||(Cuenta1.equals(""))){
                JOptionPane.showMessageDialog(null, "Campos Vacios");
            }
            else if(cuenta.next()==false){
                JOptionPane.showMessageDialog(null, "La Cuenta del cheque no existe");
            }
            else{
                SaldoC = 0;
                SaldoC = Float.parseFloat(cuenta.getString(5));
                if (Float.parseFloat(monto)>SaldoC){
                    JOptionPane.showMessageDialog(null, "La cuenta " +  Cuenta1 +" no tiene saldo suficiente para realizar esta operacion");
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
                        //idMov.previous();   //Retrocedemos
                        Instruccion3 = "INSERT INTO cheque (cheque.Movimiento_Id,cheque.Numero,cheque.Fecha) VALUES (" +
                                        idMov.getString(1) + ",'" + NoC + "',NOW());" ;
                        pst = conexion.prepareStatement(Instruccion3);
                        a = pst.executeUpdate();

                        //Hacemos commit
                        pst = conexion.prepareStatement("COMMIT");
                        a = pst.executeUpdate();
                        System.out.println("Se logró el commit");
                        JOptionPane.showMessageDialog(null, "Se ha realizado el Movimiento a la cuenta " + cuenta.getString(2));
                        //SaldoC = SaldoC - Float.parseFloat(monto);
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
        } catch (SQLException ex) {
            Logger.getLogger(MovimientoC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void ChequeD(){
        String Instruccion = "",Instruccion1 = "",Instruccion2 = "", Instruccion3 = "",Instruccion4 = "";
        String Cuenta1 = NoCuenta.getText();
        String NoC = NoCheque.getText();
        String monto = Monto.getText();
        String Cuenta2 = NoCuentaC.getText();
        PreparedStatement pst = null;
        ResultSet cuenta = null;
        Statement sentenciaC;
        ResultSet noMov = null;
        try {
            sentenciaC = conexion.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            cuenta = sentenciaC.executeQuery("SELECT * FROM  cuenta WHERE cuenta.Numero = '" + Cuenta1 + "';" );
            if((monto.equals(""))||(NoC.equals(""))||(Cuenta2.equals(""))||(Cuenta1.equals(""))){
                JOptionPane.showMessageDialog(null, "Campos Vacios");
            }
            else if(cuenta.next()==false){
                JOptionPane.showMessageDialog(null, "La Cuenta del cheque no existe");
            }
            else{
                SaldoC = 0;
                SaldoC = Float.parseFloat(cuenta.getString(5));
                if (Float.parseFloat(monto)>SaldoC){
                    JOptionPane.showMessageDialog(null, "La cuenta " +  Cuenta1 +" no tiene saldo suficiente para realizar esta operacion");
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
                        //idMov.previous();   //Retrocedemos
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
        } catch (SQLException ex) {
            Logger.getLogger(MovimientoC.class.getName()).log(Level.SEVERE, null, ex);
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

        PanelC = new javax.swing.JPanel();
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
        PanelE = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        NoCuentaE = new javax.swing.JTextField();
        MontoE = new javax.swing.JTextField();
        TipoE = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        ComboTipo = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        Vacio = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(602, 530));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        PanelC.setBorder(javax.swing.BorderFactory.createTitledBorder("Cheque"));

        jLabel1.setText("Tipo");

        Tipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cheque en Efectivo", "Depositar Cheque" }));
        Tipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TipoActionPerformed(evt);
            }
        });

        jLabel2.setText("NoCuenta del Cheque");

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

        javax.swing.GroupLayout PanelCLayout = new javax.swing.GroupLayout(PanelC);
        PanelC.setLayout(PanelCLayout);
        PanelCLayout.setHorizontalGroup(
            PanelCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(PanelCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelCLayout.createSequentialGroup()
                        .addGroup(PanelCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(PanelCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Tipo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(NoCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(PanelCLayout.createSequentialGroup()
                        .addGroup(PanelCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGroup(PanelCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelCLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(PanelCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(PanelCLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(PanelCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(NoCheque, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(NoCuentaC, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Monto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelCLayout.setVerticalGroup(
            PanelCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(PanelCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(NoCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(PanelCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(PanelCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(NoCuentaC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(PanelCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(NoCheque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PanelCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(Monto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanelE.setBorder(javax.swing.BorderFactory.createTitledBorder("Efectivo"));

        jLabel6.setText("NoCuenta");

        jLabel7.setText("Tipo");

        jLabel8.setText("Monto");

        TipoE.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Deposito", "Retiro" }));

        jButton3.setText("Aceptar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Cancelar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelELayout = new javax.swing.GroupLayout(PanelE);
        PanelE.setLayout(PanelELayout);
        PanelELayout.setHorizontalGroup(
            PanelELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelELayout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addGroup(PanelELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(PanelELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addGroup(PanelELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                        .addComponent(NoCuentaE)
                        .addComponent(MontoE)
                        .addComponent(TipoE, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelELayout.setVerticalGroup(
            PanelELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelELayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(PanelELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(NoCuentaE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PanelELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(TipoE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PanelELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(MontoE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(jButton3)
                .addGap(18, 18, 18)
                .addComponent(jButton4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ComboTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Movimiento en Efectio", "Movimiento con Cheque" }));
        ComboTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboTipoActionPerformed(evt);
            }
        });

        jLabel9.setText("Tipo de Moviento");

        javax.swing.GroupLayout VacioLayout = new javax.swing.GroupLayout(Vacio);
        Vacio.setLayout(VacioLayout);
        VacioLayout.setHorizontalGroup(
            VacioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        VacioLayout.setVerticalGroup(
            VacioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 380, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jLabel9)
                        .addGap(45, 45, 45)
                        .addComponent(ComboTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PanelE, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Vacio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(PanelC, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jLabel10)))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ComboTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Vacio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 479, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(75, 75, 75)
                .addComponent(PanelC, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PanelE, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        //new Cuenta(conexion,cuenta).setVisible(true);
        papa.setEnabled(true);
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
        NoCuenta.setText("");
        NoCuentaC.setText("");
        NoCheque.setText("");
        Monto.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void ComboTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboTipoActionPerformed
        // TODO add your handling code here:
        if(ComboTipo.getSelectedIndex()==0){
            //PanelC.setVisible(false);
            //PanelE.setVisible(true);
            Vacio.removeAll();
            PanelE.setLocation(0, 0);
            PanelE.setSize(500, 379);
            Vacio.add(PanelE);
            Vacio.revalidate();
            Vacio.repaint();
        }
        else if (ComboTipo.getSelectedIndex()==1){
            //PanelE.setVisible(false);
            //PanelC.setVisible(true);
            Vacio.removeAll();
            PanelC.setLocation(0, 0);
            PanelC.setSize(500, 379);
            Vacio.add(PanelC);
            Vacio.revalidate();
            Vacio.repaint();
        }
    }//GEN-LAST:event_ComboTipoActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if(TipoE.getSelectedIndex()==0){        //Es Deposito
            DepositoE();
        }
        else if(TipoE.getSelectedIndex()==1){   //Es Retiro
            RetiroE();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        NoCuentaE.setText("");
        MontoE.setText("");
    }//GEN-LAST:event_jButton4ActionPerformed

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
    private javax.swing.JComboBox<String> ComboTipo;
    private javax.swing.JTextField Monto;
    private javax.swing.JTextField MontoE;
    private javax.swing.JTextField NoCheque;
    private javax.swing.JTextField NoCuenta;
    private javax.swing.JTextField NoCuentaC;
    private javax.swing.JTextField NoCuentaE;
    private javax.swing.JPanel PanelC;
    private javax.swing.JPanel PanelE;
    private javax.swing.JComboBox<String> Tipo;
    private javax.swing.JComboBox<String> TipoE;
    private javax.swing.JPanel Vacio;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    // End of variables declaration//GEN-END:variables
}
