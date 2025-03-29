package com.vitalNow.exception;

public class ElementNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ElementNotFoundException() {
		super();
	}
	
	public ElementNotFoundException(Integer id) {
		super("No element could be found with the identifier "+id);
	}

	public ElementNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ElementNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ElementNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ElementNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
