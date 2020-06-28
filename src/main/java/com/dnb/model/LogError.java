package com.dnb.model;

import java.util.ArrayList;
import java.util.List;

public class LogError {
	
	public LogError() {

	}

	int errorCount;
	List<Errors> errors = new ArrayList<Errors>();
	public int getErrorCount() {
		return errorCount;
	}
	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}
	public List<Errors> getErrors() {
		return errors;
	}
	public void setErrors(List<Errors> errors) {
		this.errors = errors;
	}
	
	 @Override
	    public String toString() 
	    { 
	        return "errorCount:"
	            + errorCount
	            + ", errors:" + errors.toString()
	             + "]"; 
	    } 
}
