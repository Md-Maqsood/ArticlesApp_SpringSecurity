package com.talentica.articles.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GolbalExceptionHandler extends ResponseEntityExceptionHandler{
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, Object> details = new HashMap<String, Object>();
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map((error) -> {
			return String.format("%s: %s", error.getField(), error.getDefaultMessage());
		}).collect(Collectors.toList());
		details.put("errors", errors);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(status.value(),
																ex.getMessage(),
																details));
	}

	@ExceptionHandler(ArticleNotFoundException.class)
	public ResponseEntity<Object> handleArticleNotFoundException(ArticleNotFoundException ex, WebRequest request){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404,
																ex.getMessage(),
																new HashMap<String, Object>()));
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400,
																ex.getMessage(),
																new HashMap<String, Object>()));
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<Object> handleAuthenTicationException(AuthenticationException ex, WebRequest request){
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401,
																ex.getMessage(),
																new HashMap<String, Object>()));
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> handleAccessDEniedException(AccessDeniedException ex, WebRequest request){
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(403,
																ex.getMessage(),
																new HashMap<String, Object>()));
	}
	
}
