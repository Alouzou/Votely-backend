package com.alouzou.sondage.exceptions;

public class CategoryInactiveException extends RuntimeException {
    public CategoryInactiveException(String message) {
        super(message);
    }
}