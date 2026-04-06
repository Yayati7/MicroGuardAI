package com.MicroGuardAI.architectureservice.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateArchitectureRequest {

    private String name;
    private List<String> services;
    private String database;
    private String communication;
}