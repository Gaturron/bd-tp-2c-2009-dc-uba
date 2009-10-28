package ubadbtools.util.guiHelper;

import java.awt.Component;

import javax.swing.JOptionPane;

public class GUIHelper
{
	//[start] showInputDialog
	/**
	 * Muestra un mensaje con un campo para que el usuario ingrese texto
	 */
	public static String showInputDialog(Component parent, String message)
	{
		String ret;
		
		// Si no se agrego ningun texto, vuelvo a preguntar
		do
		{
			 ret = JOptionPane.showInputDialog(parent, message);
		}
		while(ret != null && ret.trim().equals(""));
		
		return ret; 
	}
	//[end]
	
	//[start] showErrorMessage
	/**
     * Muestra un mensaje de error al usuario
     */
	public static void showErrorMessage(Component parent, String message)
	{
		JOptionPane.showConfirmDialog(
			parent,
			message,
			"Error",
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.ERROR_MESSAGE);
	}
	//[end]

	//[start] showWarningMessage
	/**
	 * Muestra un mensaje de advertencia al usuario
	 */
	public static void showWarningMessage(Component parent, String message)
	{
		JOptionPane.showConfirmDialog(
				parent,
				message,
				"Advertencia",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.WARNING_MESSAGE);
	}
	//[end]
}
