package com.github.song.controller;

import com.github.song.repository.SongRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;

@Slf4j
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class SongControllerIT {

    @Autowired
    SongRepository repository;

    @Autowired
    WebTestClient client;

    @BeforeEach
    void cleanDb() {
        repository.deleteAll().block();
    }

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Test
    void testNotFoundOnGetEndpoint() {
        // Given

        // When & Then
        client.get()
                .uri("/songs/12")
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void testBadRequestOnGetEndpoint() {
        // Given

        // When & Then
        client.get()
                .uri("/songs/12a")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testCreatedOnSaveEndpoint() {
        // Given
        String body = getFileContents();
        System.out.println("Host: " + postgres.getHost());
        System.out.println("Port: " + postgres.getFirstMappedPort());
        System.out.println("Database: " + postgres.getDatabaseName());
        System.out.println("Username: " + postgres.getUsername());
        System.out.println("Password: " + postgres.getPassword());
        // When & Then
        client.post()
                .uri("/songs")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .bodyValue(body)
                .exchange();
        log.info("");
    }


    private String getFileContents() {
        try(var is = this.getClass().getResourceAsStream("/sampleMetadata.json")) {
            assert is != null;
            return new String(is.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}