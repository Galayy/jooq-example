package com.itechart.jooq.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class IncorrectJwtAuthenticationException extends RuntimeException {

    @Getter
    private final HttpStatus status;

    public IncorrectJwtAuthenticationException(String message) {
        super(message);
        this.status = UNAUTHORIZED;
    }

}
