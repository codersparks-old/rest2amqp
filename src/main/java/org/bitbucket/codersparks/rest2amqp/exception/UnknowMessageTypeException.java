package org.bitbucket.codersparks.rest2amqp.exception;

public class UnknowMessageTypeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4562396880716760824L;

	public UnknowMessageTypeException() {
		super();
	}

	public UnknowMessageTypeException(String message) {
		super(message);
		
	}

	public UnknowMessageTypeException(Throwable cause) {
		super(cause);
	}

	public UnknowMessageTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnknowMessageTypeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
