package com.example.linkedinclone.controller;

import com.example.linkedinclone.entity.Job;
import com.example.linkedinclone.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class JobController {

    @Autowired
    private JobService jobService;

    // show recommended jobs
    @GetMapping("/jobs")
    public String getJobs(Model model) {
        // get the list
        List<Job> topJobs = jobService.getTopJobs();
        model.addAttribute("topJobs", topJobs);
        return "jobs";
    }

    // search function
    @GetMapping("/jobs/search")
    public String searchJobs(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Job> jobs;
        if (keyword != null && !keyword.isEmpty()) {
            // search by keywords
            jobs = jobService.searchJobs(keyword);
        } else {
            // Return an empty list to indicate no jobs were found due to an empty search keyword
            jobs = new ArrayList<>();
        }
        model.addAttribute("jobs", jobs);  // add result to model
        model.addAttribute("topJobs", jobService.getTopJobs());
        return "jobs";
    }

    // show job detail page
    @GetMapping("/jobs/{id}")
    public String showJobDetails(@PathVariable("id") Long id, Model model) {
        Optional<Job> job = jobService.findJobById(id);
        // If the job exists, add it to the model and return the "jobDetail" view
        if (job.isPresent()) {
            model.addAttribute("job", job.get());
            return "jobDetail";
        } else {
            // If no, add error message, redirect to jobs page
            model.addAttribute("error", "Job not found");
            return "redirect:/jobs";
        }
    }

    // Show the job application page
    @GetMapping("/jobs/apply/{id}")
    public String showApplyJobPage(@PathVariable Long id, Model model) {
        // Find the job by its ID
        Optional<Job> jobOpt = jobService.findJobById(id);
        // Add the job details to the model
        model.addAttribute("job", jobOpt);
        return "jobDetail";
    }

    // Handle job application form submission
    @PostMapping("/jobs/apply/{id}")
    public String applyForJob(@PathVariable Long id, MultipartFile resume) {
        // Directory to store uploaded files
        String uploadDir = "uploads/";
        File dir = new File(uploadDir);
        // If the directory doesn't exist, attempt to create it
        if (!dir.exists()) {
            boolean created = dir.mkdir();
            if (!created) {
                // Log an error if the directory cannot be created and redirect to the job list page
                log.error("Failed to create upload directory: {}", uploadDir);
                return "redirect:/jobs";
            }
        }

        // Get the uploaded resume's file name
        String fileName = resume.getOriginalFilename();
        if (fileName == null) {
            // Log an error if the file name is null, redirect to jobs page
            log.error("Uploaded file name is null for job ID: {}", id);
            return "redirect:/jobs";
        }

        // Save the uploaded file in the specified directory
        File uploadedFile = new File(dir, fileName);
        try {// Transfer the uploaded file to the server
            resume.transferTo(uploadedFile);
            // Additional application logic, such as saving the job application to the database, can go here
        } catch (IOException e) {
            // Log any file transfer errors and handle the exception (e.g., redirect to an error page)
            log.error("File upload error for job ID: {} - {}", id, e.getMessage());
        }
        return "redirect:/jobs";
    }
}
