package com.github.song.repository;

import com.github.song.model.SongMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomSongRepositoryImpl implements CustomSongRepository {

    private final DatabaseClient client;

    @Override
    public Mono<SongMetadata> insert(SongMetadata metadata) {
        return client.sql("INSERT INTO songs (id, name, artist, album, duration, year) VALUES (:id, :name, :artist, :album, :duration, :year)")
                .bind("id", metadata.getId())
                .bind("name", metadata.getName())
                .bind("artist", metadata.getArtist())
                .bind("album", metadata.getAlbum())
                .bind("duration", metadata.getDuration())
                .bind("year", metadata.getYear())
                .then().thenReturn(metadata);
    }
}
