package org.kgromov.controller;

import org.kgromov.service.WebEnhancedChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebChatController.class)
class WebChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WebEnhancedChatService webEnhancedChatService;

    @Test
    void testChatWithUrl() throws Exception {
        String message = "What is this page about?";
        String url = "https://example.com";
        String expectedResponse = "This page is about domain examples.";
        
        when(webEnhancedChatService.replyWithWebContext(message, url))
                .thenReturn(expectedResponse);

        mockMvc.perform(get("/api/web-chat/with-url")
                        .param("message", message)
                        .param("url", url))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    void testChatWithSearch() throws Exception {
        String query = "What is Spring Boot?";
        String expectedResponse = "Spring Boot is a framework for building Java applications.";
        
        when(webEnhancedChatService.replyWithWebSearch(query))
                .thenReturn(expectedResponse);

        mockMvc.perform(get("/api/web-chat/with-search")
                        .param("query", query))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }
}

