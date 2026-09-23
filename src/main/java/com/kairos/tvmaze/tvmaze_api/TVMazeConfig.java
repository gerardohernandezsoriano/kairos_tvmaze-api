package com.kairos.tvmaze.tvmaze_api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class TVMazeConfig {

    @Value("${tvmaze.base-url}") String baseUrl;

    @Bean
    public RestClient tvMazeRestClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}