package com.brogrammers.floormaster.service;

public class InvalidOrderException extends Exception {

    public InvalidOrderException(String message) {
        super(message);
    }

    public InvalidOrderException(String message, Throwable cause) {
        super(message, cause);
    }
    
}