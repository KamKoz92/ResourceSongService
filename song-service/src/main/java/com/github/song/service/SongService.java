package com.github.song.service;

import com.github.common.model.SongMetadata;
import com.github.common.util.Validator;
import com.github.song.exception.MetadataAlreadyPresentException;
import com.github.song.exception.MetadataNotFoundException;
import com.github.song.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository repository;
    private final Validator validator;

    public Mono<Long> save(SongMetadata metadata) {
        log.info("Saving mp3 metadata to database");
        validator.validate(metadata);
        return repository.findById(metadata.getId())
                .map(this::throwRecordAlreadyPresentException)
                .switchIfEmpty(repository.insert(metadata))
                .doOnNext(id -> log.info("Saved song metadata with id {}", id.getId()))
                .map(SongMetadata::getId);
    }

    public Mono<SongMetadata> get(String id) {
        log.info("Getting mp3 metadata with id {}", id);
        // TODO this long
        Long l = validator.mapIdToNumber(id);
        return repository.findById(l)
                .switchIfEmpty(Mono.error(new MetadataNotFoundException(String.format("Song metadata for ID=%s not found", id))));
    }

    public Mono<List<Long>> delete(String ids) {
        return Flux.fromIterable(validator.flatMapCsv(ids))
                .flatMap(this::delete)
                .collectList();
    }

    private Mono<Long> delete(Long id) {
        log.info("Deleting mp3 file with id {}", id);
        return repository.findById(id)
                .flatMap($ -> repository.deleteById(id)
                        .thenReturn(id));
    }

    private SongMetadata throwRecordAlreadyPresentException(SongMetadata metadata) {
        Long id = metadata.getId();
        String errorMsg = String.format("Metadata for resource ID=%s already exists", id);
        log.error(errorMsg);
        throw new MetadataAlreadyPresentException(errorMsg);
    }


}
