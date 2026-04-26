package org.kgromov.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "web.search")
public record WebSearchConfig(String engineUrl, int timeout, int maxContentLength) {
}
