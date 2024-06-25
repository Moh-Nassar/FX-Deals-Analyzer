package com.bloomberg.fx_analyzer.exception;

public class DuplicateDealException extends RuntimeException {

	public DuplicateDealException(String message) {
		super(message);
	}

	public DuplicateDealException(String message, Throwable cause) {
		super(message, cause);
	}
}
