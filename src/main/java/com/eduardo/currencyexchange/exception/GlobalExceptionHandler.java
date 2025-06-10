package com.eduardo.currencyexchange.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<String> handleExternalApiException(ExternalApiException ex) {
        logger.error("External API call failed: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(InvalidApiResponseException.class)
    public ResponseEntity<String> handleInvalidApiResponseException(InvalidApiResponseException ex) {
        logger.error("Invalid external API response: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleGenericRuntimeException(RuntimeException ex) {
        logger.error("An unexpected server error occurred: {}", ex.getMessage(), ex);
        // Responds with 500 Internal Server Error for unhandled runtime issues
        return new ResponseEntity<>("An unexpected server error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String error = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
                                     ex.getValue(), ex.getName(), ex.getRequiredType().getSimpleName());
        logger.error("Request parameter type mismatch: {}", error, ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}