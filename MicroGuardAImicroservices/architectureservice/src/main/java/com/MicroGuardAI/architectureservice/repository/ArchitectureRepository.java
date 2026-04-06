package com.MicroGuardAI.architectureservice.repository;

import com.MicroGuardAI.architectureservice.models.Architecture;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArchitectureRepository extends MongoRepository<Architecture, String> {

    List<Architecture> findByUserId(String userId);
}