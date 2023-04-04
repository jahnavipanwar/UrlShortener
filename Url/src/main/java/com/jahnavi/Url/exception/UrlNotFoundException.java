package com.jahnavi.Url.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UrlNotFoundException extends RuntimeException {

	String message;

	public UrlNotFoundException(String message) {
		super(message);
	}

}
