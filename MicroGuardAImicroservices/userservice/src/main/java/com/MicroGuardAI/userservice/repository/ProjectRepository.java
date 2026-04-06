package com.MicroGuardAI.userservice.repository;

import com.MicroGuardAI.userservice.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project,String> {

    List<Project> findByUserId(String userId);

    Optional<Project> findByProjectId(String projectId);
}
