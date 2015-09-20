package org.bitbucket.codersparks.rest2amqp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class QueueAlreadyExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1429911520003703887L;

	public QueueAlreadyExistsException() {
		super();
	}

	public QueueAlreadyExistsException(String message) {
		super(message);
	}

	public QueueAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	public QueueAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public QueueAlreadyExistsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
