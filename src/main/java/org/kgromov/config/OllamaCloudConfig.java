package org.kgromov.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Profile({"cloud"})
@Configuration
public class OllamaCloudConfig {

    @Bean
    RestClient ollamaRestClient(WebSearchConfig webSearchConfig) {
        return RestClient.builder()
                .baseUrl(webSearchConfig.engineUrl())
                .defaultHeaders(header -> header.setContentType(MediaType.APPLICATION_JSON))
                .defaultHeaders(header -> header.setBearerAuth(webSearchConfig.apiKey()))
                .build();
    }
}

