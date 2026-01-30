package com.niko.apps.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public DuplicateException(String message) {
		super(message);
	}
}
