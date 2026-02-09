package com.github.resource.controller;

import com.github.resource.exception.InvalidCSVException;
import com.github.resource.exception.InvalidIdException;
import com.github.resource.exception.InvalidMP3FormatException;
import com.github.resource.exception.MP3FileNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(exception = {
            InvalidIdException.class,
            InvalidCSVException.class,
            InvalidMP3FormatException.class})
    public Mono<ResponseEntity<String>> handleInvalidIdException(RuntimeException ex) {
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

    @ExceptionHandler(MP3FileNotFoundException.class)
    public Mono<ResponseEntity<String>> handleMP3FileNotFoundException(MP3FileNotFoundException ex) {
        return Mono.just(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleGenericException(Exception ex) {
        return Mono.just(ResponseEntity.internalServerError().body(ex.getMessage()));
    }
}