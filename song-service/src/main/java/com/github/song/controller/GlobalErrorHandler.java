package com.github.song.controller;

import com.github.song.exception.InvalidCSVException;
import com.github.song.exception.InvalidIdException;
import com.github.song.exception.MetadataAlreadyPresentException;
import com.github.song.exception.MetadataNotFoundException;
import com.github.song.exception.MetadataValidationException;
import com.github.song.model.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(exception = {
            InvalidIdException.class,
            InvalidCSVException.class,
            MetadataValidationException.class})
    public Mono<ResponseEntity<ErrorResponse>> handleInvalidIdException(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode("400");
        errorResponse.setErrorMessage(e.getMessage());
        if (e instanceof MetadataValidationException ex) {
            errorResponse.setDetails(ex.getValidationErrors());
        }
        return Mono.just(ResponseEntity.badRequest().body(errorResponse));
    }

    @ExceptionHandler(MetadataNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleMetadataNotFoundException(MetadataNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(e.getMessage());
        errorResponse.setErrorCode("404");
        return Mono.just(ResponseEntity.status(404).body(errorResponse));
    }

    @ExceptionHandler(MetadataAlreadyPresentException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleMetadataAlreadyPresentException(MetadataAlreadyPresentException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(e.getMessage());
        errorResponse.setErrorCode("409");
        return Mono.just(ResponseEntity.status(409).body(errorResponse));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGenericException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode("500");
        errorResponse.setErrorMessage(e.getMessage());
        return Mono.just(ResponseEntity.internalServerError().body(errorResponse));
    }
}