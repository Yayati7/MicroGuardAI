package com.MicroGuardAI.aianalysisservice.repository;

import com.MicroGuardAI.aianalysisservice.models.AIAnalysis;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AIAnalysisRepository extends MongoRepository<AIAnalysis,String> {
    List<AIAnalysis> findByProjectId(String projectId);

    List<AIAnalysis> findByUserId(String userId);
}