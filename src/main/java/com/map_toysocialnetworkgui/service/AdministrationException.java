package com.map_toysocialnetworkgui.service;

/**
 * An Exception class specialized on Service Administration errors
 */
public class AdministrationException extends RuntimeException {
    public AdministrationException() {
    }

    public AdministrationException(String message) {
        super(message);
    }

    public AdministrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdministrationException(Throwable cause) {
        super(cause);
    }

    public AdministrationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
