package com.talentica.articles.exception;

public class InvalidRequestException extends RuntimeException{
	
	public InvalidRequestException(String message) {
		super(message);
	}

}
