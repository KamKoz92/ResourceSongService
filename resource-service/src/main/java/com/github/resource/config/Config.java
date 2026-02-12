package com.github.resource.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.resource.util.ResourceHelper;
import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class Config {

    @Bean
    Tika tika() {
        return new Tika();
    }

    @Bean
    // TODO externalize it
    WebClient songWebClient() {
        return WebClient.create("http://localhost:8081");
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
