package com.vitalNow.exception;

public class ServiceUnavailableException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceUnavailableException() {
		super("Service not avaiable at the time.");
	}

	public ServiceUnavailableException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ServiceUnavailableException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ServiceUnavailableException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ServiceUnavailableException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
