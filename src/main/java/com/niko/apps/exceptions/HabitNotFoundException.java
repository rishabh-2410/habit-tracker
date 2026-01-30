package com.niko.apps.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class HabitNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public HabitNotFoundException(String message) {
		super(message);
	}
}
