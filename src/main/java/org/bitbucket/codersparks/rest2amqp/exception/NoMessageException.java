package org.bitbucket.codersparks.rest2amqp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoMessageException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6625665209015562735L;

	public NoMessageException() {
	}

	public NoMessageException(String message) {
		super(message);
	}

	public NoMessageException(Throwable cause) {
		super(cause);
	}

	public NoMessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoMessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
