package com.niko.apps.controllers.exceptions;



import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.niko.apps.exceptions.CustomException;
import com.niko.apps.exceptions.DuplicateException;
import com.niko.apps.exceptions.HabitNotFoundException;
import com.niko.apps.exceptions.UserNotFoundException;

import jakarta.annotation.Nullable;

@ControllerAdvice
public class ExcepHandler extends ResponseEntityExceptionHandler {
	
	
	// All generic exceptions 500
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<CustomException> handleAllExceptions(Exception ex, WebRequest req) {
		CustomException customError = new CustomException(LocalDateTime.now(), ex.getMessage());
		return new ResponseEntity<CustomException>(customError, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	// Unauthorized 401
	@ExceptionHandler(UserNotFoundException.class)
	public final ResponseEntity<CustomException> handleUserNotFoundException(Exception ex, WebRequest req) {
		CustomException customError = new CustomException(LocalDateTime.now(), ex.getMessage());
		return new ResponseEntity<CustomException>(customError, HttpStatus.UNAUTHORIZED);
	}
	
	// Habit - Not Found 404
	@ExceptionHandler(HabitNotFoundException.class) 
	public final ResponseEntity<CustomException> handleHabitNotFoundException(Exception ex, WebRequest req) {
		CustomException customError = new CustomException(LocalDateTime.now(), ex.getMessage());
		return new ResponseEntity<CustomException>(customError, HttpStatus.NOT_FOUND);
	}
	
	
	// Habit Conflict 409
	@ExceptionHandler(DuplicateException.class)
	public final ResponseEntity<CustomException> handleDuplicateContentException(Exception ex, WebRequest req) {
		CustomException customError =  new CustomException(LocalDateTime.now(), ex.getMessage());
		return new ResponseEntity<CustomException>(customError, HttpStatus.CONFLICT);
	}
	
	// Email Conflict 409
	@ExceptionHandler(DataIntegrityViolationException.class)
	public final ResponseEntity<CustomException> handleDuplicateEmail(Exception ex, WebRequest req) {
		CustomException customError = new CustomException(LocalDateTime.now(), ex.getMessage());
		return new ResponseEntity<CustomException>(customError, HttpStatus.CONFLICT);
	}
	 
	// Bad Request 400
	
//	// Override Spring's internal Validation handler for bad request
	@Override
	protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		 
		 String errorMessage = ex.getBindingResult()
		            .getFieldErrors()
		            .stream()
		            .map(error -> error.getField() + ": " + error.getDefaultMessage())
		            .collect(Collectors.joining(", "));
		
		CustomException invalidArgException = new CustomException(LocalDateTime.now(), errorMessage);
		return new ResponseEntity<Object>(invalidArgException, HttpStatus.BAD_REQUEST);
	}
	
}