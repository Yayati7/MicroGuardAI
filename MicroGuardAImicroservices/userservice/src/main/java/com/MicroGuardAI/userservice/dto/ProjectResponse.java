package com.MicroGuardAI.userservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectResponse {

    private String id;

    private String projectName;
    private String projectId;

    private String aiResult;

    private LocalDateTime createdAt;
}