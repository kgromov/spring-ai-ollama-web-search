package org.kgromov.service;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OllamaWebSearchService {
    private final OllamaChatModel chatModel;

    public String fetch(String query, String url) {
        return ChatClient.builder(chatModel)
                .defaultSystem("""
                        You are a helpful assistant.
                        You MUST immediately call the web_fetch tool with the provided URL without asking any questions.
                        After fetching, answer the user's question based on the content.
                        If the content is not relevant, provide a general answer.
                        """)
                .build()
                .prompt()
                .user("Call web_fetch with url=\"%s\" and then answer: %s".formatted(url, query))
                .call()
                .content();
    }

    public String search(String query) {
        return ChatClient.builder(chatModel)
                .build()
                .prompt()
                .user(query)
                .call()
                .content();
    }

    public String search_with_prompt(String query) {
        return ChatClient.builder(chatModel)
                .defaultSystem("""
                        You are a helpful assistant.
                        You MUST immediately call the web_search tool by provided user query without asking any questions.
                        Use the provided search results to answer the user's question.
                        Synthesize information from multiple sources if available. Cite sources when relevant
                        """)
                .build()
                .prompt()
                .user("Call web_search ti answer: %s".formatted(query))
                .call()
                .content();
    }
}

