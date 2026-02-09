package com.github.resource.config;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Config {

    @Bean
    Tika tika() {
        return new Tika();
    }

    @Bean
    WebClient songWebClient() {
        return WebClient.create("http://localhost:8080");
    }
}
