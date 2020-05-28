package com.itechart.jooq.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

public class EntityAlreadyProcessedException extends RuntimeException {

    @Getter
    private final HttpStatus status;

    public EntityAlreadyProcessedException(String message) {
        super(message);
        this.status = UNPROCESSABLE_ENTITY;
    }

}
