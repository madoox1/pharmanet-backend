package com.pharmacy.Exceptions;

public class ErrorDTO {
    private String message;

    public ErrorDTO(String message) {
        this.message = message;
    }

    // Getters and setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
