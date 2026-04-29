package org.kgromov.config;

import org.kgromov.tools.WebTools;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import java.util.List;

@Profile({"cloud"})
@Configuration
public class OllamaCloudConfig {

    @Value("${spring.ai.ollama.base-url")
    private String baseUrl;

    @Value("${spring.ai.ollama.chat.options.model}")
    private String model;

    @Bean
    RestClient.Builder restClientBuilder(WebSearchConfig webSearchConfig) {
        return RestClient.builder()
                .baseUrl(webSearchConfig.engineUrl())
                .defaultHeaders(header -> header.setContentType(MediaType.APPLICATION_JSON))
                .defaultHeaders(header -> header.setBearerAuth(webSearchConfig.apiKey()));
    }

    @Bean
    RestClient ollamaRestClient(RestClient.Builder restClientBuilder) {
        return restClientBuilder.build();
    }

    @Bean
    public OllamaChatModel ollamaChatModel(RestClient.Builder restClientBuilder, List<ToolCallback> webSearchTools) {
        var api = OllamaApi.builder()
                .baseUrl(baseUrl)
                .restClientBuilder(restClientBuilder)
                .build();
        var options = OllamaChatOptions.builder()
                .model(model)
                .temperature(0.7)
                .toolCallbacks(webSearchTools)
                .build();
        return OllamaChatModel.builder()
                .ollamaApi(api)
                .defaultOptions(options)
                .build();
    }

    @Bean
    public List<ToolCallback> webSearchTools(WebTools webTools) {
        return List.of(ToolCallbacks.from(webTools));
    }
}

