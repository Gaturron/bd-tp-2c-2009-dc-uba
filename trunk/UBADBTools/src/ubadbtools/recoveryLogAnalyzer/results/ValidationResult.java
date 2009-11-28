package ubadbtools.recoveryLogAnalyzer.results;

import java.util.ArrayList;
import java.util.Collection;

import ubadbtools.recoveryLogAnalyzer.common.ValidationLogRecord;

public class ValidationResult
{
	private Collection<ValidationLogRecord> validationLogRecords = new ArrayList<ValidationLogRecord>();

	public Collection<ValidationLogRecord> getValidationLogRecords() {
		return validationLogRecords;
	}

	public void setValidationLogRecords(
			Collection<ValidationLogRecord> validationLogRecords) {
		this.validationLogRecords = validationLogRecords;
	}
	
	public void add(ValidationLogRecord validationLogRecord){
		this.getValidationLogRecords().add(validationLogRecord);
		
	}
}
