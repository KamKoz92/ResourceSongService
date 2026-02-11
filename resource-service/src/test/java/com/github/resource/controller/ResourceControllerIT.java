package com.github.resource.controller;

import com.github.resource.service.SongService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@Disabled
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class ResourceControllerIT {

    @MockitoBean
    SongService songService;

    @Autowired
    WebTestClient client;

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
                .uri("/resources/12")
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void testBadRequestOnGetEndpoint() {
        // Given

        // When & Then
        client.get()
                .uri("/resources/12a")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testCreatedOnSaveEndpoint() {
        // Given
        when(songService.save(any())).thenAnswer(a -> Mono.just(a.getArguments()[0]));
        byte[] mp3File = getSampleMP3();

        // When & Then
        client.post()
                .uri("/resources")
                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                .bodyValue(mp3File)
                .exchange()
                .expectStatus().isOk();
    }


    private byte[] getSampleMP3() {
        try(var is = this.getClass().getResourceAsStream("/mp3_sample.mp3")) {
            assert is != null;
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}