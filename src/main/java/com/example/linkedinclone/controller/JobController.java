package com.example.linkedinclone.controller;

import com.example.linkedinclone.entity.Job;
import com.example.linkedinclone.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    // Define the directory to save uploaded resumes
    private static final Logger logger = LoggerFactory.getLogger(JobController.class);
    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/jobs/apply")
    public String applyForJob(@RequestParam("resume") MultipartFile resume,
                              @RequestParam("jobId") Long jobId,
                              RedirectAttributes redirectAttributes) {
        // Log the incoming job application request
        logger.info("Received job application for job ID: {}", jobId);

        if (resume.isEmpty()) {
            logger.warn("No file uploaded for job application with job ID: {}", jobId);
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/jobs";
        }

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            System.out.println("Saving file to: " + uploadPath.toAbsolutePath());
            // Ensure the uploads directory exists
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // Save the file to the target location
            byte[] bytes = resume.getBytes();
            Path path = uploadPath.resolve(Objects.requireNonNull(resume.getOriginalFilename()));
            Files.write(path, bytes);

            // Log the success of file upload
            logger.info("File uploaded successfully for job ID: {} - File: {}", jobId, resume.getOriginalFilename());

            redirectAttributes.addFlashAttribute("message", "You successfully applied for the job!");
        } catch (IOException e) {
            // Log the error with stack trace
            logger.error("Error occurred while uploading file for job ID: {}", jobId, e);
            redirectAttributes.addFlashAttribute("message", "File upload failed.");
        }

        return "redirect:/jobs";
    }
}
