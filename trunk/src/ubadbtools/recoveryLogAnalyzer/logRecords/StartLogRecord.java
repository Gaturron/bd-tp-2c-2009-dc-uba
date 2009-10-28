package ubadbtools.recoveryLogAnalyzer.logRecords;


public class StartLogRecord extends RecoveryLogRecord
{
	public StartLogRecord(String transaction) 
	{
		this.transaction = transaction;
	}
	
	public String toString()
	{
		return "<START " + transaction + ">"; 
	}
}
