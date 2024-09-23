package com.example.linkedinclone.service;

import com.example.linkedinclone.entity.Job;
import com.example.linkedinclone.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public void saveJob(Job job) {
        jobRepository.save(job);
    }

    // Get all the jobs
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // Search jobs
    public List<Job> searchJobs(String keyword) {
        //
        if (keyword == null || keyword.trim().isEmpty()) {
            // if result is empty, return an empty list
            return List.of();
        }
        // call to perform
        return jobRepository.searchJobs(keyword.trim());
    }

    // Retrieve the three most recently posted jobs
    public List<Job> getTopJobs() {
        return jobRepository.findTop3ByOrderByPostedDateDesc();
    }

    // Get job by id
    public Optional<Job> findJobById(Long id) {
        return jobRepository.findById(id);
    }
}
