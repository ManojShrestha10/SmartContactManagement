package com.scm.helper;
//created by Manoj Shrestha o n 2025-06-03
// This class is used to handle resource not found exceptions in the application
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException() {
        super("Resource not found");
    }

}
