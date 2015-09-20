package org.bitbucket.codersparks.rest2amqp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class QueueNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6255647144192702964L;

	public QueueNotFoundException() {
		super();
	}

	public QueueNotFoundException(String message) {
		super(message);
	}

	public QueueNotFoundException(Throwable cause) {
		super(cause);
	}

	public QueueNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public QueueNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
