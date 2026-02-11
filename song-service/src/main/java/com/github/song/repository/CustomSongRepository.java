package com.github.song.repository;

import com.github.common.model.SongMetadata;
import reactor.core.publisher.Mono;

public interface CustomSongRepository {
    Mono<SongMetadata> insert(SongMetadata metadata);
}
