package com.github.resource.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Data
@Table("songs")
public class SongMetadata {
    @Id
    private Long id;
    private String name;
    private String artist;
    private String album;
    private String duration;
    private String year;
}