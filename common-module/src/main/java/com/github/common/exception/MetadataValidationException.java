package com.github.common.exception;

import lombok.Getter;

import java.util.Map;

public class MetadataValidationException extends RuntimeException {
    @Getter
    private final Map<String, String> validationErrors;

    public MetadataValidationException(String message, Map<String, String> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }
}
