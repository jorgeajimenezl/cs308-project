package com.harbourspace.cs308.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class<?> entityClass, Object identifier) {
        super(entityClass.getSimpleName() + " not found for identifier: " + identifier);
    }
    
    public EntityNotFoundException(String message) {
        super(message);
    }
}