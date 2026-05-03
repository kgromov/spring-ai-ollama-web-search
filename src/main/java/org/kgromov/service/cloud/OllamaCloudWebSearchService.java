package org.kgromov.service.cloud;

import org.kgromov.service.WebSearchService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Profile("cloud")
@Service
public class OllamaCloudWebSearchService implements WebSearchService {
    private final ChatModel chatModel;

    public OllamaCloudWebSearchService(List<ToolCallback> webSearchTools) {
        this.chatModel = this.chatModel(webSearchTools);
    }

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

    private ChatModel chatModel(List<ToolCallback> webSearchTools) {
        var options = OllamaChatOptions.builder()
                .model("minimax-m2.7:cloud")
                .temperature(0.7)
                .toolCallbacks(webSearchTools)
                .build();
        return OllamaChatModel.builder()
                .ollamaApi(OllamaApi.builder().build())
                .defaultOptions(options)
                .build();
    }
}

