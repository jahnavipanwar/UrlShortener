package com.jahnavi.Url.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ArgumentNotValidException extends RuntimeException {

	String message;

	public ArgumentNotValidException(String message) {
		super(message);
	}

}
