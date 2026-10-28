package com.example.employee.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 400 - Request body validation failures
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CustomErrorResponse> handleValidationException(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		Map<String, String> errors = new LinkedHashMap<>();

		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			errors.putIfAbsent(fieldError.getField(),
					fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "Invalid value");
		}

		CustomErrorResponse error = buildError(HttpStatus.BAD_REQUEST, "Please correct the validation errors", request,
				errors);

		return ResponseEntity.badRequest().body(error);
	}

	// 400 - Invalid request parameter or method-level constraint
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<CustomErrorResponse> handleConstraintViolationException(ConstraintViolationException ex,
			HttpServletRequest request) {

		Map<String, String> errors = new LinkedHashMap<>();

		ex.getConstraintViolations().forEach(
				violation -> errors.putIfAbsent(violation.getPropertyPath().toString(), violation.getMessage()));

		CustomErrorResponse error = buildError(HttpStatus.BAD_REQUEST, "One or more parameters are invalid", request,
				errors);

		return ResponseEntity.badRequest().body(error);
	}

	// 404 - Employee or another requested resource does not exist
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<CustomErrorResponse> handleNotFoundException(ResourceNotFoundException ex,
			HttpServletRequest request) {

		CustomErrorResponse error = buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request, Map.of());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	// 409 - Duplicate email or other business-level conflict
	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<CustomErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex,
			HttpServletRequest request) {

		CustomErrorResponse error = buildError(HttpStatus.CONFLICT, ex.getMessage(), request, Map.of());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

	// 400 - Invalid JSON, invalid enum value, or incompatible JSON type
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<CustomErrorResponse> handleUnreadableMessage(HttpMessageNotReadableException ex,
			HttpServletRequest request) {

		CustomErrorResponse error = buildError(HttpStatus.BAD_REQUEST,
				"Request body is invalid or contains an unsupported value", request, Map.of());

		return ResponseEntity.badRequest().body(error);
	}

	// 400 - Invalid request parameter type, such as page=abc
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<CustomErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
			HttpServletRequest request) {

		CustomErrorResponse error = buildError(HttpStatus.BAD_REQUEST,
				"Invalid value supplied for parameter: " + ex.getName(), request, Map.of());

		return ResponseEntity.badRequest().body(error);
	}

	// 400 - A required query parameter is missing
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<CustomErrorResponse> handleMissingParameter(MissingServletRequestParameterException ex,
			HttpServletRequest request) {

		CustomErrorResponse error = buildError(HttpStatus.BAD_REQUEST,
				"Required parameter is missing: " + ex.getParameterName(), request, Map.of());

		return ResponseEntity.badRequest().body(error);
	}

	// 400 - Invalid pagination values or other invalid arguments
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<CustomErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
			HttpServletRequest request) {

		CustomErrorResponse error = buildError(HttpStatus.BAD_REQUEST,
				ex.getMessage() != null ? ex.getMessage() : "The supplied request is invalid", request, Map.of());

		return ResponseEntity.badRequest().body(error);
	}

	// 405 - HTTP method not supported for an endpoint
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<CustomErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpServletRequest request) {

		CustomErrorResponse error = buildError(HttpStatus.METHOD_NOT_ALLOWED,
				"The HTTP method is not supported for this endpoint", request, Map.of());

		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
	}

	// 409 - Database integrity constraint violation
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<CustomErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex,
			HttpServletRequest request) {

		CustomErrorResponse error = buildError(HttpStatus.CONFLICT, "The request conflicts with existing data", request,
				Map.of());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

	// 500 - Any unexpected exception
	@ExceptionHandler(Exception.class)
	public ResponseEntity<CustomErrorResponse> handleUnexpectedException(Exception ex, HttpServletRequest request) {

		// Log the full exception internally, not in the API response.
		// Replace this with your application's logger.
		System.err.println("Unexpected error: " + ex.getMessage());
		ex.printStackTrace();

		CustomErrorResponse error = buildError(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected server error occurred",
				request, Map.of());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	private CustomErrorResponse buildError(HttpStatus status, String message, HttpServletRequest request,
			Map<String, String> validationErrors) {

		return new CustomErrorResponse(Instant.now(), status.value(), status.getReasonPhrase(), message,
				request.getRequestURI(), validationErrors);
	}

}
