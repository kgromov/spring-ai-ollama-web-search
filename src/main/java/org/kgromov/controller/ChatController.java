package org.kgromov.controller;

import org.kgromov.service.OllamaChatService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@AllArgsConstructor
public class ChatController {

    private final OllamaChatService chatService;

    @GetMapping
    public String chat(@RequestParam(defaultValue = "Hello, how are you?") String message) {
        return chatService.reply(message);
    }
}

