package com.itechart.jooq.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class EntityNotFoundException extends RuntimeException {

    @Getter
    private final HttpStatus status;

    public EntityNotFoundException(String message) {
        super(message);
        this.status = NOT_FOUND;
    }

}
