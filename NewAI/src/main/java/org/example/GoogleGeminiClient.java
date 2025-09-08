package org.example;

import java.net.http.*;
import java.net.URI;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GoogleGeminiClient implements AIPlatformClient {
    private final String apiKey;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public GoogleGeminiClient(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String getName() {
        return "Google Gemini";
    }

    @Override
    public String getResponse(String question) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + apiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"contents\":[{\"parts\":[{\"text\":\"" + question + "\"}]}]}"
                ))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String json = response.body();

        JsonNode root = mapper.readTree(json);
        if (root.has("error")) {
            return "Error: " + root.path("error").toString();
        }
        JsonNode candidates = root.path("candidates");
        if (candidates.isArray() && candidates.size() > 0) {
            JsonNode parts = candidates.get(0).path("content").path("parts");
            if (parts.isArray() && parts.size() > 0 && parts.get(0).has("text")) {
                return parts.get(0).path("text").asText();
            } else {
                return "Error: No 'text' in Gemini response. Full response: " + json;
            }
        } else {
            return "Error: No candidates in Gemini response. Full response: " + json;
        }
    }
}