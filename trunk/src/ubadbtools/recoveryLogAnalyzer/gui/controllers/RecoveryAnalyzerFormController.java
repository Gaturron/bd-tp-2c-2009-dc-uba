package ubadbtools.recoveryLogAnalyzer.gui.controllers;


import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import ubadbtools.recoveryLogAnalyzer.common.RecoveryLog;
import ubadbtools.recoveryLogAnalyzer.exceptions.GUIException;
import ubadbtools.recoveryLogAnalyzer.gui.etc.LogTableModel;
import ubadbtools.recoveryLogAnalyzer.gui.forms.AnalyzeLogDialog;
import ubadbtools.recoveryLogAnalyzer.gui.forms.EditLogRecordDialog;
import ubadbtools.recoveryLogAnalyzer.gui.forms.RecoveryAnalyzerForm;
import ubadbtools.recoveryLogAnalyzer.logRecords.RecoveryLogRecord;
import ubadbtools.util.guiHelper.GUIHelper;

public class RecoveryAnalyzerFormController
{
	//[start] Atributos
	private RecoveryAnalyzerForm form = null;
	private JTable formTable = null;
	private LogTableModel formTableModel = null;
	private RecoveryLog log = null;
	//[end]
	
	//[start] Constructor
	private RecoveryAnalyzerFormController(RecoveryAnalyzerForm form)
	{
		// Guardo e inicializo las variables del control 
		this.form = form;
		this.formTable = form.getTableLog(); 
		this.formTableModel =  ((LogTableModel)formTable.getModel());
		this.log = new RecoveryLog(); 
	}
	//[end]
	
	//[start] CreateController
	//SI
	/**
	 * Devuelve una instancia especifica de controlador que corresponda al tipo de historia dada
	 * @param form
	 * @param scheduleType
	 * @return
	 */
	public static RecoveryAnalyzerFormController CreateController(RecoveryAnalyzerForm form)
	{
		// Creo un controlador y lo inicializo
		RecoveryAnalyzerFormController ret = new RecoveryAnalyzerFormController(form);
		ret.InitializeForm();
		
		return ret;
	}

	public static RecoveryAnalyzerFormController CreateController(RecoveryAnalyzerForm form, RecoveryLog recoveryLog)
	{
		// Creo un controlador y lo inicializo
		RecoveryAnalyzerFormController ret = new RecoveryAnalyzerFormController(form);
		
		// Al nuevo controlador le cargo el log
		ret.LoadLog(recoveryLog);
		
		return ret;
	}
	
	//[start] Privadas
	private void InitializeForm()
	{
		// Limpio la tabla
		ClearTableLog();
		
		//Limpio los ítems
		DefaultListModel listModel = (DefaultListModel)form.getItemsList().getModel();
		listModel.clear();
		
		//Limpio las transacciones
		listModel = (DefaultListModel)form.getTransactionsList().getModel();
		listModel.clear();
		
        // Agrego la columna de numero de registro
       formTableModel.addColumn("#Registro");
       formTableModel.addColumn("");
       
       // Habilito los controles
       form.EnableUserControls();
	}
	
	private void ClearTableLog()
	{
		// Borro todas las filas de la tabla
		formTableModel.getDataVector().removeAllElements();
	
		// Borro todas las columnas de la tabla
		TableColumnModel columnModel = formTable.getColumnModel();
		while(formTable.getColumnCount() > 0)
		{
			formTable.removeColumn(columnModel.getColumn(0));
		}
		//Además de remover las columnas, hay que llamar a este método (aparentemente, bug de Java: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4283734)
		formTableModel.setDataVector( new Vector<Object>(), new Vector<Object>());
	}
	
	private void LoadLog(RecoveryLog log)
	{
		// Piso el log actual
		this.log = log; 
		
		// Inicializo el formulario
		this.InitializeForm();
		
		// Muestro la historia en la tabla
		ShowLogInForm(log);
	}
	
