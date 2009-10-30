package ubadbtools.recoveryLogAnalyzer;

import ubadbtools.recoveryLogAnalyzer.gui.forms.RecoveryAnalyzerForm;

public class RecoveryLogAnalyzerTool
{
	public static void main(String[] args)
	{
		java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RecoveryAnalyzerForm().setVisible(true);
            }
        });
	}
}
