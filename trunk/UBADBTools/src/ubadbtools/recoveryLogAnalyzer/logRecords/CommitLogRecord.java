package ubadbtools.recoveryLogAnalyzer.logRecords;


public class CommitLogRecord extends RecoveryLogRecord
{
	public CommitLogRecord(String transaction)
	{
		this.transaction = transaction;
	}
	
	public String toString()
	{
		return "<COMMIT " + transaction + ">"; 
	}
}
