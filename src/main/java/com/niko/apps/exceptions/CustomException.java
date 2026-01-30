package com.niko.apps.exceptions;

import java.time.LocalDateTime;



public class CustomException {
	private LocalDateTime timestamp;
	private String message;
	
	public CustomException(LocalDateTime timestamp, String message) {
		super();
		this.timestamp = timestamp;
		this.message = message;
	}
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "Error [timestamp=" + timestamp + ", message=" + message + "]";
	}
	
	
}
