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
public class WebEnhancedChatService {

    private final OllamaChatModel chatModel;
    private final WebContentService webContentService;

    public String replyWithWebContext(String userQuery, String url) {
        String webContent = webContentService.fetchWebContent(url);
        
        SystemMessage systemMessage = new SystemMessage(
            "You are a helpful assistant. Use the provided web content to answer the user's question. " +
            "If the web content doesn't contain relevant information, say so and provide a general answer."
        );
        
        String enhancedQuery = String.format(
            "Web Content:\n%s\n\nUser Question: %s",
            webContent, userQuery
        );
        
        UserMessage userMessage = new UserMessage(enhancedQuery);
        Prompt chatPrompt = new Prompt(List.of(systemMessage, userMessage));
        ChatResponse response = chatModel.call(chatPrompt);
        return response.getResult().getOutput().getText();
    }

    public String replyWithWebSearch(String query) {
        String searchResults = webContentService.searchAndFetch(query);
        
        SystemMessage systemMessage = new SystemMessage(
            "You are a helpful assistant. Use the provided search results to answer the user's question. " +
            "Synthesize information from multiple sources if available. Cite sources when relevant."
        );
        
        String enhancedQuery = String.format(
            "%s\n\nBased on the above search results, please answer: %s",
            searchResults, query
        );
        
        UserMessage userMessage = new UserMessage(enhancedQuery);
        Prompt chatPrompt = new Prompt(List.of(systemMessage, userMessage));
        ChatResponse response = chatModel.call(chatPrompt);
        return response.getResult().getOutput().getText();
    }
}

