package com.dnb.model;

public class PreviousLog {
	
	String timeStamp;
	String className;
	String message;
	
	public PreviousLog() {

	}
	
	public PreviousLog(String timeStamp, String className, String message) {
		super();
		this.timeStamp = timeStamp;
		this.className = className;
		this.message = message;
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
}
