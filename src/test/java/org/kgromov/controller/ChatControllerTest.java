package org.kgromov.controller;

import org.kgromov.service.OllamaChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OllamaChatService chatService;

    @Test
    void testChatEndpointWithMessage() throws Exception {
        String testMessage = "What is 2 plus 2?";
        String expectedResponse = "2 plus 2 equals 4.";
        
        when(chatService.reply(testMessage)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/chat")
                        .param("message", testMessage))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    void testChatEndpointWithDefaultMessage() throws Exception {
        String defaultMessage = "Hello, how are you?";
        String expectedResponse = "I'm doing well, thank you!";
        
        when(chatService.reply(defaultMessage)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/chat"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    void testChatEndpointWithCodingQuestion() throws Exception {
        String testMessage = "Write hello world in Python";
        String expectedResponse = "print(\"Hello, World!\")";
        
        when(chatService.reply(testMessage)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/chat")
                        .param("message", testMessage))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    void testChatEndpointWithEmptyMessage() throws Exception {
        String emptyMessage = "";
        String expectedResponse = "Please provide a message.";
        
        when(chatService.reply(anyString())).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/chat")
                        .param("message", emptyMessage))
                .andExpect(status().isOk());
    }
}

