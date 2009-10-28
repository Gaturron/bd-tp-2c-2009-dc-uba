/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CommitLogRecordDialog.java
 *
 * Created on 16/09/2009, 16:57:06
 */

package ubadbtools.recoveryLogAnalyzer.gui.forms;

import java.awt.Frame;
import java.util.Set;

import ubadbtools.recoveryLogAnalyzer.logRecords.RecoveryLogRecord;
import ubadbtools.recoveryLogAnalyzer.logRecords.UpdateLogRecord;
import ubadbtools.util.guiHelper.GUIHelper;

/**
 *
 * @author ces
 */
public class UpdateLogRecordDialog extends javax.swing.JDialog {
	//[start] Atributos
	private RecoveryLogRecord logRecord = null;
	//[end]
	
	//[start] Constructor
    public UpdateLogRecordDialog(java.awt.Frame parent, boolean modal, Set<String> transactions, Set<String> items) {
        super(parent, modal);
        initComponents();
        
        //Lleno las transacciones
        for(String transaction : transactions)
        	cbTransactions.addItem(transaction);

        //Lleno los ítems
        for(String item : items)
        	cbItems.addItem(item);
    }
    //[end]

    //[start] showDialog
    public static RecoveryLogRecord showDialog(Frame parent, Set<String> transactions, Set<String> items)
    {
    	UpdateLogRecordDialog dialog = new UpdateLogRecordDialog(parent, true, transactions,items);
        dialog.setVisible(true);
        
        return dialog.logRecord;
    }
    //[end]
    
    //[start] InitComponents (AUTO-GENERATED)
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btCancelar = new javax.swing.JButton();
        btAceptar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cbTransactions = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        cbItems = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        txtOldValue = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Update");

        btCancelar.setText("Cancelar");
        btCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btCancelarMouseClicked(evt);
            }
        });

        btAceptar.setText("Aceptar");
        btAceptar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btAceptarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(btAceptar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btCancelar)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btCancelar)
                    .addComponent(btAceptar))
                .addGap(36, 36, 36))
        );

        jLabel1.setText("Transacción");

        jLabel2.setText("Ítem");

        jLabel3.setText("Valor Anterior");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(23, 23, 23)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbTransactions, 0, 106, Short.MAX_VALUE)
                    .addComponent(cbItems, 0, 106, Short.MAX_VALUE)
                    .addComponent(txtOldValue))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbTransactions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbItems, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtOldValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
  //[end]

    //[start] Eventos
    private void btCancelarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btCancelarMouseClicked
       this.setVisible(false);
    }//GEN-LAST:event_btCancelarMouseClicked

    private void btAceptarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btAceptarMouseClicked
    	if(	cbTransactions.getSelectedItem()==null	||
    		cbItems.getSelectedItem()==null || 
    		txtOldValue.getText().trim().equals(""))
    	{
        	GUIHelper.showWarningMessage(this, "Falta ingresar algún dato");
    	}
    	else
    	{
	        logRecord = new UpdateLogRecord((String)cbTransactions.getSelectedItem(),(String)cbItems.getSelectedItem(),txtOldValue.getText());
	        this.setVisible(false);
    	}
    }//GEN-LAST:event_btAceptarMouseClicked
    //[end]

    //[start] Variables Form (AUTO-GENERATED)
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAceptar;
    private javax.swing.JButton btCancelar;
    private javax.swing.JComboBox cbItems;
    private javax.swing.JComboBox cbTransactions;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtOldValue;
    // End of variables declaration//GEN-END:variables
    //[end]

}
