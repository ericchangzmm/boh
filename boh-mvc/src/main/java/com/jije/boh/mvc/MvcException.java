package com.jije.boh.mvc;

public class MvcException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MvcException() {
		super();
	}

	public MvcException(String message, Throwable cause) {
		super(message, cause);
	}

	public MvcException(String message) {
		super(message);
	}

	public MvcException(Throwable cause) {
		super(cause);
	}

}
