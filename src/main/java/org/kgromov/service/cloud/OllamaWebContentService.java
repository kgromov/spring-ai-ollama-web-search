package org.kgromov.service.cloud;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kgromov.service.WebContentService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

// TODO: add structure output and decouple from WebContentService interface
@Profile("cloud")
@Service
@Slf4j
@RequiredArgsConstructor
public class OllamaWebContentService implements WebContentService {
    private final RestClient ollamaRestClient;

    @Override
    public String fetchWebContent(String url) {
        return ollamaRestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/web_fetch").build())
                .body(Map.of("url", url))
                .retrieve()
                .toEntity(JsonNode.class)
                .toString();
    }

    @Override
    public String searchWebContent(String query) {
        return ollamaRestClient.post()
                .uri(uriBuilder ->  uriBuilder.path("/web_search").build())
                .body(Map.of("query", query))
                .retrieve()
                .toEntity(JsonNode.class)
                .toString();
    }
}