	private void ShowLogInForm(RecoveryLog log)
	{
		// Agrego los ítems
		for(String item : log.getItems())
		{
			this.AddItemToForm(item);
		}
		
		// Agrego las transacciones
		for(String transaction : log.getTransactions())
		{
			this.AddTransactionToForm(transaction);
		}
		
		// Agrego los registros
		for(RecoveryLogRecord logRecord : log.getLogRecords())
		{
			this.AddLogRecordToForm(logRecord);
		}
	}
	//[end]
	//[end]
	
	//[start] AnalyzeLog
	public void AnalyzeLog()
	{
		// Lanzo el dialogo en donde se analizara el log
		AnalyzeLogDialog.showDialog(form, log);
	}
	//[end]
	
	//[start] AddNewTransaction
	public void AddNewTransaction()
	{
		//Le pido al usuario un nombre
		String newTxName = GUIHelper.showInputDialog(form,"Nombre de la nueva transacción:");
		
		// Si el usuario puso algo, agrego la nueva transaccion
		if(newTxName != null && !newTxName.trim().equals(""))
		{
			//Se la agrego al log
			log.addTransaction(newTxName);
			
			//Se la agrego al form
			AddTransactionToForm(newTxName);
		}
	}
	//[end]
	
	//[start] AddNewItem
	public void AddNewItem()
	{
		// Invoco al metodo concreto para agregar al nuevo item
		String newItem = GUIHelper.showInputDialog(form,"Nombre del nuevo item:");
		
		// Si se agrego un item, lo agrego a la lista de items en el formulario
		if(newItem != null && !newItem.trim().equals(""))
		{
			//Se lo agrego al log
			log.addItem(newItem);
			
			//Se lo agrego al form
			AddItemToForm(newItem);
		}
	}
	//[end]

	//[start] AddNewLogRecord
	public void AddNewLogRecord()
	{
		// Lanzo el dialogo para la edicion del registro de log
		RecoveryLogRecord logRecord = EditLogRecordDialog.showDialog(form, log.getTransactions(), log.getItems());
		
		// Si se eligió un registro de log...
    	if(logRecord != null)
    	{
    		//Se lo agrego al log
    		log.addLogRecord(logRecord);
    		
    		//Se lo agrego al form
    		AddLogRecordToForm(logRecord);
    	}
	}
	//[end]
	
	//[start] GetLog
	public RecoveryLog GetLog()
	{
		return log;
	}
	//[end]
	
	//[start] EditLogRecord
	public void EditLogRecord(int recordNumber) throws GUIException
    {
    	// Lanzo el editor de registros
    	RecoveryLogRecord logRecord = EditLogRecordDialog.showDialog(form, log.getTransactions(), log.getItems());
    	
		// Si no hay registro es porque se cancelo el dialogo
		if(logRecord != null)
		{
			// Agrego la accion a la historia
			log.replaceLogRecord(recordNumber, logRecord);
			
			// Modifico la tabla con el nuevo valor 
			formTableModel.setValueAt(logRecord.toString(), 
				recordNumber,
				1);	//La columna 1 es donde está el contenido del registro
		}
	}
	//[end]
	
	//[start] Privadas comunes
	private void AddItemToForm(String item)
	{
		//Se lo agrego a la lista en el form
		DefaultListModel listModel = (DefaultListModel)form.getItemsList().getModel();
		
		if(!listModel.contains(item))
			listModel.add(listModel.getSize(), item);
	}
	
	private void AddTransactionToForm(String transaction)
	{
		//Se lo agrego a la lista en el form
		DefaultListModel listModel = (DefaultListModel)form.getTransactionsList().getModel();
		
		if(!listModel.contains(transaction))
			listModel.add(listModel.getSize(), transaction);
	}
	
	private void AddLogRecordToForm(RecoveryLogRecord logRecord)
	{
		// Averiguo el numero del nuevo registro
    	int recordNumber = formTableModel.getRowCount() + 1;

		// Agrego la fila a la tabla con el numero de paso en el primer campo
    	Object[] newRow = new Object[formTableModel.getColumnCount() + 1];	//No sé por qué pero necesita el +1
    	newRow[0] =  Integer.toString(recordNumber);
    	newRow[1] = logRecord.toString();	//La columna 1 tiene el contenido del registro
    	formTableModel.addRow(newRow);
	}
	//[end]

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
