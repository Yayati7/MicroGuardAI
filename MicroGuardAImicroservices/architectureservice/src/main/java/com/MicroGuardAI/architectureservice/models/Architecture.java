package com.MicroGuardAI.architectureservice.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "architectures")
@Data
public class Architecture {

    @Id
    private String id;

    private String userId;

    private String projectId;

    private String architectureJson;

    private LocalDateTime createdAt;
}