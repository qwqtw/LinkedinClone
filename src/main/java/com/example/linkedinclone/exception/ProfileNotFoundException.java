package com.example.linkedinclone.exception;

public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException() {
        super("Default message: Profile not found");
    }

    public ProfileNotFoundException(String message) {
        super(message);
    }
}

