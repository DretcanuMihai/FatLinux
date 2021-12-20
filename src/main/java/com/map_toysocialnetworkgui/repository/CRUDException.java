package com.map_toysocialnetworkgui.repository;

/**
 * a class that describes an exception related to CRUD in repo
 */
public class CRUDException extends RuntimeException{
    public CRUDException() {
    }

    public CRUDException(String message) {
        super(message);
    }

    public CRUDException(String message, Throwable cause) {
        super(message, cause);
    }

    public CRUDException(Throwable cause) {
        super(cause);
    }

    public CRUDException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
