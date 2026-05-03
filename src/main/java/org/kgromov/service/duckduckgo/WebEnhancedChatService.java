package org.kgromov.service.duckduckgo;

import lombok.RequiredArgsConstructor;
import org.kgromov.service.WebSearchService;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Profile("direct")
@Service
@RequiredArgsConstructor
public class WebEnhancedChatService implements WebSearchService {

    private final OllamaChatModel chatModel;
    private final JsoupWebContentService jsoupWebContentService;

    @Override
    public String fetch(String userQuery, String url) {
        String webContent = jsoupWebContentService.fetchWebContent(url);
        
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

    @Override
    public String search(String query) {
        String searchResults = jsoupWebContentService.searchWebContent(query);
        
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

