package com.github.song.service;

import com.github.resource.exception.InvalidIdException;
import com.github.song.exception.MetadataAlreadyPresentException;
import com.github.song.exception.MetadataNotFoundException;
import com.github.song.model.SongMetadata;
import com.github.song.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository repository;

    public Mono<Long> save(SongMetadata metadata) {
        log.info("Saving mp3 metadata to database");
        return repository.findById(metadata.getId())
                .map(this::throwRecordAlreadyPresentException)
                .switchIfEmpty(repository.insert(metadata))
                .doOnNext(id -> log.info("Saved song with id {}", id.getId()))
                .map(SongMetadata::getId);
    }

    public Mono<SongMetadata> get(String id) {
        log.info("Getting mp3 metadata with id {}", id);
        return repository.findById(mapIdToNumber(id))
                .switchIfEmpty(Mono.error(() ->
                        new MetadataNotFoundException(String.format("Metadata with id: %s is not present in database", id))));
    }

    public Mono<Long> delete(String id) {
        log.info("Deleting mp3 file with id {}", id);
        return repository.deleteById(mapIdToNumber(id))
                .map(v -> mapIdToNumber(id));
    }

    private Long mapIdToNumber(String id) {
        if (StringUtils.isNumeric(id)) {
            try {
                long l = Long.parseLong(id);
                if (l == 0) {
                    throw new InvalidIdException("Provided id is equal to 0");
                }
                return l;
            } catch (NumberFormatException e) {
                throw new InvalidIdException("Provided id is not parsable to number");
            }
        }
        throw new InvalidIdException("Provided id is non numeric");
    }

    private SongMetadata throwRecordAlreadyPresentException(SongMetadata metadata) {
        Long id = metadata.getId();
        String errorMsg = String.format("Metadata with id: %s already present in database.", id);
        log.error(errorMsg);
        throw new MetadataAlreadyPresentException(errorMsg);
    }
}
