package com.baraigad.adminuser.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class AdminUserNotFoundException extends RuntimeException {
    public AdminUserNotFoundException(String message) {

        super(message);

    }
}
