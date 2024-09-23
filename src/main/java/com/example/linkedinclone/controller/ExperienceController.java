package com.example.linkedinclone.controller;

import com.example.linkedinclone.entity.Experience;
import com.example.linkedinclone.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/experience")
public class ExperienceController {

    @Autowired
    private ExperienceService experienceService;


    @GetMapping("/user/{userId}")
    public List<Experience> getUserExperiences(@PathVariable String username) {
        return experienceService.getExperiencesByUsername(username);
    }


    @PostMapping("/add")
    public Experience addExperience(@RequestBody Experience experience) {
        return experienceService.saveExperience(experience);
    }


    @DeleteMapping("/delete/{id}")
    public void deleteExperience(@PathVariable Long id) {
        experienceService.deleteExperience(id);
    }
}
