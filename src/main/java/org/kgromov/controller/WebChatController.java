package org.kgromov.controller;

import org.kgromov.service.WebEnhancedChatService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/web-chat")
@AllArgsConstructor
public class WebChatController {

    private final WebEnhancedChatService webEnhancedChatService;

    @GetMapping("/with-url")
    public String chatWithUrl(
            @RequestParam String message,
            @RequestParam String url) {
        return webEnhancedChatService.replyWithWebContext(message, url);
    }

    @GetMapping("/with-search")
    public String chatWithSearch(@RequestParam String query) {
        return webEnhancedChatService.replyWithWebSearch(query);
    }
}

