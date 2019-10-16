package com.example.demo.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * This class is the main exception handler of the application, detect the
 * Exception flow, creates a new ResponseEntity with the
 * {@link ExceptionResponse ExceptionResponse} and the corresponding 4XX or 5XX
 * error per exception
 * 
 * @author <a href="mailto:rodrigo.moncada@live.com">Rodrigo Moncada</a>
 * @see ControllerAdvice
 * @see RestController
 * @see ResponseEntityExceptionHandler
 */
@ControllerAdvice
@RestController
public class EmployeeResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Logger instance
	 * 
	 * @see Logger
	 * @see LogManager
	 */
	private static final Logger logger = LogManager.getLogger(EmployeeResponseEntityExceptionHandler.class);

	/**
	 * This Method handles any exception that are not mapped for an specific 4XX or
	 * 5XX error, and automatically is detected as a INTERNAL_SERVER_ERROR
	 * 
	 * @param ex      Any Exception and classes that has Exception has parent
	 * @param request WebRequest with the data to send the Exception to the
	 *                controller response
	 * @return ResponseEntity with the error information
	 */
	@ExceptionHandler({ Exception.class })
	public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
		String message = "";
		if (ex.getMessage() == null || ex.getMessage().isEmpty()) {
			if (ex instanceof NullPointerException) {
				message = "Null pointer detected during the process of this request. Try again or contact the system administrator";
			} else {
				message = "Something went wrong. Try again or contact the system administrator";
			}
		} else {
			message = ex.getMessage();
		}
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), message, request.getDescription(false));
		logError(ex);
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * This Method handles any exception that is related with find or retrieve resources
	 * 
	 * @param ex
	 *     Any Exception and classes that has Exception has parent
	 * @param request
	 *     WebRequest with the data to send the Exception to the controller response
	 * @return ResponseEntity with the error information
	 */
	@ExceptionHandler({ EmployeeNotFoundException.class, UserNotFoundException.class })
	public final ResponseEntity<Object> handleNotFoundException(Exception ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
				request.getDescription(false));
		logError(ex);
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}
	
	/**
	 * This Method handles any exception that is related with Employee's status
	 * 
	 * @param ex
	 *     Any Exception and classes that has Exception has parent
	 * @param request
	 *     WebRequest with the data to send the Exception to the controller response
	 * @return ResponseEntity with the error information
	 */
	@ExceptionHandler({ EmployeeAlreadyActiveException.class })
	public final ResponseEntity<Object> handleEmployeeAlreadyActive(Exception ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
				request.getDescription(false));
		logError(ex);
		return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
	}
	
	/**
	 * Customize the response for MethodArgumentNotValidException.
	 * <p>
	 * This method delegates to {@link #handleExceptionInternal}.
	 * 
	 * @param ex      the exception
	 * @param headers the headers to be written to the response
	 * @param status  the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		StringBuilder builder = new StringBuilder();
		StringBuilder temp = new StringBuilder();
		List<FieldError> errors = ex.getBindingResult().getFieldErrors();
		Map<String, Object> errorsMap = new HashMap<>();
		int errorCount = 0;
		for (FieldError error : errors) {
			if (!errorsMap.containsKey(error.getField())) {
				errorCount++;
				errorsMap.put(error.getField(), error.getField());
				temp.append("[Invalid value '" + error.getRejectedValue() + "' in field: " + error.getField() + " -> "
						+ error.getDefaultMessage() + "], ");
			}
		}
		builder.append("Errors found: " + errorCount + " -> ").append(temp.toString());
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Validation Failed. " + builder.toString(),
				request.getDescription(false));
		logError(ex);
		return new ResponseEntity<Object>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * @param ex
	 */
	private void logError(Exception ex) {
		logger.error(ex.getMessage());
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		logger.error(writer.toString());
	}
}
