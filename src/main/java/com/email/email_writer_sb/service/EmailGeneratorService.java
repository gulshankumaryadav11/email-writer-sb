package com.email.email_writer_sb.service;

import com.email.email_writer_sb.EmailRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import jakarta.annotation.PostConstruct;
import java.util.Map;

@Service
public class EmailGeneratorService {

    private final WebClient webClient;
    private final String apiKey;

    public EmailGeneratorService(
            @Value("${groq.api.key}") String apiKey
    ) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.groq.com/openai/v1")
                .build();
        this.apiKey = apiKey;
    }

    // 🔥 CHECK API KEY LOAD
    @PostConstruct
    public void checkKey() {
        System.out.println("GROQ API KEY: " + apiKey);
    }

    public String generateEmailReply(EmailRequest request) {

        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("Groq API key missing ❌");
        }

        String prompt = buildPrompt(request);

        Map<String, Object> body = Map.of(
                "model", "llama-3.1-8b-instant",
                "messages", new Object[]{
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                }
        );

        try {

            String response = webClient.post()
                    .uri("/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return extractContent(response);

        } catch (WebClientResponseException e) {
            System.out.println("Groq API Error: " + e.getResponseBodyAsString());
            throw new RuntimeException("Groq API failed ❌: " + e.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong ❌", e);
        }
    }

    private String buildPrompt(EmailRequest r) {
        return """
You are a professional email reply generator.

STRICT RULES:
- Generate ONLY ONE final email reply
- No options
- No explanations
- Only email text

Tone: %s

Original Email:
%s

Additional Instructions:
%s
""".formatted(
                safe(r.getTone()),
                safe(r.getEmailContent()),
                safe(r.getInstructions())
        );
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private String extractContent(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            return root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText()
                    .trim();

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Groq response ❌", e);
        }
    }
}