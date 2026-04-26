package org.kgromov.controller;

import org.junit.jupiter.api.Test;
import org.kgromov.config.WebSearchConfig;
import org.kgromov.service.WebEnhancedChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebSearchChatController.class)
class WebSearchChatControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebSearchConfig webSearchConfig;
    @MockitoBean
    private WebEnhancedChatService webEnhancedChatService;


    @Test
    void testChatWithUrl() throws Exception {
        String message = "What is this page about?";
        String url = "https://example.com";
        String expectedResponse = "This page is about domain examples.";

        when(webEnhancedChatService.replyWithWebContext(message, url))
                .thenReturn(expectedResponse);

        mockMvc.perform(get("/api/web-chat/fetch")
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

        mockMvc.perform(get("/api/web-chat/search")
                        .param("query", query))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }


    @TestConfiguration
    @EnableConfigurationProperties(WebSearchConfig.class)
    static class TestWebConfig {

    }
}

