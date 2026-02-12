package com.github.song.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.song.util.ResourceHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class Config {

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
