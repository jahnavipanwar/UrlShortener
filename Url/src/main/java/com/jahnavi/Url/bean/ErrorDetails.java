package com.jahnavi.Url.bean;

public class ErrorDetails {

	private String errorDetails;
	private String message;

	public ErrorDetails() {
		// TODO Auto-generated constructor stub
	}

	public ErrorDetails(String errorDetails, String message) {
		super();
		this.errorDetails = errorDetails;
		this.message = message;
	}

	public String getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ErrorDetails [errorDetails=" + errorDetails + ", message=" + message + "]";
	}
}