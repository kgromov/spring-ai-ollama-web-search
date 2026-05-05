package org.kgromov.service;

public interface WebSearchService {

    String fetch(String query, String url);

    String search(String query);
}
