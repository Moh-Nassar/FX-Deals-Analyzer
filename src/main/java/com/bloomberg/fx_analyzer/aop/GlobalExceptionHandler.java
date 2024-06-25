package com.bloomberg.fx_analyzer.aop;

import com.bloomberg.fx_analyzer.exception.DuplicateDealException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DuplicateDealException.class)
	public ResponseEntity<Map<String, Object>> handleDuplicateDealException(DuplicateDealException exception) {
		Map<String, Object> body = new HashMap<>();
		body.put("message", exception.getMessage());
		body.put("timestamp", LocalDateTime.now());

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleArgumentValidationException(MethodArgumentNotValidException exception) {
		Map<String, Object> body = new HashMap<>();
		body.put("message", Objects.requireNonNull(exception.getFieldError()).getDefaultMessage());
		body.put("timestamp", LocalDateTime.now());

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	public static void main(String... args) {
		System.out.println(Currency.getInstance("rtertert"));
	}
}