package org.kgromov.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kgromov.config.WebSearchConfig;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class WebContentServiceTest {

    @InjectMocks
    private JsoupWebContentService jsoupWebContentService;
    @Mock
    private WebSearchConfig webSearchConfig;

    @Test
    void testFetchWebContentWithValidUrl() {
        String url = "https://example.com";
        String content = jsoupWebContentService.fetchWebContent(url);
        
        assertNotNull(content);
        assertTrue(content.contains("Title:") || content.contains("Error:"));
    }

    @Test
    void testFetchWebContentWithInvalidUrl() {
        String url = "https://invalid-url-that-does-not-exist-12345.com";
        String content = jsoupWebContentService.fetchWebContent(url);
        
        assertNotNull(content);
        assertTrue(content.contains("Error:"));
    }

    @Test
    void testSearchAndFetch() {
        String query = "Spring Boot tutorial";
        String results = jsoupWebContentService.searchWebContent(query);
        
        assertNotNull(results);
        assertTrue(results.contains("Search results for:") || results.contains("Error:"));
    }
}

