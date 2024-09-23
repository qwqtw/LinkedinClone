package com.example.linkedinclone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Table(name="experiences")
@AllArgsConstructor
@NoArgsConstructor
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String company;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isCurrentlyWorking;
    private String description;
    // Many-to-one relationship with Profile
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
