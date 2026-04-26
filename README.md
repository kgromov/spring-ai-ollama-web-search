# Spring AI Ollama Sample

Spring Boot + Ollama AI with web scraping capabilities. Chat with AI that can fetch and analyze web content in real-time.

## Quick Start

### Prerequisites

- Java 21+ | Maven 3.6+ | [Ollama](https://ollama.ai) 

```bash
# Install and start Ollama
ollama pull qwen3-coder:30b

# Run the application
mvn spring-boot:run
```

Application starts at **http://localhost:8080**

## API Endpoints

### 1. Basic Chat

```bash
curl "http://localhost:8080/api/chat?message=Hello"
```

### 2. Analyze Webpage

```bash
curl -G "http://localhost:8080/api/web-chat/with-url" \
  --data-urlencode "message=Summarize this page" \
  --data-urlencode "url=https://example.com"
```

### 3. Web Search + AI Answer

```bash
curl -G "http://localhost:8080/api/web-chat/with-search" \
  --data-urlencode "query=What is Spring Boot?"
```
