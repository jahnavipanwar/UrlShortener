package com.jahnavi.Url.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UrlLengthException extends RuntimeException{
	String message;

	public UrlLengthException(String message) {
		super(message);
	}

}
