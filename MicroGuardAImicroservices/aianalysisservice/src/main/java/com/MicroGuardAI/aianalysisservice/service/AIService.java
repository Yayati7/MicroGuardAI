package com.MicroGuardAI.aianalysisservice.service;

import com.MicroGuardAI.aianalysisservice.models.AIAnalysis;
import com.MicroGuardAI.aianalysisservice.repository.AIAnalysisRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.*;
import org.springframework.http.HttpMethod;  // ✅ Added import

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIService {

    private final AIAnalysisRepository repository;

    @Value("${mistral.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    @KafkaListener(topics = "ARCHITECTURE_SUBMITTED", groupId = "ai-group")
    public void consume(String message){

        String projectId = "UNKNOWN";
        String userId = "UNKNOWN";
        String architectureJson = "UNKNOWN";

        try {
            log.info("Received architecture for AI analysis");

            String[] partsMsg = message.split("\\|", 3);

            projectId = partsMsg[0];
            userId = partsMsg[1];
            architectureJson = partsMsg[2];

            String prompt = """
        You are a senior system architect.

        Analyze this microservices architecture JSON:

        %s

        Provide:
        1. Architecture score (0-100)
        2. Scalability risks
        3. Security issues
        4. Improvements
        """.formatted(architectureJson);

            // 🔥 MISTRAL API
            String url = "https://api.mistral.ai/v1/chat/completions";

            Map<String, Object> request = Map.of(
                    "model", "mistral-small",
                    "messages", List.of(
                            Map.of("role", "user", "content", prompt)
                    )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            log.info("Sending request to Mistral API");

            ResponseEntity<Map> responseEntity =
                    restTemplate.postForEntity(url, entity, Map.class);

            Map responseMap = responseEntity.getBody();

            String response = "AI analysis failed";

            // 🔥 MISTRAL RESPONSE PARSE
            try {
                List choices = (List) responseMap.get("choices");
                Map firstChoice = (Map) choices.get(0);
                Map messageObj = (Map) firstChoice.get("message");

                response = (String) messageObj.get("content");

            } catch (Exception e) {
                log.error("Error parsing Mistral response", e);
                response = "Error parsing AI response";
            }

            // save result
            AIAnalysis analysis = new AIAnalysis();
            analysis.setProjectId(projectId);
            analysis.setUserId(userId);
            analysis.setArchitectureJson(architectureJson);
            analysis.setResult(response);
            analysis.setCreatedAt(LocalDateTime.now());

            repository.save(analysis);

            // ✅ Added snippet from image
            try {
                String updateUrl = "http://localhost:8081/api/projects/update-result";
                Map<String, String> updateBody = Map.of(
                        "projectId", projectId,
                        "aiResult", response
                );
                HttpHeaders updateHeaders = new HttpHeaders();
                updateHeaders.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<Map<String, String>> updateEntity = new HttpEntity<>(updateBody, updateHeaders);
                restTemplate.exchange(updateUrl, HttpMethod.PUT, updateEntity, Void.class);
                log.info("AI result written back to user-service");
            } catch (Exception e) {
                log.error("Failed to write AI result back to user-service", e);
            }

            log.info("AI analysis saved successfully");

        } catch (Exception e) {

            log.error("FULL AI FLOW FAILED", e);

            AIAnalysis fallback = new AIAnalysis();
            fallback.setProjectId(projectId);
            fallback.setUserId(userId);
            fallback.setArchitectureJson(architectureJson);
            fallback.setResult("AI processing failed: " + e.getMessage());
            fallback.setCreatedAt(LocalDateTime.now());

            repository.save(fallback);
        }
    }
}
