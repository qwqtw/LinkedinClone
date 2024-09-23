package com.example.linkedinclone.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Table(name="jobs")
@AllArgsConstructor
@NoArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Job title is required")
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recruiter_id", nullable = false)
    private Recruiter recruiter;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Job description is required")
    @Size(min = 50, max = 2000, message = "Job description must contain between {min} and {max} characters")
    private String description;

    @NotBlank(message = "keywords are required")
    private String keywords;

    @NotNull(message = "Posted date is required")
    @PastOrPresent(message = "Posted date cannot be in the future")
    private LocalDate postedDate;
}
