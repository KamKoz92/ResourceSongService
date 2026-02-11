package com.github.resource.service;

import com.github.common.model.IdsResponse;
import com.github.common.model.SongMetadata;
import com.github.resource.exception.SongServiceException;
import com.github.resource.model.ResourceEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongService {

    private final WebClient client;

    public Mono<ResourceEntity> save(ResourceEntity resourceEntity) {
        log.info("Sending request to save mp3 metadata");
        SongMetadata metadata = extractMetadata(resourceEntity);
        return client.post()
                .uri("/songs")
                .bodyValue(metadata)
                .retrieve()
                .bodyToMono(String.class)
                .thenReturn(resourceEntity)
                .onErrorMap(error -> new SongServiceException("Error while saving mp3 metadata"));
    }

    public Mono<SongMetadata> get(String id) {
        log.info("Sending request to get mp3 metadata");
        return client.get()
                .uri(uri -> uri.path("/songs/{id}").build(id))
                .retrieve()
                .bodyToMono(SongMetadata.class);
    }

    public Mono<List<Long>> delete(List<Long> ids) {
        if (ids.isEmpty()) {
            return Mono.just(ids);
        }
        log.info("Sending request to delete mp3 metadata");
        return client.delete()
                .uri(uri -> uri.path("/songs").queryParam("id", ids).build())
                .retrieve()
                .bodyToMono(IdsResponse.class)
                .map(IdsResponse::ids)
                .onErrorMap(error -> new SongServiceException("Error while deleting mp3 metadata"));
    }

    private SongMetadata extractMetadata(ResourceEntity resourceEntity) {
        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        BodyContentHandler handler = new BodyContentHandler();
        ParseContext context = new ParseContext();
        try (ByteArrayInputStream input = new ByteArrayInputStream(resourceEntity.getAudio())) {
            parser.parse(input, handler, metadata, context);
        } catch (IOException | TikaException | SAXException e) {
            throw new RuntimeException(e);
        }

        String duration = formatDuration(metadata.get("xmpDM:duration"));
        SongMetadata songMetadata = new SongMetadata();
        songMetadata.setName(metadata.get("dc:title"));
        songMetadata.setAlbum(metadata.get("xmpDM:album"));
        songMetadata.setYear(metadata.get("xmpDM:releaseDate"));
        songMetadata.setArtist(metadata.get("xmpDM:artist"));
        songMetadata.setDuration(duration);
        songMetadata.setId(resourceEntity.getId());
        return songMetadata;
    }

    private String formatDuration(String xmpDMDuration) {
        int decimal = xmpDMDuration.indexOf(".");
        String seconds = xmpDMDuration.substring(0, decimal);
        int i = Integer.parseInt(seconds);
        int minutes = i / 60;
        int second = i % 60;
        String s = String.valueOf(minutes);
        String ss = String.valueOf(second);
        return String.format("%s:%s", StringUtils.leftPad(s, 2, "0"), StringUtils.leftPad(ss, 2, "0"));
    }


}
