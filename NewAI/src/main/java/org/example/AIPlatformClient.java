package org.example;


public interface AIPlatformClient {
    String getName();
    String getResponse(String question) throws Exception;
}
