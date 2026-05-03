package org.kgromov.controller;

import org.kgromov.service.localTools.OllamaLocalWebSearchService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/web-chat")
@AllArgsConstructor
public class WebSearchChatController {

    private final OllamaLocalWebSearchService webSearchService;

    @GetMapping("/fetch")
    public String chatWithUrl(
            @RequestParam String message,
            @RequestParam String url) {
        return webSearchService.fetch(message, url);
    }

    @GetMapping("/search")
    public String chatWithSearch(@RequestParam String query) {
        return webSearchService.search(query);
    }
}

