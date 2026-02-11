package com.github.song.repository;

import com.github.common.model.SongMetadata;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SongRepository extends ReactiveCrudRepository<SongMetadata, Long>, CustomSongRepository {
}
