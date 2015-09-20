package org.bitbucket.codersparks.rest2amqp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class QueueNameException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5677224643287982480L;

	public QueueNameException() {
	}

	public QueueNameException(String message) {
		super(message);
	}

	public QueueNameException(Throwable cause) {
		super(cause);
	}

	public QueueNameException(String message, Throwable cause) {
		super(message, cause);
	}

	public QueueNameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
