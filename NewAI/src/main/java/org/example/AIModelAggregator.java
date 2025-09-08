package org.example;

import java.util.*;

public class AIModelAggregator {
    private final List<AIPlatformClient> clients;

    public AIModelAggregator(List<AIPlatformClient> clients) {
        this.clients = clients;
    }

    public String getBestResponse(String question) {
        Map<String, String> results = new LinkedHashMap<>();
        for (AIPlatformClient client : clients) {
            try {
                String output = client.getResponse(question);
                results.put(client.getName(), output);
            } catch (Exception e) {
                results.put(client.getName(), "Error: " + e.getMessage());
            }
        }

        System.out.println("=== OUTPUTS ===");
        results.forEach((k, v) -> System.out.println("[" + k + "]: " + v));

        // Simple merge: choose the longest response
        String bestResponse = results.values().stream()
                .max(Comparator.comparingInt(String::length))
                .orElse("No valid responses.");

        System.out.println("\n=== MERGED BEST RESPONSE ===");
        System.out.println(bestResponse);

        return bestResponse;
    }
}