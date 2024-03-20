package com.java.exam.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DeliveryRejectionException extends RuntimeException {
    public DeliveryRejectionException(String message) {
        super(message);
    }
}
