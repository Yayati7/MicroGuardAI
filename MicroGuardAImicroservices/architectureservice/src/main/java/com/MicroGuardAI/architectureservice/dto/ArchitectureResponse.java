package com.MicroGuardAI.architectureservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArchitectureResponse {

    private String id;

    private String projectId;

    private String architectureJson;

    private LocalDateTime createdAt;
}