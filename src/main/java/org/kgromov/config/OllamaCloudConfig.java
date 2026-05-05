package org.kgromov.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

@Profile({"cloud"})
@Configuration
public class OllamaCloudConfig {

    @Value("${spring.ai.ollama.base-url}")
    private String baseUrl;


    @Value("${spring.ai.ollama.chat.options.model}")
    private String model;

    @Bean
    private static RestClient.Builder ollamaResClientBuilder(WebSearchConfig webSearchConfig) {
        return RestClient.builder()
                .baseUrl(webSearchConfig.engineUrl())
                .requestInterceptor(new LoggingInterceptor())
                .defaultHeaders(header -> header.setContentType(MediaType.APPLICATION_JSON))
                .defaultHeaders(header -> header.setBearerAuth(webSearchConfig.apiKey()));
    }

    @Bean
    public OllamaChatModel ollamaCloudChatModel(RestClient.Builder restClientBuilder,
                                                List<ToolCallback> webSearchTools) {
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
    RestClient ollamaRestClient(RestClient.Builder ollamaResClientBuilder) {
        return ollamaResClientBuilder.build();
    }

    @Slf4j
    private static class LoggingInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            log.debug("Request: method = {}, url = {}", request.getMethod(), request.getURI());
            ClientHttpResponse response = execution.execute(request, body);
            String responseBody = this.getContent(response).orElse("");
            log.debug("Response Status: {}, content (to string) = {}", response.getStatusCode(), responseBody);
            return response;
        }

        private Optional<String> getContent(ClientHttpResponse response) {
            try {
                return Optional.of(new String(response.getBody().readAllBytes(), UTF_8));
            } catch (IOException e) {
                log.error("Unable to get response body", e);
                return Optional.empty();
            }
        }
    }
}

