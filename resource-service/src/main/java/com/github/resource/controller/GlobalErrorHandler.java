package com.github.resource.controller;

import com.github.resource.exception.InvalidCSVException;
import com.github.resource.exception.InvalidIdException;
import com.github.resource.exception.InvalidMP3FormatException;
import com.github.resource.exception.MP3FileNotFoundException;
import com.github.resource.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(exception = {
            InvalidIdException.class,
            InvalidCSVException.class,
            InvalidMP3FormatException.class,
            ResponseStatusException.class})
    public Mono<ResponseEntity<ErrorResponse>> handleInvalidIdException(RuntimeException e) {
        log.error("", e);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode("400");
        if (e instanceof UnsupportedMediaTypeStatusException ex) {
            errorResponse.setErrorMessage(String.format("Invalid file format: %s. Only MP3 files are allowed", ex.getContentType()));
            return Mono.just(ResponseEntity.badRequest().body(errorResponse));
        }
        if (e instanceof ResponseStatusException ex) {
            errorResponse.setErrorMessage(ex.getReason());
            return Mono.just(ResponseEntity.badRequest().body(errorResponse));
        }
        errorResponse.setErrorMessage(e.getMessage());
        return Mono.just(ResponseEntity.badRequest().body(errorResponse));
    }
    
    @ExceptionHandler(MP3FileNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleMP3FileNotFoundException(MP3FileNotFoundException e) {
        log.error("", e);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode("404");
        errorResponse.setErrorMessage(e.getMessage());
        return Mono.just(ResponseEntity.status(404).body(errorResponse));
    }

    @ExceptionHandler(exception = Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGenericException(Exception e) {
        log.error("", e);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode("500");
        errorResponse.setErrorMessage(e.getMessage());
        return Mono.just(ResponseEntity.internalServerError().body(errorResponse));
    }
}