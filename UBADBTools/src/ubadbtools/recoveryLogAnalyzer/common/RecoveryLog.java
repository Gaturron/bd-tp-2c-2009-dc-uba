package ubadbtools.recoveryLogAnalyzer.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import ubadbtools.recoveryLogAnalyzer.logRecords.CheckPointEndLogRecord;
import ubadbtools.recoveryLogAnalyzer.logRecords.CheckPointStartLogRecord;
import ubadbtools.recoveryLogAnalyzer.logRecords.CommitLogRecord;
import ubadbtools.recoveryLogAnalyzer.logRecords.RecoveryLogRecord;
import ubadbtools.recoveryLogAnalyzer.logRecords.StartLogRecord;
import ubadbtools.recoveryLogAnalyzer.logRecords.UpdateLogRecord;
import ubadbtools.recoveryLogAnalyzer.results.RecoveryResult;
import ubadbtools.recoveryLogAnalyzer.results.ValidationResult;

public class RecoveryLog
{
	//[start] Atributos
	private List<RecoveryLogRecord> logRecords;
	private Set<String> items;	//Uso sets para evitar repetidos
	private Set<String> transactions;
	//[end]
	
	//[start] Constructor
	public RecoveryLog()
	{
		logRecords 	= new ArrayList<RecoveryLogRecord>();
		items 		= new LinkedHashSet<String>();	//Uso esta clase porque me provee un orden de iteración predecible (no como HashSet)
		transactions= new LinkedHashSet<String>();
	}
	//[end]

	//[start] Getters
	public List<RecoveryLogRecord> getLogRecords()
	{
		return logRecords;
	}
	
	public Set<String> getItems()
	{
		return items;
	}

	public Set<String> getTransactions()
	{
		return transactions;
	}
	//[end]
	
	//[start] Add
	public void addLogRecord(RecoveryLogRecord logRecord)
	{
		logRecords.add(logRecord);
	}
	
	public void addItem(String item)
	{
		items.add(item);
	}

	public void addTransaction(String transaction)
	{
		transactions.add(transaction);
	}
	//[end]

	//[start] replaceLogRecord
	public void replaceLogRecord(int position, RecoveryLogRecord logRecord)
	{
		logRecords.set(position, logRecord);
	}
	//[end]
	
	//[start] Validate
	public ValidationResult validate()
	{
		//TODO: Completar
		return null;
	}
	//[end]
	
	//[start] RecoverFromCrash
	public RecoveryResult recoverFromCrash()
	{
		//TODO: ESTO LO HIZO EL GOOONZA
		
		RecoveryLogRecord currentElem = null;
		String currentTransaction = null;
		HashSet<String> transactionsCommited = new HashSet<String>();
		boolean checkPoinEndFound = false;
		for (Iterator<RecoveryLogRecord> it = logRecords.iterator(); it.hasNext(); currentElem = it.next())
		{
				currentTransaction = currentElem.getTransaction();
				
				// COMMIT RECORD?
				if (currentElem instanceof CommitLogRecord)
				{
					transactionsCommited.add( currentTransaction );
				
				} else if (currentElem instanceof StartLogRecord && 
							transactionsCommited.contains(currentTransaction))
				{
					/*
					 * Este caso contempla el hecho que una transaccion T1 termine habiendo hecho commit y
					 * posteriormente se cree otra con el mismo nombre y que no haga commit.  
					 */
					transactionsCommited.remove(currentTransaction);
					
				// UPDATE RECORD?
				} else if (currentElem instanceof UpdateLogRecord &&
							!transactionsCommited.contains(currentTransaction))
				{
					/*
					 * La transaccion no fue comiteada, de modo que hay que revertir el valor del item
					 */
					UpdateLogRecord item = (UpdateLogRecord) currentElem;
					System.out.println("Restaurar el item " + item.getItem() + " al valor " + item.getOldValue());
					System.out.println("Agregar un Abort al log para la transaccion " + currentTransaction);
				
				// USEFULL CHECKPOINT START RECORD?
				} else if (currentElem instanceof CheckPointStartLogRecord && checkPoinEndFound)
				{
					/*
					 * Como previo al CheckPointStartLogRecord encontre un CheckPointEndLogRecord, entonces
					 * en este punto del log se que todas las transacciones activas terminaron o fueron
					 * abortadas y por ende no es necesario seguir revisando el log
					 */
					checkPoinEndFound = false;		//no es necesario, es solo para mantener consistencia
					break;

				// USELESS CHECKPOINT START RECORD?
				} else if (currentElem instanceof CheckPointStartLogRecord && !checkPoinEndFound)
				{
					/*
					 * Estoy en un CheckPointStartLogRecord pero no tiene su correspondiente 
					 * CheckPointEndLogRecord, de modo que no me sirve de nada este item, 
					 * tengo que seguir viendo el log
					 */
					continue;

				// CHECKPOINT END RECORD?					
				} else if (currentElem instanceof CheckPointEndLogRecord)
				{
					/*
					 * Encontre un CheckPointEnd, de modo que si al primer CheckPointStart que encuentre,
					 * estoy en condiciones de para y dejar de recorrer el log
					 */
					checkPoinEndFound = true;
				} else
				{
					System.out.println("ERROR: Item no reconocido : " + currentElem.toString());
					continue;
				}
		}
		
		return null;
		
	}
	//[end]
}
