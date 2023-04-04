package com.jahnavi.Url.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UrlTimeoutException extends RuntimeException {

	String message;

	public UrlTimeoutException(String message) {
		super(message);
	}

}
