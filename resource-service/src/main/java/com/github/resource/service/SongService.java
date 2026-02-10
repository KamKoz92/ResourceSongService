package com.github.resource.service;

import com.github.resource.model.ResourceEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.xml.sax.SAXException;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongService {

    private final WebClient client;

    public Mono<ResourceEntity> save(ResourceEntity resourceEntity) {
        log.info("Sending request to save mp3 metadata");
        Metadata metadata = extractMetadata(resourceEntity);
        return client.post()
                .uri("/save")
                .bodyValue(metadata)
                .retrieve()
                .bodyToMono(String.class)
                .thenReturn(resourceEntity);
    }

    public Mono<Metadata> get(String id) {
        log.info("Sending request to get mp3 metadata");
        return client.get()
                .uri(uri -> uri.path("/{id}").queryParam("id", id).build())
                .retrieve()
                .bodyToMono(Metadata.class);
    }

    private Metadata extractMetadata(ResourceEntity resourceEntity) {
        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        BodyContentHandler handler = new BodyContentHandler();
        ParseContext context = new ParseContext();
        try (ByteArrayInputStream input = new ByteArrayInputStream(resourceEntity.getAudio())) {
            parser.parse(input, handler, metadata, context);
        } catch (IOException | TikaException | SAXException e) {
            throw new RuntimeException(e);
        }
        return metadata;
    }

    public Mono<Void> delete(Long id) {
        log.info("Sending request to delete mp3 metadata");
        return client.delete()
                .uri("uri")
                .retrieve()
                .bodyToMono(Void.class);
    }
}
