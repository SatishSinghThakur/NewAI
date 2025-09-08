package org.example;

import java.net.http.*;
import java.net.URI;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DeepSeekClient implements AIPlatformClient {
    private final String apiKey;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public DeepSeekClient(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String getName() {
        return "DeepSeek";
    }

    @Override
    public String getResponse(String question) throws Exception {
        // Replace with actual DeepSeek API endpoint and payload!
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.deepseek.com/v1/chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"model\":\"deepseek-chat\",\"messages\":[{\"role\":\"user\",\"content\":\"" + question + "\"}]}"
                ))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String json = response.body();

        JsonNode root = mapper.readTree(json);
        if (root.has("error")) {
            return "Error: " + root.path("error").toString();
        }
        JsonNode choices = root.path("choices");
        if (choices.isArray() && choices.size() > 0) {
            JsonNode message = choices.get(0).path("message");
            if (message.has("content")) {
                return message.path("content").asText();
            } else {
                return "Error: No 'content' field in DeepSeek response. Full response: " + json;
            }
        } else {
            return "Error: No choices in DeepSeek response. Full response: " + json;
        }
    }
}