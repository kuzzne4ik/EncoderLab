package com.api.exceptions;

import com.api.component.CustomLogger;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final CustomLogger customLogger;

    public GlobalExceptionHandler(CustomLogger customLogger) {
        this.customLogger = customLogger;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public com.api.exceptions.ErrorResponse handleInternalServerError(RuntimeException ex) {
        customLogger.logError("error, 500 code");
        return new com.api.exceptions.ErrorResponse(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpClientErrorException.class, HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class, MissingServletRequestParameterException.class,
            ConstraintViolationException.class})
    public com.api.exceptions.ErrorResponse handleBadRequestException(Exception ex) {
        customLogger.logError("error, 400 code");
        return new com.api.exceptions.ErrorResponse("400 error, BAD REQUEST");
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public com.api.exceptions.ErrorResponse handleMethodNotAllowed(Exception ex) {
        customLogger.logError("error, 405 code");
        return new com.api.exceptions.ErrorResponse("405 error, METHOD NOT ALLOWED");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public com.api.exceptions.ErrorResponse handlerFoundException(Exception ex) {
        customLogger.logError("error, 404 code");
        return new com.api.exceptions.ErrorResponse("404 error, NOT FOUND");
    }
}

