package com.github.song.controller;

import com.github.resource.model.IdResponse;
import com.github.song.model.SongMetadata;
import com.github.song.service.SongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/songs")
public class SongServiceController {

    private final SongService service;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    Mono<ResponseEntity<IdResponse>> save(@RequestBody Mono<SongMetadata> metadata) {
        log.info("Received save mp3 metadata request");
        return metadata
                .flatMap(service::save)
                .map(entityId -> new ResponseEntity<>(new IdResponse(entityId), OK));
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    Mono<ResponseEntity<SongMetadata>> get(@PathVariable String id) {
        log.info("Received get mp3 metadata request");
        return Mono.just(id)
                .flatMap(service::get)
                .map(bytes -> new ResponseEntity<>(bytes, OK));

    }

    @DeleteMapping(path = "/{id}")
    Mono<ResponseEntity<IdResponse>> delete(@PathVariable String id) {
        log.info("Received delete mp3 metadata request");
        return Mono.just(id)
                .flatMap(service::delete)
                .map(entityId -> new ResponseEntity<>(new IdResponse(entityId), OK));
    }

}
