package ubadbtools.recoveryLogAnalyzer.gui.forms;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.sun.xml.internal.ws.util.StringUtils;

import ubadbtools.recoveryLogAnalyzer.common.RecoveryLog;
import ubadbtools.recoveryLogAnalyzer.common.ValidationLogRecord;
import ubadbtools.recoveryLogAnalyzer.logRecords.CheckPointStartLogRecord;
import ubadbtools.recoveryLogAnalyzer.logRecords.RecoveryLogRecord;

@SuppressWarnings("serial")
public class AnalyzeLogDialog extends JDialog
{
	private Collection<ValidationLogRecord> validationLogRecords = new ArrayList<ValidationLogRecord>(); 
	
	
	//[start] Constructor
    public AnalyzeLogDialog(Frame parent, boolean modal, RecoveryLog log)
    {
    	super(parent, modal);
        AnalyzeValidity(log);
        AnalyzeRecoverability(log);
        initComponents();
        
        // Analizo cada aspecto del log

    }
    //[end]
    
    //[start] showDialog
    public static void showDialog(Frame parent, RecoveryLog log)
    {
    	AnalyzeLogDialog dialog = new AnalyzeLogDialog(parent, true, log);
        dialog.setVisible(true);
    }
    //[end]
    
    //[start] AnalyzeValidity
    public void AnalyzeValidity(RecoveryLog log1)
    {
    	System.out.println("Empieza el analisis");
        //TODO: Completar
    	
    	RecoveryLog log =log1;
    	
    	//Para que un log UNDO sea valido debe cumplir las siguientes reglas:
    	
    	//NOTA: nose si ir chequeando cada regla por separado como estoy haciendo o 
    	//hacer un bloque de codigo que chequee todo junto, hay muchas partes
    	// que se podrian optimizar. Mucho codigo copy pasteado
    	
    	//+Una transacci�n T no puede hacer COMMIT si previamente no hizo un START.
    	
    	List<RecoveryLogRecord> logRecords = log.getLogRecords();
    	
		Set<String> transaccionesActivas = new HashSet<String>();
    	
    	for(Iterator<RecoveryLogRecord> it = logRecords.iterator(); it.hasNext();){
    	
    		RecoveryLogRecord item = it.next();
    		    		
    		if(item.getClass().equals(ubadbtools.recoveryLogAnalyzer.logRecords.StartLogRecord.class)){
    			transaccionesActivas.add(item.getTransaction());
    		}
    		
    		if(item.getClass().equals(ubadbtools.recoveryLogAnalyzer.logRecords.CommitLogRecord.class)){
    		
    			if(transaccionesActivas.contains(item.getTransaction())){
    				transaccionesActivas.remove(item.getTransaction());
    			}
    		}

    		//System.out.println("trans: "+transaccionesActivas);
    	}    	
    	validationLogRecords.add(new ValidationLogRecord("Paso el test1?:", transaccionesActivas.isEmpty()));
    	//System.out.println("Paso el test1?: "+transaccionesActivas.isEmpty());
    	
    	//+Todas las acciones de UPDATE deben estar entre un START y un COMMIT de esa misma Transaccion involucrada
    	
    	transaccionesActivas = new HashSet<String>();
    	boolean res = true;
    	
    	for(Iterator<RecoveryLogRecord> it = logRecords.iterator(); it.hasNext();){
    		
    		RecoveryLogRecord item = it.next();
    	
    		if(item.getClass().equals(ubadbtools.recoveryLogAnalyzer.logRecords.StartLogRecord.class)){
    			transaccionesActivas.add(item.getTransaction());
    		}
    		
    		if(item.getClass().equals(ubadbtools.recoveryLogAnalyzer.logRecords.UpdateLogRecord.class)){
    			
    			if(transaccionesActivas.contains(item.getTransaction())){
    				//todo ok, este update esta entre un start y commit
    			}else{
    				//no esta, falla
    				res = false;
    			}
    			
    		}
    		
    		if(item.getClass().equals(ubadbtools.recoveryLogAnalyzer.logRecords.CommitLogRecord.class)){
    		
    			if(transaccionesActivas.contains(item.getTransaction())){
    				transaccionesActivas.remove(item.getTransaction());
    			}
    		}
    	}    	
    	validationLogRecords.add(new ValidationLogRecord("Paso el test2?:", res));
    	//System.out.println("Paso el test2?: "+res);
    	
    	//+Las transacciones que estan en START CKPT deben ser transacciones activas en ese momento
    	
    	transaccionesActivas = new HashSet<String>();
    	res = true;
    	
    	for(Iterator<RecoveryLogRecord> it = logRecords.iterator(); it.hasNext();){
    		
    		RecoveryLogRecord item = it.next();
    	
    		if(item.getClass().equals(ubadbtools.recoveryLogAnalyzer.logRecords.StartLogRecord.class)){
    			transaccionesActivas.add(item.getTransaction());
    		}
    		
    		if(item.getClass().equals(ubadbtools.recoveryLogAnalyzer.logRecords.CheckPointStartLogRecord.class)){
    			
    			Set<String> trans = ((CheckPointStartLogRecord) item).getTransactions();
    			
    			if(trans.equals(transaccionesActivas))
    			{
    				//todo ok
    			}else{
    				//todo mal falla
    				res = false;
    			}
    			
    		}
    		
    		if(item.getClass().equals(ubadbtools.recoveryLogAnalyzer.logRecords.CommitLogRecord.class)){
    		
    			if(transaccionesActivas.contains(item.getTransaction())){
    				transaccionesActivas.remove(item.getTransaction());
    			}
    		}
    	}    	
    	validationLogRecords.add(new ValidationLogRecord("Paso el test3?:", res));
    	
    	
    	//+Cuando esas transacciones activas hacen COMMIT se puede agregar el END CKPT
    	
    	transaccionesActivas = new HashSet<String>();
    	res = true;
    	
    	for(Iterator<RecoveryLogRecord> it = logRecords.iterator(); it.hasNext();){
    		
    		RecoveryLogRecord item = it.next();
    	
    		if(item.getClass().equals(ubadbtools.recoveryLogAnalyzer.logRecords.StartLogRecord.class)){
    			transaccionesActivas.add(item.getTransaction());
    		}
    		
    		if(item.getClass().equals(ubadbtools.recoveryLogAnalyzer.logRecords.CheckPointEndLogRecord.class)){
    			
    			if(transaccionesActivas.isEmpty())
    			{
    				//todo ok
    			}else{
    				//todo mal falla
    				res = false;
    			}
    			
    		}
    		
    		if(item.getClass().equals(ubadbtools.recoveryLogAnalyzer.logRecords.CommitLogRecord.class)){
    		
    			if(transaccionesActivas.contains(item.getTransaction())){
    				transaccionesActivas.remove(item.getTransaction());
    			}
    		}
    	}
    	validationLogRecords.add(new ValidationLogRecord("Paso el test4?:", res));
    	//System.out.println("Paso el test4?: "+res);
    	
    }
    //[end]

