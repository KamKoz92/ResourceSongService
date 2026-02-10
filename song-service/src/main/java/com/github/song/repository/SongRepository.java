package com.github.song.repository;

import com.github.song.model.SongMetadata;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SongRepository extends ReactiveCrudRepository<SongMetadata, Long>, CustomSongRepository {
}
