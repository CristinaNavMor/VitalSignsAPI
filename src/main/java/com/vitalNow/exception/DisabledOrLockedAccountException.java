package com.vitalNow.exception;

public class DisabledOrLockedAccountException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DisabledOrLockedAccountException() {
		// TODO Auto-generated constructor stub
	}

	public DisabledOrLockedAccountException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public DisabledOrLockedAccountException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public DisabledOrLockedAccountException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public DisabledOrLockedAccountException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
