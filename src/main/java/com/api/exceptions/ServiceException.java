package com.api.exceptions;

public class ServiceException extends RuntimeException {
    public ServiceException() {
        super("Error 500");
    }
}