package com.MicroGuardAI.aianalysisservice.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "ai_analysis")
@Data
public class AIAnalysis {

    @Id
    private String id;

    private String projectId;
    private String userId;

    private String architectureJson;

    private String result;

    private LocalDateTime createdAt;
}