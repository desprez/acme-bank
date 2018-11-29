package com.acmebank.demo.bank.account.app.exposition.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.acmebank.demo.bank.account.app.domain.exception.AccountNotFoundException;
import com.acmebank.demo.bank.account.app.domain.exception.IllegalAccountStatusException;
import com.acmebank.demo.bank.account.app.domain.exception.InsufficientBalanceException;
import com.acmebank.demo.bank.account.app.domain.exception.InvalidAccountNumberException;
import com.acmebank.demo.bank.account.app.domain.exception.InvalidAccountStatusException;
import com.acmebank.demo.bank.account.app.domain.exception.NotSupportedException;
import com.acmebank.demo.bank.account.app.domain.exception.NotZeroBalanceException;
import com.acmebank.demo.bank.account.app.exposition.dto.ApiError;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		final List<String> errors = new ArrayList<String>();

		for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}

		for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}

		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);

		return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
	}

	@ExceptionHandler(value = { AccountNotFoundException.class })
	@ResponseBody
	public ResponseEntity<Object> handleNotFoundException(final HttpServletRequest req, final Exception ex) {

		final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), "error occurred");

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler(value = { InvalidAccountNumberException.class })
	@ResponseBody
	public ResponseEntity<Object> handleInvalidAccountNumberException(final HttpServletRequest req,
			final Exception ex) {

		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "error occurred");

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler({ InvalidAccountStatusException.class, NotSupportedException.class })
	@ResponseBody
	public ResponseEntity<Object> handleInvalidAccountStatusException(final Exception ex, final WebRequest request) {

		final ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getLocalizedMessage(), "error occurred");

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler({ IllegalAccountStatusException.class })
	@ResponseBody
	public ResponseEntity<Object> handleIllegalAccountStatusException(final Exception ex, final WebRequest request) {

		final ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getLocalizedMessage(), "error occurred");

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler({ Exception.class, NotZeroBalanceException.class, InsufficientBalanceException.class })
	@ResponseBody
	public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {

		final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(),
				"error occurred");

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

}