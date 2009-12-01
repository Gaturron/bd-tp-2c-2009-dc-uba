package ubadbtools.recoveryLogAnalyzer.gui.forms;

import java.awt.Frame;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;
import javax.swing.border.BevelBorder;

import ubadbtools.recoveryLogAnalyzer.common.RecoveryLog;
import ubadbtools.recoveryLogAnalyzer.common.ValidationLogRecord;
import ubadbtools.recoveryLogAnalyzer.results.RecoveryResult;
import ubadbtools.recoveryLogAnalyzer.results.ValidationResult;

@SuppressWarnings("serial")
public class AnalyzeLogDialog extends JDialog
{
	private RecoveryResult recoveryResults = null;
	private ValidationResult validationResult=null;
	//[start] Constructor
    public AnalyzeLogDialog(Frame parent, boolean modal, RecoveryLog log)
    {
    	super(parent, modal);
        // Analizo cada aspecto del log
        AnalyzeValidity(log);
        AnalyzeRecoverability(log);
        initComponents();
        


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
    public void AnalyzeValidity(RecoveryLog log)
    {
    	validationResult = log.validate();
    }
    //[end]

    //[start] AnalyzeRecoverability
    public void AnalyzeRecoverability(RecoveryLog log)
    {
    	recoveryResults = log.recoverFromCrash();
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
	
	private void formatLogMesagges(javax.swing.JTextArea logInfo) {
		String textToShow = "";
		boolean showRecoveryInfo = true;

		textToShow += ("\n\n" + "\t\t*** Resultados de validacion del log ***\t\t\n\t==================================================\n");
		for (Iterator iter = validationResult.getValidationLogRecords().iterator(); iter.hasNext();) {
			ValidationLogRecord record = (ValidationLogRecord) iter.next();
			textToShow += (logInfo.getText() + record.getValidationDesc()
					+ " :" + record.isResult() + "\n");
			showRecoveryInfo = showRecoveryInfo && record.isResult();
		}

		showRecoveryInfo(logInfo, textToShow, showRecoveryInfo);
	}
	
	
	private void showRecoveryInfo(javax.swing.JTextArea logInfo,
			String textToShow, boolean showRecoveryInfo) {
		if (showRecoveryInfo) {
			textToShow += ("\n"
					+ "\t        *** Pasos a efectuar segun algoritmo de recovery ***\t\t\n\t=================================================\n");

			for (Iterator<String> it = recoveryResults.getItems().iterator(); it
					.hasNext();) {
				textToShow += (logInfo.getText() + it.next() + "\n");
			}
		} else {
			textToShow += ("El log es invalido, no hay informacion de recuperacion");
		}
		logInfo.setText(textToShow);
	}
	
    private void initComponents() {

        butCerrar = new javax.swing.JButton();
		logInfo = new javax.swing.JTextArea();
		JScrollPane scrollPane = new JScrollPane(logInfo);

		setTitle("Analisis de log");

		this.formatLogMesagges(logInfo);
		logInfo.setEditable(false);
		logInfo.setOpaque(true);
		logInfo.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));

        butCerrar.setText("Cerrar");
        butCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                butCerrarMouseClicked(evt);
            }
        });

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		this.setResizable(false);
		layout.setVerticalGroup(layout.createSequentialGroup()
			.addContainerGap()
			.addComponent(scrollPane, 0, 206, Short.MAX_VALUE)
			.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
			.addComponent(butCerrar, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
			.addContainerGap());
		layout.setHorizontalGroup(layout.createSequentialGroup()
			.addContainerGap()
			.addGroup(layout.createParallelGroup()
			    .addComponent(scrollPane, GroupLayout.Alignment.LEADING, 0, 504, Short.MAX_VALUE)
			    .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
			        .addGap(0, 459, Short.MAX_VALUE)
			        .addComponent(butCerrar, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)))
			.addContainerGap());

		pack();
		this.setSize(536, 415);
    }// </editor-fold>//GEN-END:initComponents
	//[end]
    
    //[start] Eventos
    private void butCerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_butCerrarMouseClicked
        this.setVisible(false);
    }//GEN-LAST:event_butCerrarMouseClicked
	//[end]
	
	//[start] Variables Form (AUTO-GENERATED)
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea logInfo;
    private javax.swing.JButton butCerrar;
    // End of variables declaration//GEN-END:variables
    //[end]
}
