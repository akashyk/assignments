package com.dnb.model;

public class TimedError {

	long timestampSeconds;
	String timestamp;
	String className;
	String message;
	
	public TimedError() {
	}
	
	public TimedError(long timestampSeconds, String className, 
			String message, String timestamp) {
		super();
		this.timestampSeconds = timestampSeconds;
		this.className = className;
		this.message = message;
		this.timestamp = timestamp;
	}
	
	public long getTimestampSeconds() {
		return timestampSeconds;
	}
	public void setTimestampSeconds(long timestampSeconds) {
		this.timestampSeconds = timestampSeconds;
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

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
