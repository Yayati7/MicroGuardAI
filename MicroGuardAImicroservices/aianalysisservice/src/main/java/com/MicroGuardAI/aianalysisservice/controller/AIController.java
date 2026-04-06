package com.MicroGuardAI.aianalysisservice.controller;

import com.MicroGuardAI.aianalysisservice.models.AIAnalysis;
import com.MicroGuardAI.aianalysisservice.repository.AIAnalysisRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analysis")
@AllArgsConstructor
public class AIController {

    private final AIAnalysisRepository repository;

    @GetMapping("/user/{userId}")
    public List<AIAnalysis> getUserHistory(@PathVariable String userId){
        return repository.findByUserId(userId);
    }

    @GetMapping("/{projectId}")
    public List<AIAnalysis> getByProjectId(@PathVariable String projectId){
        return repository.findByProjectId(projectId);
    }
}