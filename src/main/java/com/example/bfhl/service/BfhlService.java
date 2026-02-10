package com.example.bfhl.service;

import com.example.bfhl.util.MathUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BfhlService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public Object process(Map<String, Object> body) {
        String key = body.keySet().iterator().next();

        switch (key) {

            case "fibonacci":
                if (!(body.get(key) instanceof Integer)) {
                    throw new IllegalArgumentException("Invalid fibonacci input");
                }
                return MathUtil.fibonacci((Integer) body.get(key));

            case "prime":
                return ((List<?>) body.get(key))
                        .stream()
                        .map(v -> (Integer) v)
                        .filter(MathUtil::isPrime)
                        .collect(Collectors.toList());

            case "lcm":
                return ((List<?>) body.get(key))
                        .stream()
                        .map(v -> (Integer) v)
                        .reduce(MathUtil::lcm)
                        .orElse(0);

            case "hcf":
                return ((List<?>) body.get(key))
                        .stream()
                        .map(v -> (Integer) v)
                        .reduce(MathUtil::gcd)
                        .orElse(0);

            case "AI":
                if (!(body.get(key) instanceof String)) {
                    throw new IllegalArgumentException("Invalid AI input");
                }
                return callGemini((String) body.get(key));

            default:
                throw new IllegalArgumentException("Invalid key");
        }
    }

    private String callGemini(String question) {
        RestTemplate restTemplate = new RestTemplate();

        String url =
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key="
                        + geminiApiKey;

        Map<String, Object> payload = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", question)
                                )
                        )
                )
        );

        Map<?, ?> response = restTemplate.postForObject(url, payload, Map.class);

        // SAFE extraction
        List<?> candidates = (List<?>) response.get("candidates");
        Map<?, ?> candidate = (Map<?, ?>) candidates.get(0);
        Map<?, ?> content = (Map<?, ?>) candidate.get("content");
        List<?> parts = (List<?>) content.get("parts");
        Map<?, ?> part = (Map<?, ?>) parts.get(0);

        String text = part.get("text").toString();

        // Return single-word response
        return text.split("\\s+")[0];
    }
}
