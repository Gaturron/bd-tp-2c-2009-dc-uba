package ubadbtools.recoveryLogAnalyzer.logRecords;

import java.util.Set;


public class CheckPointStartLogRecord extends RecoveryLogRecord
{
	private Set<String> transactions;
	
	public CheckPointStartLogRecord(Set<String> transactions) 
	{
		this.transactions = transactions;
	}
	
	public Set<String> getTransactions()
	{
		return this.transactions;
	}
	
	public String toString()
	{
		return "<START CKPT " + transactions + ">"; 
	}
}
