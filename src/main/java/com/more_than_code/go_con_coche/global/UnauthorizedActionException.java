package com.more_than_code.go_con_coche.global;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedActionException extends RuntimeException {
    public UnauthorizedActionException(String action, String entityClass, String entityId) {
        super(String.format("Unauthorized to %s %s with id %s", action, entityClass, entityId));
    }
}