package com.MicroGuardAI.architectureservice.service;

import com.MicroGuardAI.architectureservice.dto.CreateArchitectureRequest;
import com.MicroGuardAI.architectureservice.dto.ArchitectureResponse;
import com.MicroGuardAI.architectureservice.models.Architecture;
import com.MicroGuardAI.architectureservice.repository.ArchitectureRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ArchitectureService {

    private final ArchitectureRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public ArchitectureResponse create(String userId, CreateArchitectureRequest req){

        Architecture a = new Architecture();
        a.setUserId(userId);

        // ✅ FIX START
        String projectId = java.util.UUID.randomUUID().toString();
        a.setProjectId(projectId);

        String architectureJson;
        try {
            architectureJson = new com.fasterxml.jackson.databind.ObjectMapper()
                    .writeValueAsString(req);
        } catch (Exception e) {
            throw new RuntimeException("Error converting request to JSON");
        }

        a.setArchitectureJson(architectureJson);
        // ✅ FIX END

        a.setCreatedAt(LocalDateTime.now());

        Architecture saved = repository.save(a);

        String event = saved.getProjectId() + "|" + saved.getUserId() + "|" + saved.getArchitectureJson();
        kafkaTemplate.send("ARCHITECTURE_SUBMITTED", event);

        ArchitectureResponse res = new ArchitectureResponse();
        res.setId(saved.getId());
        res.setProjectId(saved.getProjectId());
        res.setArchitectureJson(saved.getArchitectureJson());
        res.setCreatedAt(saved.getCreatedAt());

        return res;
    }
}