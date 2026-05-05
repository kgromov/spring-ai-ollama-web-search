package org.kgromov.config;

import lombok.extern.slf4j.Slf4j;
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
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

@Profile({"cloud"})
@Configuration
public class OllamaCloudConfig {

    @Bean
    RestClient ollamaRestClient(WebSearchConfig webSearchConfig) {
        return RestClient.builder()
                .baseUrl(webSearchConfig.engineUrl())
                .requestInterceptor(new LoggingInterceptor())
                .defaultHeaders(header -> header.setContentType(MediaType.APPLICATION_JSON))
                .defaultHeaders(header -> header.setBearerAuth(webSearchConfig.apiKey()))
                .build();
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

