package com.MicroGuardAI.userservice.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;

    private String projectId;   // ✅ NEW — links to architecture-service UUID

    private String projectName;

    @Column(length = 5000)
    private String architectureJson;

    @Column(length = 10000)     // ✅ bigger for AI result
    private String aiResult;

    @CreationTimestamp
    private LocalDateTime createdAt;
}