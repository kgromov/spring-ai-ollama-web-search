package org.kgromov.service;


import lombok.AllArgsConstructor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OllamaChatService {

    private final OllamaChatModel chatModel;

    public String reply(String prompt) {
        SystemMessage systemMessage = new SystemMessage("You respond concisely.");
        UserMessage userMessage = new UserMessage(prompt);
        Prompt chatPrompt = new Prompt(List.of(systemMessage, userMessage));
        ChatResponse response = chatModel.call(chatPrompt);
        return response.getResult().getOutput().getText();
    }
}

