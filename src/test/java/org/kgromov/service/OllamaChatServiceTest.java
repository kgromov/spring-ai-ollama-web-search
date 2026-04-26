package org.kgromov.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OllamaChatServiceTest {

    @Mock
    private OllamaChatModel chatModel;

    private OllamaChatService chatService;

    @BeforeEach
    void setUp() {
        chatService = new OllamaChatService(chatModel);
    }

    @Test
    void testReplyReturnsValidResponse() {
        String userPrompt = "What is 2 plus 2?";
        String expectedResponse = "2 plus 2 equals 4.";
        
        AssistantMessage assistantMessage = new AssistantMessage(expectedResponse);
        Generation generation = new Generation(assistantMessage);
        ChatResponse chatResponse = new ChatResponse(java.util.List.of(generation));
        
        when(chatModel.call(any(Prompt.class))).thenReturn(chatResponse);

        String actualResponse = chatService.reply(userPrompt);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testReplyWithCodingQuestion() {
        String userPrompt = "Write hello world in Python";
        String expectedResponse = "print(\"Hello, World!\")";
        
        AssistantMessage assistantMessage = new AssistantMessage(expectedResponse);
        Generation generation = new Generation(assistantMessage);
        ChatResponse chatResponse = new ChatResponse(java.util.List.of(generation));
        
        when(chatModel.call(any(Prompt.class))).thenReturn(chatResponse);

        String actualResponse = chatService.reply(userPrompt);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testReplyWithGreeting() {
        String userPrompt = "Hello";
        String expectedResponse = "Hello! How can I help you today?";
        
        AssistantMessage assistantMessage = new AssistantMessage(expectedResponse);
        Generation generation = new Generation(assistantMessage);
        ChatResponse chatResponse = new ChatResponse(java.util.List.of(generation));
        
        when(chatModel.call(any(Prompt.class))).thenReturn(chatResponse);

        String actualResponse = chatService.reply(userPrompt);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
    }
}

