package org.example;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Your API keys here
        String googleApiKey = "";
        String openAIApiKey = "";
        String grolkApiKey = "";
        String deepSeekApiKey = "";

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
