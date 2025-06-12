package com.example.library.exception;

import lombok.Getter;

@Getter
public class StudentException extends RuntimeException{
    private final HttpStatusCode status;

    public StudentException(String message, HttpStatusCode status) {
        super(message);
        this.status = status;
    }
}
