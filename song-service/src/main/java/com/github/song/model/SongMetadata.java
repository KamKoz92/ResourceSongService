package com.github.song.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("songs")
public class SongMetadata {
    @Id
    Long id;
    String name;
    String artist;
    String album;
    String duration;
    String year;

}