package com.example.linkedinclone.repository;

import com.example.linkedinclone.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findByUsername(String username);
}