    //[start] AnalyzeRecoverability
    public void AnalyzeRecoverability(RecoveryLog log)
    {
        //TODO: Completar LO HACE GONZAAAA!!!
    }
    //[end]

    //[start] ShowErrorMessage
	/**
     * Muestra un mensaje de error al usuario
     * @param message
     */
	public void ShowErrorMessage(String message)
	{
		JOptionPane.showConfirmDialog(
			this,
			message,
			"Error",
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.ERROR_MESSAGE);
	}
	//[end]
	
	private void formatLogMesagges (javax.swing.JTextArea logInfo){
		for (Iterator iter = validationLogRecords.iterator(); iter.hasNext();) {
			ValidationLogRecord record = (ValidationLogRecord) iter.next();
			logInfo.setText(logInfo.getText() + record.getValidationDesc() + " :" + record.isResult() + "\n");
			
		}
		
		
	}
	
    //[start] InitComponents (AUTO-GENERATED)
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        butCerrar = new javax.swing.JButton();
        logInfo = new javax.swing.JTextArea();

        setTitle("An�lisis de log");  
        
        
        this.formatLogMesagges(logInfo);
        logInfo.setEditable(false);
        logInfo.setOpaque(true);
        butCerrar.setText("Cerrar");
        butCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                butCerrarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)           
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            	.addComponent(logInfo)
                .addContainerGap(333, Short.MAX_VALUE)                
                .addComponent(butCerrar)             
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)            
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(logInfo)
                .addContainerGap(442, Short.MAX_VALUE)                
                .addComponent(butCerrar)              
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
	//[end]
    
    //[start] Eventos
    private void butCerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_butCerrarMouseClicked
        this.setVisible(false);
    }//GEN-LAST:event_butCerrarMouseClicked
	//[end]
	
	//[start] Variables Form (AUTO-GENERATED)
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton butCerrar;
    private javax.swing.JTextArea logInfo;
    // End of variables declaration//GEN-END:variables
    //[end]
}
