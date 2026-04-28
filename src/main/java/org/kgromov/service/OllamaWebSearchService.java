package org.kgromov.service;


import lombok.AllArgsConstructor;
import org.kgromov.tools.WebTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OllamaWebSearchService {
    private final OllamaChatModel chatModel;
    private final WebTools webTools;

    public String fetch(String query, String url) {
        return ChatClient.builder(chatModel)
//                .defaultToolNames("web_fetch")
                .defaultToolCallbacks(ToolCallbacks.from(webTools))
                .defaultSystem("""
                                You are a helpful assistant to fetch web content by provided {{url}} to answer the user's question.
                                If the web content doesn't contain relevant information, say so and provide a general answer
                                """)
                .defaultUser("Based on fetched web content, please answer {{query}}")
                .build()
                .prompt()
                .system(sp -> sp.param("url", url))
                .user(up -> up.param("query", query))
                .call()
                .content();
    }

    public String search(String query) {
        return ChatClient.builder(chatModel)
//                .defaultToolNames("web_search")
                .defaultToolCallbacks(ToolCallbacks.from(webTools))
                .defaultSystem("""
                                You are a helpful assistant. Use the provided {{search_results}} to answer the user's question.
                                Synthesize information from multiple sources if available. Cite sources when relevant
                                """)
                .defaultUser("Based on the above search results, please answer {{query}}")
                .build()
                .prompt()
                .user(up -> up.param("query", query))
                .call()
                .content();
    }
}

