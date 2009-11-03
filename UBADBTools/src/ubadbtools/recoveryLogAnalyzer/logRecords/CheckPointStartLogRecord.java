package ubadbtools.recoveryLogAnalyzer.logRecords;

import java.util.Set;


public class CheckPointStartLogRecord extends RecoveryLogRecord
{
	Set<String> transactions;
	
	public CheckPointStartLogRecord(Set<String> transactions) 
	{
		this.transactions = transactions;
	}
	
	public String toString()
	{
		return "<START CKPT " + transactions + ">"; 
	}
}
