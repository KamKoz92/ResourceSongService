package com.github.song.jpa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongJPARepository extends JpaRepository<SongMetadataJPA, Long> {}