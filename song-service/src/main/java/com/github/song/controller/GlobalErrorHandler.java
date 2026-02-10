package com.github.song.controller;

import com.github.resource.exception.InvalidIdException;
import com.github.song.exception.MetadataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(exception = InvalidIdException.class)
    public Mono<ResponseEntity<String>> handleInvalidIdException(InvalidIdException ex) {
        log.error("Invalid id exception", ex);
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

    @ExceptionHandler(MetadataNotFoundException.class)
    public Mono<ResponseEntity<String>> handleMetadataNotFoundException(MetadataNotFoundException ex) {
        log.error("Metadata not found", ex);
        return Mono.just(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleGenericException(Exception ex) {
        log.error("Unknown exception", ex);
        return Mono.just(ResponseEntity.internalServerError().body(ex.getMessage()));
    }
}