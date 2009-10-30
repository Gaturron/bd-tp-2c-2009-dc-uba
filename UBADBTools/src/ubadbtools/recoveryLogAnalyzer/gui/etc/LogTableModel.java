package ubadbtools.recoveryLogAnalyzer.gui.etc;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class LogTableModel extends DefaultTableModel
{
	public LogTableModel()
	{  
	   super();  
	}
	
	public boolean isCellEditable(int row, int col)
	{  
	   return false;  
	}  
}
