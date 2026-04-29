package org.kgromov.tools;

import lombok.RequiredArgsConstructor;
import org.kgromov.service.WebContentService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebTools {
    private final WebContentService webContentService;

    @Tool(name = "web_fetch", description = "Tool to fetch web content by provided URL")
    public String fetch(@ToolParam(description = "The URL to fetch content from") String url) {
        return webContentService.fetchWebContent(url);
    }

    @Tool(name = "web_search", description = "Tool to search web content by provided query")
    public String search(@ToolParam(description = "The user query to search for") String query) {
        return webContentService.searchWebContent(query);
    }
}
