package com.baraigad.common.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Email address or password is incorrect.");
    }
}
