package org.example;

import java.net.http.*;
import java.net.URI;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class OpenAIClient implements AIPlatformClient {
    private final String apiKey;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public OpenAIClient(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String getName() {
        return "OpenAI ChatGPT";
    }

    @Override
    public String getResponse(String question) throws Exception {
        ObjectNode payload = mapper.createObjectNode();
        payload.put("model", "gpt-3.5-turbo");
        payload.putArray("messages").addObject().put("role", "user").put("content", question);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
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
                return "Error: No 'content' field in OpenAI response. Full response: " + json;
            }
        } else {
            return "Error: No choices in OpenAI response. Full response: " + json;
        }
    }
}