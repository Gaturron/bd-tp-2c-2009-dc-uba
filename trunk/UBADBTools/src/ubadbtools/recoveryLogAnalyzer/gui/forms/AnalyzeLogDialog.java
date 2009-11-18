package ubadbtools.recoveryLogAnalyzer.gui.forms;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import ubadbtools.recoveryLogAnalyzer.common.RecoveryLog;
import ubadbtools.recoveryLogAnalyzer.common.ValidationLogRecord;
import ubadbtools.recoveryLogAnalyzer.logRecords.CheckPointStartLogRecord;
import ubadbtools.recoveryLogAnalyzer.logRecords.RecoveryLogRecord;
import ubadbtools.recoveryLogAnalyzer.results.RecoveryResult;

@SuppressWarnings("serial")
public class AnalyzeLogDialog extends JDialog {
	// estructuras comunes a los metodos de validacion
	private Set<String> transaccionesActivas = new HashSet<String>();
	private Set<String> transaccionesComiteadas = new HashSet<String>();
	private Set<String> transaccionesStarteadas = new HashSet<String>();
	private boolean lastValueForUpdatesOutOfBounds = true;
	private Collection<ValidationLogRecord> validationLogRecords = new ArrayList<ValidationLogRecord>();

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

		for (Iterator<RecoveryLogRecord> it = logRecords.iterator(); it
				.hasNext();) {

			RecoveryLogRecord item = it.next();
			if (item
					.getClass()
					.equals(
							ubadbtools.recoveryLogAnalyzer.logRecords.StartLogRecord.class)) {
				transaccionesActivas.add(item.getTransaction());
				transaccionesStarteadas.add(item.getTransaction());
			}

			resCheckUpdateBetweenStartAndCommit = checkUpdateBetweenStartAndCommit(item);
			resCheckAllTransactionsOnCKPTAreActive = checkAllTransactionsOnCKPTAreActive(item);
			resCheckAllTransactionCommitBeforAndCKPT = checkAllTransactionCommitBeforAndCKPT(item);

			if (item
					.getClass()
					.equals(
							ubadbtools.recoveryLogAnalyzer.logRecords.CommitLogRecord.class)) {
				transaccionesComiteadas.add(item.getTransaction());
				if (transaccionesActivas.contains(item.getTransaction())) {
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

	}

	private boolean checkUpdateBetweenStartAndCommit(RecoveryLogRecord item) {
		if (item
				.getClass()
				.equals(
						ubadbtools.recoveryLogAnalyzer.logRecords.UpdateLogRecord.class)) {
			lastValueForUpdatesOutOfBounds = transaccionesActivas.contains(item.getTransaction()) && lastValueForUpdatesOutOfBounds;
			return lastValueForUpdatesOutOfBounds;
		}
		return lastValueForUpdatesOutOfBounds;

	}

	private boolean checkAllStartAndCommitClosures(RecoveryLogRecord item) {
		return (transaccionesStarteadas.containsAll(transaccionesComiteadas));
	}

	private boolean checkAllTransactionsOnCKPTAreActive(RecoveryLogRecord item) {
		if (item
				.getClass()
				.equals(
						ubadbtools.recoveryLogAnalyzer.logRecords.CheckPointStartLogRecord.class)) {
			return ((CheckPointStartLogRecord) item).getTransactions().equals(
					transaccionesActivas);

		}
		return true;
	}

	private boolean checkAllTransactionCommitBeforAndCKPT(RecoveryLogRecord item) {
		if (item
				.getClass()
				.equals(
						ubadbtools.recoveryLogAnalyzer.logRecords.CheckPointEndLogRecord.class)) {
			return transaccionesActivas.isEmpty();

		}
		return true;
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

		textToShow += ("\n" + "\t\tResultados de validacion del log\t\t\n");
		for (Iterator iter = validationLogRecords.iterator(); iter.hasNext();) {
			ValidationLogRecord record = (ValidationLogRecord) iter.next();
			textToShow += (logInfo.getText() + record.getValidationDesc()
					+ " :" + record.isResult() + "\n");

		}

		textToShow += ("\n"
				+ "\t\tPasos a efectuar segun algoritmo de recovery\t\t\n");

		for (Iterator<String> it = recoveryResults.getItems().iterator(); it
				.hasNext();) {
			textToShow += (logInfo.getText() + it.next() + "\n");
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

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addComponent(logInfo)
						.addContainerGap(333, Short.MAX_VALUE).addComponent(
								butCerrar).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addComponent(logInfo)
						.addContainerGap(442, Short.MAX_VALUE).addComponent(
								butCerrar).addContainerGap()));

		pack();
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
