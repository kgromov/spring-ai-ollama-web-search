package org.kgromov.tools;

import lombok.RequiredArgsConstructor;
import org.kgromov.service.WebContentService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebTools {
    private final WebContentService webContentService;

    @Tool(name = "web_fetch", description = "Tool to fetch web content by provided URL")
    public String fetch(String url) {
        return webContentService.fetchWebContent(url);
    }

    @Tool(name = "web_search", description = "Tool to search web content by provided query")
    public String search(String query) {
        return webContentService.searchAndFetch(query);
    }
}
