package ubadbtools.recoveryLogAnalyzer.common;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import ubadbtools.recoveryLogAnalyzer.logRecords.RecoveryLogRecord;
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
		//TODO: Completar
		return null;
	}
	//[end]
}
