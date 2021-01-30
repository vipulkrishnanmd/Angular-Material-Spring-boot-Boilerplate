package com.vipulkrishnanmd.workflows.exception;

public class ResourceNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     * 
     * @param message
     */
    public ResourceNotFoundException(String message) {
        super(message);
     }
}
