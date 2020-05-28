package com.itechart.jooq.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class ForbiddenAccessException extends RuntimeException {

    @Getter
    private final HttpStatus status;

    public ForbiddenAccessException(String message) {
        super(message);
        this.status = FORBIDDEN;
    }

}
