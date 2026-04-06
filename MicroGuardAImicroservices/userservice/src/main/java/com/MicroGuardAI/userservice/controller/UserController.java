package com.MicroGuardAI.userservice.controller;

import com.MicroGuardAI.userservice.dto.CreateProjectRequest;
import com.MicroGuardAI.userservice.dto.ProjectResponse;
import com.MicroGuardAI.userservice.models.User;
import com.MicroGuardAI.userservice.repository.UserRepository;
import com.MicroGuardAI.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/api/users/me")
    public Map<String, String> registerMe(@AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject();

        // ✅ Pull all possible name/email fields from JWT
        String email = jwt.getClaimAsString("email");
        String preferredUsername = jwt.getClaimAsString("preferred_username");
        String givenName = jwt.getClaimAsString("given_name");
        String name = jwt.getClaimAsString("name");

        // ✅ Best-effort email: use email claim, else derive from username if it looks like email
        String resolvedEmail = email;
        if (resolvedEmail == null || resolvedEmail.isBlank()) {
            if (preferredUsername != null && preferredUsername.contains("@")) {
                resolvedEmail = preferredUsername;
            } else {
                resolvedEmail = "unknown";
            }
        }

        // ✅ Best-effort name: prefer full name > preferred_username > given_name
        String resolvedName = name;
        if (resolvedName == null || resolvedName.isBlank()) {
            resolvedName = preferredUsername;
        }
        if (resolvedName == null || resolvedName.isBlank()) {
            resolvedName = givenName;
        }
        if (resolvedName == null || resolvedName.isBlank()) {
            resolvedName = "unknown";
        }

        final String finalEmail = resolvedEmail;
        final String finalName = resolvedName;

        // ✅ If user exists, update email if it was previously unknown
        userRepository.findByKeycloakId(keycloakId).ifPresentOrElse(
                existing -> {
                    boolean dirty = false;
                    if ("unknown".equals(existing.getEmail()) && !"unknown".equals(finalEmail)) {
                        existing.setEmail(finalEmail);
                        dirty = true;
                    }
                    if ("unknown".equals(existing.getName()) && !"unknown".equals(finalName)) {
                        existing.setName(finalName);
                        dirty = true;
                    }
                    if (dirty) userRepository.save(existing);
                },
                () -> {
                    User u = new User();
                    u.setKeycloakId(keycloakId);
                    u.setEmail(finalEmail);
                    u.setName(finalName);
                    userRepository.save(u);
                }
        );

        return Map.of("keycloakId", keycloakId, "name", finalName, "email", finalEmail);
    }

    @PostMapping("/api/projects")
    public ProjectResponse createProject(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody CreateProjectRequest request) {
        return userService.createProject(jwt.getSubject(), request);
    }

    @GetMapping("/api/projects")
    public List<ProjectResponse> getProjects(@AuthenticationPrincipal Jwt jwt) {
        return userService.getProjects(jwt.getSubject());
    }

    @PutMapping("/api/projects/update-result")
    public void updateResult(@RequestBody Map<String, String> body) {
        userService.updateAiResult(body.get("projectId"), body.get("aiResult"));
    }
}