package com.github.resource.service;

import com.github.resource.exception.InvalidCSVException;
import com.github.resource.exception.InvalidIdException;
import com.github.resource.exception.InvalidMP3FormatException;
import com.github.resource.exception.MP3FileNotFoundException;
import com.github.resource.model.ResourceEntity;
import com.github.resource.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository repository;
    private final SongService songService;
    private final Tika tika;

    public Mono<Long> save(byte[] audioData) {
        log.info("Saving mp3 file to database");
        return validateMP3Format(audioData)
                .map(this::getResourceEntity)
                .flatMap(repository::save)
                .flatMap(songService::save)
                .map(ResourceEntity::getId);
    }

    public Mono<byte[]> get(String id) {
        log.info("Getting mp3 file with id {}", id);
        return repository.findById(mapIdToNumber(id))
                .map(ResourceEntity::getAudio)
                .switchIfEmpty(Mono.error(new MP3FileNotFoundException()));
    }

    public Mono<List<Long>> delete(String ids) {
        return Flux.fromIterable(flatMapCsv(ids))
                .map(this::mapIdToNumber)
                .flatMap(this::delete)
                //TODO on error continue
                .collectList();
    }

    private Mono<Long> delete(Long id) {
        log.info("Deleting mp3 file with id {}", id);
        return repository.deleteById(id)
                .flatMap(v -> songService.delete(id))
                .thenReturn(id);
    }

    private ResourceEntity getResourceEntity(byte[] audioData) {
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setAudio(audioData);
        return resourceEntity;
    }

    private Mono<byte[]> validateMP3Format(byte[] bytes) {
        if (Strings.CI.equals(tika.detect(bytes), "audio/mpeg")) {
            return Mono.just(bytes);
        }
        return Mono.error(new InvalidMP3FormatException("Invalid mp3 format"));
    }


    private List<String> flatMapCsv(String ids) {
        if (StringUtils.length(ids) >= 200) {
            throw new InvalidCSVException("Csv string length has 200 or more characters");
        }
        return Arrays.asList(ids.split(","));
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
}
