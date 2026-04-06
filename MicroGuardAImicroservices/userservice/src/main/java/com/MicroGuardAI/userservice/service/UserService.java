package com.MicroGuardAI.userservice.service;

import com.MicroGuardAI.userservice.dto.CreateProjectRequest;
import com.MicroGuardAI.userservice.dto.ProjectResponse;
import com.MicroGuardAI.userservice.models.Project;
import com.MicroGuardAI.userservice.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final ProjectRepository projectRepository;

    public ProjectResponse createProject(String userId, CreateProjectRequest req) {
        Project p = new Project();
        p.setUserId(userId);
        p.setProjectId(req.getProjectId());         // ✅
        p.setProjectName(req.getProjectName());
        p.setArchitectureJson(req.getArchitectureJson());
        p.setCreatedAt(LocalDateTime.now());

        Project saved = projectRepository.save(p);
        return toResponse(saved);
    }

    // ✅ NEW — called by AI service after analysis completes
    public void updateAiResult(String projectId, String aiResult) {
        projectRepository.findByProjectId(projectId).ifPresent(p -> {
            p.setAiResult(aiResult);
            projectRepository.save(p);
        });
    }

    public List<ProjectResponse> getProjects(String userId) {
        return projectRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ProjectResponse toResponse(Project p) {
        ProjectResponse r = new ProjectResponse();
        r.setId(p.getId());
        r.setProjectId(p.getProjectId());           // ✅
        r.setProjectName(p.getProjectName());
        r.setAiResult(p.getAiResult());
        r.setCreatedAt(p.getCreatedAt());
        return r;
    }
}