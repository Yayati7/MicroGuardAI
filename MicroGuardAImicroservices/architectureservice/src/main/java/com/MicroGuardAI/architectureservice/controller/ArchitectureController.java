package com.MicroGuardAI.architectureservice.controller;

import com.MicroGuardAI.architectureservice.dto.CreateArchitectureRequest;
import com.MicroGuardAI.architectureservice.dto.ArchitectureResponse;
import com.MicroGuardAI.architectureservice.service.ArchitectureService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/architecture")
@AllArgsConstructor
public class ArchitectureController {

    private final ArchitectureService service;

    @PostMapping
    public ArchitectureResponse create(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody CreateArchitectureRequest request
    ){
        String userId = jwt.getSubject();
        return service.create(userId, request);
    }
}