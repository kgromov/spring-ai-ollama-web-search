package org.kgromov.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.kgromov.config.WebSearchConfig;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Primary
@Profile("!cloud")
@Service
@Slf4j
@RequiredArgsConstructor
public class JsoupWebContentService implements WebContentService {
    private final WebSearchConfig webSearchConfig;

    public String fetchWebContent(String url) {
        try {
            log.info("Fetching content from URL: {}", url);
            Document doc = this.fetchPageContent(url);
            String title = doc.title();
            String bodyText = doc.body().text();
            if (bodyText.length() > webSearchConfig.maxContentLength()) {
                bodyText = bodyText.substring(0, webSearchConfig.maxContentLength()) + "...";
            }
            return String.format("Title: %s\n\nContent: %s", title, bodyText);
        } catch (IOException e) {
            log.error("Failed to fetch content from URL: {}", url, e);
            return "Error: Unable to fetch content from the provided URL.";
        }
    }

    public String searchWebContent(String query) {
        String searchUrl = webSearchConfig.engineUrl() + query.replace(" ", "+");
        try {
            log.info("Searching for: {}", query);
            Document doc = this.fetchPageContent(searchUrl);
            StringBuilder results = new StringBuilder();
            results.append("Search results for: ").append(query).append("\n\n");
            doc.select("div.result").stream()
                    .limit(3)
                    .forEach(result -> {
                        String title = result.select("a.result__a").text();
                        String snippet = result.select("a.result__snippet").text();
                        String link = result.select("a.result__url").attr("href");
                        
                        results.append("Title: ").append(title).append("\n");
                        results.append("Summary: ").append(snippet).append("\n");
                        results.append("URL: ").append(link).append("\n\n");
                    });
            return results.toString();
        } catch (Exception e) {
            log.error("Failed to perform search for query: {}", query, e);
            return "Error: Unable to perform web search.";
        }
    }

    private Document fetchPageContent(String url) throws IOException {
        return Jsoup.connect(url)
                .timeout(webSearchConfig.timeout())
                .userAgent("Mozilla/5.0 (compatible; SpringAI-Bot/1.0)")
                .get();
    }
}

