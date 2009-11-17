package ubadbtools.recoveryLogAnalyzer.results;

import java.util.ArrayList;
import java.util.List;

public class RecoveryResult
{
	private List<String> itemsToShow = new ArrayList<String>();
	
	public void addItem(String item)
	{
		itemsToShow.add(item);
	}

	public List<String> getItems()
	{
		return itemsToShow;
	}
}
