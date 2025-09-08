package org.example;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Your API keys here
        String googleApiKey = "a958f6ca10ad4117bddde45c80fcf483";
        String openAIApiKey = "sk-ijklmnopqrstuvwxijklmnopqrstuvwxijklmnop";
        String grolkApiKey = "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";
        String deepSeekApiKey = "sk-b748bb5512da4943bda649941c684cb5";

        List<AIPlatformClient> clients = Arrays.asList(
                new GoogleGeminiClient(googleApiKey),
                new OpenAIClient(openAIApiKey),
                new GrolkClient(grolkApiKey),
                new DeepSeekClient(deepSeekApiKey)
        );

        AIModelAggregator aggregator = new AIModelAggregator(clients);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Ask a question:");
        String question = scanner.nextLine();

        String bestResponse = aggregator.getBestResponse(question);
        System.out.println("\nFinal Response:\n" + bestResponse);
    }
}