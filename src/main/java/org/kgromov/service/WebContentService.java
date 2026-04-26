package org.kgromov.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class WebContentService {

    private static final int TIMEOUT = 10000;
    private static final int MAX_CONTENT_LENGTH = 2000;

    public String fetchWebContent(String url) {
        try {
            log.info("Fetching content from URL: {}", url);
            Document doc = Jsoup.connect(url)
                    .timeout(TIMEOUT)
                    .userAgent("Mozilla/5.0 (compatible; SpringAI-Bot/1.0)")
                    .get();

            String title = doc.title();
            String bodyText = doc.body().text();

            if (bodyText.length() > MAX_CONTENT_LENGTH) {
                bodyText = bodyText.substring(0, MAX_CONTENT_LENGTH) + "...";
            }

            return String.format("Title: %s\n\nContent: %s", title, bodyText);
        } catch (IOException e) {
            log.error("Failed to fetch content from URL: {}", url, e);
            return "Error: Unable to fetch content from the provided URL.";
        }
    }

    public String searchAndFetch(String query) {
        String searchUrl = "https://html.duckduckgo.com/html/?q=" + 
                          query.replace(" ", "+");
        
        try {
            log.info("Searching for: {}", query);
            Document doc = Jsoup.connect(searchUrl)
                    .timeout(TIMEOUT)
                    .userAgent("Mozilla/5.0 (compatible; SpringAI-Bot/1.0)")
                    .get();

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
}

