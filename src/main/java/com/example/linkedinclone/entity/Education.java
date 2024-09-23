package com.example.linkedinclone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Table(name="educations")
@AllArgsConstructor
@NoArgsConstructor
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String schoolName;
    private String fieldOfStudy;
    private String degree;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isCurrentlyStudying;
    // Many-to-one relationship with Profile
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
