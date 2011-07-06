package gueei.binding.validation;

import java.util.ArrayList;

public class ValidationResult {
	private boolean valid = true;
	private ArrayList<String> errorMessages = new ArrayList<String>(1);
	
	public boolean isValid(){
		return valid;
	}
	
	public void putValidationError(String message){
		valid = false;
		errorMessages.add(message);
	}
	
	public String[] getValidationErrors(){
		return errorMessages.toArray(new String[errorMessages.size()]);
	}
}
