package ubadbtools.recoveryLogAnalyzer.gui.forms;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import ubadbtools.recoveryLogAnalyzer.common.RecoveryLog;
import ubadbtools.recoveryLogAnalyzer.common.ValidationLogRecord;
import ubadbtools.recoveryLogAnalyzer.logRecords.CheckPointStartLogRecord;
import ubadbtools.recoveryLogAnalyzer.logRecords.RecoveryLogRecord;
import ubadbtools.recoveryLogAnalyzer.results.RecoveryResult;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
@SuppressWarnings("serial")
public class AnalyzeLogDialog extends JDialog {
	// estructuras comunes a los metodos de validacion
	private Set<String> transaccionesActivas = new HashSet<String>();
	private Set<String> transaccionesComiteadas = new HashSet<String>();
	private Set<String> transaccionesStarteadas = new HashSet<String>();
	private Collection<ValidationLogRecord> validationLogRecords = new ArrayList<ValidationLogRecord>();
	private Set<String> activeTXOnCKPT = new HashSet<String>();

	private RecoveryResult recoveryResults = null;

	public AnalyzeLogDialog(Frame parent, boolean modal, RecoveryLog log) {
		super(parent, modal);
		AnalyzeValidity(log);
		AnalyzeRecoverability(log);
		initComponents();

		// Analizo cada aspecto del log

	}

	// [end]

	// [start] showDialog
	public static void showDialog(Frame parent, RecoveryLog log) {
		AnalyzeLogDialog dialog = new AnalyzeLogDialog(parent, true, log);
		dialog.setVisible(true);
	}

	// [end]

	// [start] AnalyzeValidity
	public void AnalyzeValidity(RecoveryLog log1) {

		RecoveryLog log = log1;

		// Para que un log UNDO sea valido debe cumplir las siguientes reglas:

		// NOTA: nose si ir chequeando cada regla por separado como estoy
		// haciendo o
		// hacer un bloque de codigo que chequee todo junto, hay muchas partes
		// que se podrian optimizar. Mucho codigo copy pasteado

		// +Una transacci�n T no puede hacer COMMIT si previamente no hizo un
		// START.

		// Declaramos las estructuras comunes a todos los metodos de validacion
		List<RecoveryLogRecord> logRecords = log.getLogRecords();
		boolean resCheckAllStartAndCommitClosures = true;
		boolean resCheckUpdateBetweenStartAndCommit = true;
		boolean resCheckAllTransactionsOnCKPTAreActive = true;
		boolean resCheckAllTransactionCommitBeforAndCKPT = true;
		boolean resCheckAllTransactionStartedOutOfClosure = true;
		boolean resCheckAllTransactionCommitOutOfClosure = true;
		boolean resCheckAllCKPTClosure = true;
		
		boolean isCKPTOn = false;

		for (Iterator<RecoveryLogRecord> it = logRecords.iterator(); it
				.hasNext();) {

			RecoveryLogRecord item = it.next();
			if (item
					.getClass()
					.equals(
							ubadbtools.recoveryLogAnalyzer.logRecords.StartLogRecord.class)) {
				transaccionesActivas.add(item.getTransaction());
				resCheckAllTransactionStartedOutOfClosure = resCheckAllTransactionStartedOutOfClosure
						&& checkAllTransactionStartedOutOfClosure(item);
				transaccionesStarteadas.add(item.getTransaction());
			}

			if (item
					.getClass()
					.equals(
							ubadbtools.recoveryLogAnalyzer.logRecords.CheckPointEndLogRecord.class)) {
				isCKPTOn = !isCKPTOn;
				resCheckAllCKPTClosure = resCheckAllCKPTClosure && !isCKPTOn;
				resCheckAllTransactionCommitBeforAndCKPT = resCheckAllTransactionCommitBeforAndCKPT
						&& checkAllTransactionCommitBeforAndCKPT(item);
				

			}

			if (item
					.getClass()
					.equals(
							ubadbtools.recoveryLogAnalyzer.logRecords.CheckPointStartLogRecord.class)) {
				isCKPTOn = !isCKPTOn;
				resCheckAllCKPTClosure = resCheckAllCKPTClosure && isCKPTOn;
				resCheckAllTransactionsOnCKPTAreActive = resCheckAllTransactionsOnCKPTAreActive
						&& checkAllTransactionsOnCKPTAreActive(item);
				activeTXOnCKPT.addAll(((CheckPointStartLogRecord)item).getTransactions());

			}

			if (item
					.getClass()
					.equals(
							ubadbtools.recoveryLogAnalyzer.logRecords.UpdateLogRecord.class)) {
				resCheckUpdateBetweenStartAndCommit = resCheckUpdateBetweenStartAndCommit
						&& checkUpdateBetweenStartAndCommit(item);
			}

			if (item
					.getClass()
					.equals(
							ubadbtools.recoveryLogAnalyzer.logRecords.CommitLogRecord.class)) {
				transaccionesComiteadas.add(item.getTransaction());
				resCheckAllTransactionCommitOutOfClosure = resCheckAllTransactionCommitOutOfClosure
						&& checkAllTransactionCommitOutOfClosure(item);
				if (resCheckAllTransactionCommitOutOfClosure) {
					transaccionesActivas.remove(item.getTransaction());
				}

			}
			resCheckAllStartAndCommitClosures = checkAllStartAndCommitClosures(item);
		}
		validationLogRecords
				.add(new ValidationLogRecord(
						"No hay transacciones que hagan Commit sin haber hecho su correspondiente START:",
						resCheckAllStartAndCommitClosures));
		validationLogRecords.add(new ValidationLogRecord(
				"No hay updates que no esten entre un start y un commit:",
				resCheckUpdateBetweenStartAndCommit));
		validationLogRecords.add(new ValidationLogRecord(
				"Los START CKPT contemplan todas las transacciones activas:",
				resCheckAllTransactionsOnCKPTAreActive));
		validationLogRecords.add(new ValidationLogRecord(
				"Los END CKPT estan colocados correctamente:",
				resCheckAllTransactionCommitBeforAndCKPT));
		validationLogRecords.add(new ValidationLogRecord(
				"Las transacciones no se solapan",
				resCheckAllTransactionStartedOutOfClosure
						|| resCheckAllTransactionCommitOutOfClosure));
		validationLogRecords.add(new ValidationLogRecord(
				"Los registros de CKPT no se solapan",
				resCheckAllCKPTClosure));

	}

