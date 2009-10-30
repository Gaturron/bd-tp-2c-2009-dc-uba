package ubadbtools.recoveryLogAnalyzer.logRecords;


public class UpdateLogRecord extends RecoveryLogRecord
{
	private String 	item;
	private String	oldValue;
	
	public UpdateLogRecord(String transaction, String item, String oldValue)
	{
		this.transaction = transaction;
		this.item = item;
		this.oldValue = oldValue;
	}
	
	public String getItem()
	{
		return item;
	}
	public String getOldValue()
	{
		return oldValue;
	}
	
	public String toString()
	{
		return "<UPDATE " + transaction + ", ITEM=" + item + ", OLD VALUE=" + oldValue+ ">"; 
	}
}
