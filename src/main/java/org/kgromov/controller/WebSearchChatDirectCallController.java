package org.kgromov.controller;

import lombok.AllArgsConstructor;
import org.kgromov.service.WebEnhancedChatService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile("!cloud")
@RestController
@RequestMapping("/api/web-chat")
@AllArgsConstructor
public class WebSearchChatDirectCallController {

    private final WebEnhancedChatService webEnhancedChatService;

    @GetMapping("/fetch")
    public String chatWithUrl(
            @RequestParam String message,
            @RequestParam String url) {
        return webEnhancedChatService.replyWithWebContext(message, url);
    }

    @GetMapping("/search")
    public String chatWithSearch(@RequestParam String query) {
        return webEnhancedChatService.replyWithWebSearch(query);
    }
}

