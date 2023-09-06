package com.sn.gestionstock.Exception;

public class EntityNotfoundException extends RuntimeException{

	private ErrorCodes errorCodes;
	
	public EntityNotfoundException(String message) {
		super(message);
	}
	
	public EntityNotfoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public EntityNotfoundException(String message, Throwable cause, ErrorCodes errorCodes) {
		super(message, cause);
		this.errorCodes = errorCodes;
	}
	
	public EntityNotfoundException(String message,ErrorCodes  errorCodes) {
		super(message);
		this.errorCodes = errorCodes;
	}
}
