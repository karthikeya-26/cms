 package com.dao;

public class DaoException extends Exception {

    private static final long serialVersionUID = 1L;

	/**
     * Constructor with a message.
     * 
     * @param message the detail message
     */
    public DaoException(String message) {
        super(message);
    }

    /**
     * Constructor with a message and a cause.
     * 
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
