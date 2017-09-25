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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author SERGIO MALDONADO
 */
public class NuevaCuenta extends javax.swing.JFrame {

    /**
     * Creates new form NuevoCliente
     */
    DefaultTableModel modelo;
    Connection conexion;
    int max = 5;
    String numeroC = "";
    String numeroT = "";
    JFrame Padre;
    public NuevaCuenta() {
        initComponents();
    }
    public NuevaCuenta(Connection conex,JFrame pad) {
        initComponents();
        this.setLocationRelativeTo(null);
        conexion = conex;
        modelo = (DefaultTableModel) Clientes.getModel();
        calcularNoCuenta();
        Padre = pad;
    }
    public void calcularNoCuenta(){
        int num;
        ResultSet numero;
        try {
            
            Statement sentencia = conexion.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            numero = sentencia.executeQuery("SELECT COUNT(cuenta.Id) FROM cuenta;" );
            if(numero.next()){
                num = Integer.parseInt(numero.getString(1))+1;
                if(num<10){
                    NoCuenta.setText("No.000" + num);
                    numeroC = "000"+num;
                    numeroT = "000"+num;
                    NoTarjeta.setText("Cod.000" + num);
                }
                else if(num<100){
                    NoCuenta.setText("No.00" + num);
                    NoTarjeta.setText("Cod.00" + num);
                    numeroC = "00"+num;
                    numeroT = "00"+num;
                }
                else if(num<1000){
                    NoCuenta.setText("No.0" + num);
                    NoTarjeta.setText("Cod.0" + num);
                    numeroC = "0"+num;
                    numeroT = "0"+num;
                }
                else{
                    NoCuenta.setText("No." + num);
                    NoTarjeta.setText("Cod." + num);
                    numeroC = ""+num;
                    numeroT = ""+num;
                }
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(NuevaCuenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String TablaCuentaC(){
        String Instruccion = "", Inst = "";;
        ResultSet cuent = null;
        ResultSet client = null;
        String a = "",b = "";
        try {
            Statement sentencia = conexion.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            cuent = sentencia.executeQuery("SELECT cuenta.Id FROM cuenta WHERE cuenta.Numero = '" + numeroC + "';" );
            if(cuent.next()){
                    System.out.println("Si hay un next en cuenta");
                    System.out.println("ID " + cuent.getString(1));
                    a = cuent.getString(1);
            }
            //cuent.next();
            for(int cont = 0; cont < modelo.getRowCount();cont++){
                client = null;
                client = sentencia.executeQuery("SELECT cliente.Id FROM cliente WHERE cliente.DPI = '" + modelo.getValueAt(cont, 2) + "';" );
                if(client.next()){
                    System.out.println("Si hay un next en cliente");
                    System.out.println("ID " + client.getString(1));
                    b = client.getString(1);
                }
                Inst = "INSERT INTO cuentacliente (cuentacliente.Cuenta_Id,cuentacliente.Cliente_Id) VALUES("
                + a + "," + b + ");";
                Instruccion = Instruccion + Inst + "\n";
                System.out.println("Termino el ciclo");
            } 
        } catch (SQLException ex) {
            Logger.getLogger(Cuenta.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Callo el catch de TablaCuentaC");
        }
        System.out.println("Retorno el String");
        return Instruccion;
    }
    public String TablaTarjeta(int Ac){
        String Instruccion = "";
        ResultSet cuent = null;
        try {
            Statement sentencia = conexion.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            cuent = sentencia.executeQuery("SELECT cuenta.Id FROM cuenta WHERE cuenta.Numero = '" + numeroC + "';" );
            cuent.next();
            Instruccion = "INSERT INTO tarjeta (tarjeta.Cuenta_Id,tarjeta.Codigo,tarjeta.PIN,tarjeta.Activo) VALUES (" 
                    + cuent.getString(1) + ",'" + numeroT + "','" + Pin.getText() + "'," + Ac + ");";
        } catch (SQLException ex) {
            Logger.getLogger(Cuenta.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Instruccion;
    }
    public void Vaciar(){
        modelo.setRowCount(0);
        NoCuenta.setText("");
        Saldo.setText("");
        Nombre.setText("");
        Dpi.setText("");
        Direccion.setText("");
        Telefono.setText("");
        //Mostramos el siguiente numero de cuenta
        calcularNoCuenta();
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Nombre = new javax.swing.JTextField();
        Dpi = new javax.swing.JTextField();
        Direccion = new javax.swing.JTextField();
        Telefono = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        NoCuenta = new javax.swing.JTextField();
        Saldo = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Clientes = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        NoTarjeta = new javax.swing.JTextField();
        Pin = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        Activar = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Personales"));

        jLabel1.setText("Nombre Completo");

        jLabel2.setText("DPI");

        jLabel3.setText("Dirección");

        jLabel4.setText("Telefono");

        jButton1.setText("Agregar Cliente");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(Nombre, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                        .addComponent(Dpi)
                        .addComponent(Direccion)
                        .addComponent(Telefono)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(Dpi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(Direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(Telefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(28, 28, 28))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Cuenta"));

        jLabel5.setText("Numero de Cuenta");

        jLabel6.setText("Saldo");

        NoCuenta.setEditable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(NoCuenta, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(Saldo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(NoCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(Saldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Clientes"));

        Clientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Nombre", "DPI", "Dirección", "Telefono"
            }
        ));
        jScrollPane1.setViewportView(Clientes);
        if (Clientes.getColumnModel().getColumnCount() > 0) {
            Clientes.getColumnModel().getColumn(0).setPreferredWidth(25);
        }

        jButton2.setText("Eliminar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(175, 175, 175)
                        .addComponent(jButton2)))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jButton3.setText("Crear Cuenta");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Tarjeta"));

        jLabel7.setText("Numero de Tarjeta");

        jLabel8.setText("PIN");

        NoTarjeta.setEditable(false);

        Pin.setText("1234");

        jLabel9.setText("Estado");

        Activar.setText("Activar");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Pin, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Activar)
                            .addComponent(NoTarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Activar)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(NoTarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Pin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(203, 203, 203)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String Nom = Nombre.getText();
        String dpi = Dpi.getText();
        String Direc = Direccion.getText();
        String Tel = Telefono.getText();
        if((Nom.equals(""))&&(dpi.equals(""))&&(Direc.equals(""))&&(Tel.equals(""))){
            JOptionPane.showMessageDialog(null, "Campos Vacios");
        }
        else if (modelo.getRowCount()==max){
            JOptionPane.showMessageDialog(null, "Se ha llegado al limite de Clientes adjuntos a esta cuenta ");
        }
        else if (modelo.getRowCount()<max){
            modelo.addRow(new Object[]{modelo.getRowCount()+1,Nom,dpi,Direc,Tel});
            Nombre.setText("");
            Dpi.setText("");
            Direccion.setText("");
            Telefono.setText("");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if(Clientes.getSelectedRow()==-1){
            JOptionPane.showMessageDialog(null, "Seleccione una fila");
        }
        else if (Clientes.getSelectedRow()>1){
            JOptionPane.showMessageDialog(null, "Seleccione SOLO una fila");
        }
        else{
            modelo.removeRow(Clientes.getSelectedRow());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        Statement sentencia = null;
        String Instruccion = "", Instruccion1 = "", Instruccion2, Instruccion3 = "";
        String NumeroC = numeroC;
        String Sald = Saldo.getText();
        PreparedStatement pst = null;
        if((NumeroC.equals(""))&&(Sald.equals(""))){
            
        }
        else if(modelo.getRowCount()==0){
            
        }
        else if((modelo.getRowCount()>0)&&(modelo.getRowCount()<=max)){
            //Aca podemos insertar
            //Primero se va a crear la cuenta
            Instruccion = "INSERT INTO cuenta (cuenta.Numero,cuenta.FechaCreacion,cuenta.Activo,cuenta.Saldo) VALUES ('" + NumeroC + "',NOW(),1," + Sald + ");";
            try {
                
                //Iniciamos la transsacion
                pst = conexion.prepareStatement("START TRANSACTION");
                int a = pst.executeUpdate();
                if (a>0){
                    System.out.println("Se logró iniciar la transaccion");
                }
                
                //Primero se va crear la cuenta;
                pst = conexion.prepareStatement(Instruccion);
                a = pst.executeUpdate();
                if (a>0){
                    System.out.println("Se logró el Insert Cuenta");
                }
                
                //Luego Tarjeta si la hay
                if(Activar.isSelected()){
                    Instruccion1 = TablaTarjeta(1);
                }
                else{
                    Instruccion1 = TablaTarjeta(0);
                }
                pst = conexion.prepareStatement(Instruccion1);
                a = pst.executeUpdate();
                if (a>0){
                    System.out.println("Se logró el Tarjeta");
                }
                
                //Luego los clientes;
                for(int cont = 0; cont < modelo.getRowCount();cont++){
                    Instruccion2 = "INSERT INTO cliente (cliente.NombreCompleto,cliente.DPI,cliente.Direccion,cliente.Telefono) VALUES ('"
                    + modelo.getValueAt(cont, 1) + "','" + modelo.getValueAt(cont, 2) + "','" + modelo.getValueAt(cont, 3) + "','" + modelo.getValueAt(cont, 4) + "');";
                    pst = conexion.prepareStatement(Instruccion2);
                    a = pst.executeUpdate();
                    if (a>0){
                        System.out.println("Se logró el Insert Clientes");
                    }
                    System.out.println("Terminó Ciclo TablaClientes");
                }
                //Luego los cuentacliente
                String ac = "", bc = "";
                Statement sentencia2 = conexion.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                ResultSet cuent = sentencia2.executeQuery("SELECT cuenta.Id FROM cuenta WHERE cuenta.Numero = '" + NumeroC + "';" );
                ResultSet client = null;
                if(cuent.next()){
                    System.out.println("Si hay un next en cuenta");
                    System.out.println("ID " + cuent.getString(1));
                    ac = cuent.getString(1);
                }
                //cuent.next();
                for(int cont = 0; cont < modelo.getRowCount();cont++){
                    client = null;
                    client = sentencia2.executeQuery("SELECT cliente.Id FROM cliente WHERE cliente.DPI = '" + modelo.getValueAt(cont, 2) + "';" );
                    if(client.next()){
                        System.out.println("Si hay un next en cliente");
                        System.out.println("ID " + client.getString(1));
                        bc = client.getString(1);
                    }
                    Instruccion3 = "INSERT INTO cuentacliente (cuentacliente.Cuenta_Id,cuentacliente.Cliente_Id) VALUES("
                    + ac + "," + bc + ");";
                    pst = conexion.prepareStatement(Instruccion3);
                    a = pst.executeUpdate();
                    if (a>0){
                        System.out.println("Se logró el Insert cuentacliente");
                    }
                    System.out.println("Terminó Ciclo TablaClientes");
                    System.out.println("Termino el ciclo");
                } 
                
                //Hacemos Commit
                pst = conexion.prepareStatement("COMMIT");
                a = pst.executeUpdate();
                System.out.println("Se logró el commit");
                if(Activar.isSelected()){
                    JOptionPane.showMessageDialog(null, "Se ha creado la cuenta " + NoCuenta.getText() + "\n" +
                                                        "La Tarjeta " + NoTarjeta.getText() + " se ha activado" + "\n"+
                                                        "Pin: " + Pin.getText());
                }
                else{
                    JOptionPane.showMessageDialog(null, "Se ha creado la cuenta " + NoCuenta.getText());
                }
                //conexion.commit();
                Vaciar();
            } catch (SQLException ex) {
                System.err.println("ERROR: " + ex.getMessage());
                try {
                    //Hacemos Rollback
                    pst = conexion.prepareStatement("ROLLBACK;");
                    int b = pst.executeUpdate();
                    System.out.println("Se logró el Rollback");
                    JOptionPane.showMessageDialog(null, "Error Inesperado");
                    //conexion.rollback();
                } catch (SQLException ex1) {
                    //Insertar algo en el archivo
                    System.out.println("No se pudo realizar el Rollback");
                    Logger.getLogger(NuevaCuenta.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(NuevaCuenta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        Padre.setEnabled(true);
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

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
            java.util.logging.Logger.getLogger(NuevaCuenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NuevaCuenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NuevaCuenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NuevaCuenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NuevaCuenta().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox Activar;
    private javax.swing.JTable Clientes;
    private javax.swing.JTextField Direccion;
    private javax.swing.JTextField Dpi;
    private javax.swing.JTextField NoCuenta;
    private javax.swing.JTextField NoTarjeta;
    private javax.swing.JTextField Nombre;
    private javax.swing.JTextField Pin;
    private javax.swing.JTextField Saldo;
    private javax.swing.JTextField Telefono;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
