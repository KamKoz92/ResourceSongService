package com.github.resource.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.resource.util.ResourceHelper;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class Config {

    @Value("${service.song.url}")
    public String songUrl;

    @Bean
    Tika tika() {
        return new Tika();
    }

    @Bean
    WebClient songWebClient() {
        return WebClient.create(songUrl);
    }

    @Bean
    public JsonMapper.Builder jsonMapperBuilder() {
        return JsonMapper.builder()
                .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL))
                .changeDefaultPropertyInclusion(incl -> incl.withContentInclusion(JsonInclude.Include.NON_NULL));

    }

    @Bean
    public ResourceHelper resourceHelper() {
        return new ResourceHelper();
    }
}
