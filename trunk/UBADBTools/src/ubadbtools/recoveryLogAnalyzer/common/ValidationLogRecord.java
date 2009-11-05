package ubadbtools.recoveryLogAnalyzer.common;

public class ValidationLogRecord {

	private String validationDesc;

	private boolean result;
	
	public ValidationLogRecord(String validationDesc, boolean result){
		this.setResult(result);
		this.setValidationDesc(validationDesc);
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getValidationDesc() {
		return validationDesc;
	}

	public void setValidationDesc(String validationDesc) {
		this.validationDesc = validationDesc;
	}

}
