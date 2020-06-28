package com.dnb.model;

import java.util.List;

public class Errors {

	String timeStamp;
	String className;
	String message;
	List<PreviousLog> previousLogs;
	
	public Errors() {
		
	}

	public Errors(String timeStamp, String className, String message, List<PreviousLog> previousLogs) {
		super();
		this.timeStamp = timeStamp;
		this.className = className;
		this.message = message;
		this.previousLogs = previousLogs;
	}
	
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public List<PreviousLog> getPreviousLogs() {
		return previousLogs;
	}
	public void setPreviousLogs(List<PreviousLog> previousLogs) {
		this.previousLogs = previousLogs;
	}
	
	 @Override
	    public String toString() 
	    { 
	        return "{\n timeStamp:"
	            + timeStamp 
	            + ", \n className:"
	            + className 
	            + ", \n message:"
	            + message  
	            + ", \n previousLogs:[\n"
	            + previousLogs.toString()
	            + "\n]"
	            + "\n}"; 
	    } 
}
