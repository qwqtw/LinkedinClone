package com.example.linkedinclone.service;

import com.example.linkedinclone.entity.Experience;
import com.example.linkedinclone.repository.ExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperienceService {

    @Autowired
    private ExperienceRepository experienceRepository;


    public List<Experience> getExperiencesByUsername(String username) {
        return experienceRepository.findByUsername(username);  // Use username here
    }


    public Experience saveExperience(Experience experience) {
        return experienceRepository.save(experience);
    }


    public void deleteExperience(Long experienceId) {
        experienceRepository.deleteById(experienceId);
    }
}
