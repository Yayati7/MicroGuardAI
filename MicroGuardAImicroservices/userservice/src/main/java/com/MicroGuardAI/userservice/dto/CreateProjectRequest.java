package com.MicroGuardAI.userservice.dto;

import lombok.Data;

@Data
public class CreateProjectRequest {

    private String projectId;
    private String projectName;

    private String architectureJson;
}