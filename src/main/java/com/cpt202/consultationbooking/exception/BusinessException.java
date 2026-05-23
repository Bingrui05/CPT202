package com.cpt202.consultationbooking.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }
}
