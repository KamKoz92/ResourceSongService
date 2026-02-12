package com.github.resource.controller;

import com.github.resource.model.IdResponse;
import com.github.resource.model.IdsResponse;
import com.github.resource.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/resources")
public class ResourceServiceController {

    private final ResourceService service;

    @PostMapping(consumes = "audio/mpeg")
    Mono<ResponseEntity<IdResponse>> save(@RequestBody Mono<byte[]> audioData) {
        log.info("Received save mp3 request");
        return audioData
                .flatMap(service::save)
                .map(entityId -> new ResponseEntity<>(new IdResponse(entityId), OK));
    }

    @GetMapping(path = "/{id}", produces = "audio/mpeg")
    Mono<ResponseEntity<byte[]>> get(@PathVariable String id) {
        return Mono.just(id)
                .flatMap(service::get)
                .map(bytes -> new ResponseEntity<>(bytes, OK));

    }

    @DeleteMapping(params = "id")
    Mono<ResponseEntity<IdsResponse>> delete(@RequestParam("id") String ids) {
        return Mono.just(ids)
                .flatMap(service::delete)
                .map(list -> new ResponseEntity<>(new IdsResponse(list), OK));
    }


}
