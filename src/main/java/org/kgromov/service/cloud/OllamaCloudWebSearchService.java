package org.kgromov.service.cloud;

import lombok.RequiredArgsConstructor;
import org.kgromov.service.WebSearchService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("cloud")
@Service
@RequiredArgsConstructor
public class OllamaCloudWebSearchService implements WebSearchService {
    private final OllamaChatModel ollamaCloudChatModel;

    public String fetch(String query, String url) {
        return ChatClient.builder(ollamaCloudChatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
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
        return ChatClient.builder(ollamaCloudChatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build()
                .prompt()
                .user(query)
                .call()
                .content();
    }

    public String search_with_prompt(String query) {
        return ChatClient.builder(ollamaCloudChatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
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

