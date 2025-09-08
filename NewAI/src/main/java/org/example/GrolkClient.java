package org.example;

import java.net.http.*;
import java.net.URI;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GrolkClient implements AIPlatformClient {
    private final String apiKey;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public GrolkClient(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String getName() {
        return "Grolk";
    }

    @Override
    public String getResponse(String question) throws Exception {
        // Replace with actual Grolk API endpoint and payload
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.grolk.ai/v1/generate"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"prompt\":\"" + question + "\"}"
                ))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String json = response.body();

        JsonNode root = mapper.readTree(json);
        if (root.has("error")) {
            return "Error: " + root.path("error").toString();
        }
        if (root.has("result")) {
            return root.path("result").asText();
        } else {
            return "Error: No 'result' field in Grolk response. Full response: " + json;
        }
    }
}