	private boolean checkUpdateBetweenStartAndCommit(RecoveryLogRecord item) {
		return transaccionesActivas.contains(item.getTransaction());
	}

	private boolean checkAllStartAndCommitClosures(RecoveryLogRecord item) {
		return (transaccionesStarteadas.containsAll(transaccionesComiteadas));

	}

	private boolean checkAllTransactionStartedOutOfClosure(
			RecoveryLogRecord item) {
		return (!transaccionesActivas.contains(item.getTransaction()));
	}

	private boolean checkAllTransactionCommitOutOfClosure(RecoveryLogRecord item) {
		return (transaccionesActivas.contains(item.getTransaction()));
	}

	private boolean checkAllTransactionsOnCKPTAreActive(RecoveryLogRecord item) {
		return ((CheckPointStartLogRecord) item).getTransactions().equals(
				transaccionesActivas);

	}

	private boolean checkAllTransactionCommitBeforAndCKPT(RecoveryLogRecord item) {
		boolean hasAny=false;
		for (Iterator iterator = activeTXOnCKPT.iterator(); iterator.hasNext();) {
			String tx = (String) iterator.next();
		    hasAny = hasAny || transaccionesActivas.contains(tx);	
		}
		return !hasAny;
	}

	// [end]

	// [start] AnalyzeRecoverability
	public void AnalyzeRecoverability(RecoveryLog log) {
		// TODO: Completar LO HACE GONZAAAA!!!
		recoveryResults = log.recoverFromCrash();
	}

	// [end]

	// [start] ShowErrorMessage
	/**
	 * Muestra un mensaje de error al usuario
	 * 
	 * @param message
	 */
	public void ShowErrorMessage(String message) {
		JOptionPane.showConfirmDialog(this, message, "Error",
				JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
	}

	// [end]

	private void formatLogMesagges(javax.swing.JTextArea logInfo) {
		String textToShow = "";
		boolean showRecoveryInfo = true;

		textToShow += ("\n\n" + "\t\t*** Resultados de validacion del log ***\t\t\n\t==================================================\n");
		for (Iterator iter = validationLogRecords.iterator(); iter.hasNext();) {
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

	// [start] InitComponents (AUTO-GENERATED)
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		butCerrar = new javax.swing.JButton();
		logInfo = new javax.swing.JTextArea();

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
			.addComponent(logInfo, 0, 206, Short.MAX_VALUE)
			.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
			.addComponent(butCerrar, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
			.addContainerGap());
		layout.setHorizontalGroup(layout.createSequentialGroup()
			.addContainerGap()
			.addGroup(layout.createParallelGroup()
			    .addComponent(logInfo, GroupLayout.Alignment.LEADING, 0, 504, Short.MAX_VALUE)
			    .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
			        .addGap(0, 459, Short.MAX_VALUE)
			        .addComponent(butCerrar, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)))
			.addContainerGap());

		pack();
		this.setSize(536, 415);
	}// </editor-fold>//GEN-END:initComponents

	// [end]

	// [start] Eventos
	private void butCerrarMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_butCerrarMouseClicked
		this.setVisible(false);
	}// GEN-LAST:event_butCerrarMouseClicked

	// [end]

	// [start] Variables Form (AUTO-GENERATED)
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton butCerrar;
	private javax.swing.JTextArea logInfo;
	// End of variables declaration//GEN-END:variables
	// [end]
}
